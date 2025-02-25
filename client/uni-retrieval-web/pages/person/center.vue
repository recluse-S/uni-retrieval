<template>
	<view id="app">
		<view class="uni-container" style="background: linear-gradient(#1787E0, #FFFFFF); z-index: -1;
					 height: 20vh; position: absolute; top: -1px; left: 0;">
		</view>

		<view class="top">
			<view style="font-size: 16px;" class="line"> 尊敬的用户 {{phone}} ,</view>
			<view style="font-size: 24px; color: #1787E0;" class="line"> 您好！ </view>

			<view style="height: 20px;"></view>

			<view v-if="isUpTimePast()">
				<view style="display: flex; align-items: center;">
					<view> 您还不是会员，</view>
					<view style="color: #1787E0; cursor: pointer;" @click="showModal = true"> 点击开通 </view>
				</view>
			</view>
			<view v-else>
				<view style="color: #999; font-size: 14px;" class="line"> 您已是尊贵的会员用户，</view>
				<view style="color: #999; font-size: 14px;" class="line"> 会员时间截至到 {{getDay(upTime)}} </view>
			</view>
		</view>

		<!-- 弹窗 -->
		<view v-if="showModal" class="modal">
			<view class="modal-content">
				<view class="modal-header">
					<button class="close-btn" @click="closeModal"> × </button>
				</view>
				<view class="modal-body">
					<text class="modal-message">您的查询次数已用完，请充值开通获得查询资格</text>
					<view class="price-box">
						<text class="price-title">开通一年</text>
						<text class="price-value">¥{{ (paymentAmount / 100).toFixed(2).replace(/\.00$/, '') }}</text>
					</view>
					<view style="color: #999; font-size: 14px; margin-bottom: 20px;"> *温馨提示: 本产品为虚拟内容服务，购买成功后不支持退款
					</view>
					<button class="pay-btn" @click="onPay">立即开通</button>
				</view>
			</view>
		</view>

		<view class="function">
			<view style="display: flex; flex-direction: row;" @click="navCollection()">
				<view class="icon">
					<image src="../../static/collection-false.png"></image>
				</view>
				<view class="text">
					我的收藏
				</view>
			</view>

			<div class="divider"></div>

			<view style="display: flex; flex-direction: row;" @click="navModel()">
				<view class="icon">
					<image src="../../static/订单.png"></image>
				</view>
				<view class="text">
					我的订单
				</view>
			</view>
		</view>

	</view>
</template>

<script>
	import {
		wxchatPay,
		getUserInfo,
		deleteUsableTimes,
		updateUpTime,
		getPaymentAmount
	} from "@/common/apis.js"

	import {
		getCache
	} from '../../utils/cache.js'

	export default {
		data() {
			return {
				phone: '',

				orderId: '', // 用来保存订单号 *微信支付用
				paymentData: null, // 用来保存支付数据 *微信支付用
				upTime: '',
				usableTimes: '',
				showModal: false, // 控制弹窗的显示与隐藏
				paymentAmount: 0,
			}
		},

		mounted() {
			this.getUserInformation();
			this.getPayAmount();
		},

		onShow() {
			this.getUserInformation();
		},

		computed: {
			formattedUpTime() {
				// 创建 Date 对象并格式化为年月日（不包含时间）
				const date = new Date(this.upTime);
				return date.toLocaleDateString('zh-CN'); // 可以根据需要调整地区格式
			}
		},

		methods: {
			// *************************** 微信支付功能区 ***************************
			// 用户点击支付按钮时触发
			onPay() {
				const token = getCache("token"); // 获取 token
				if (!token) {
					uni.showToast({
						title: '请先登录',
						icon: 'none',
					});
					return; // 中断请求
				}
				// 调用后端接口生成订单
				this.generateOrder().then((paymentData) => {
					this.paymentData = paymentData;
					// 调起微信支付
					this.requestPayment(paymentData);
				}).catch((err) => {
					uni.showToast({
						title: '创建订单失败',
						icon: 'none',
					});
					console.error('支付订单创建失败', err);
				});
			},
			// 请求后端创建订单并返回支付数据
			generateOrder() {
				return new Promise((resolve, reject) => {
					wxchatPay()
						.then((res) => {
							console.log('----------------');
							if (res && res.code === 1) {
								resolve(res.data); // 返回支付所需的参数
							} else {
								reject('生成订单失败-------------');
							}
						})
						.catch((err) => {
							reject(err);
						});
				});
			},
			// 调起微信支付
			// 将支付函数包装成一个返回 Promise 的函数
			requestPaymentAsync(paymentData) {
				return new Promise((resolve, reject) => {
					uni.requestPayment({
						provider: 'wxpay',
						timeStamp: String(paymentData.timeStamp),
						nonceStr: paymentData.nonceStr,
						package: paymentData.package,
						signType: String(paymentData.signType),
						paySign: paymentData.paySign,
						tradeNo: paymentData.tradeNo,
						// success: (resolve) => resolve(resolve), // 支付成功时 resolve
						success: (res) => {
							// 支付成功时，将支付结果和 tradeNo 一并返回
							resolve({
								...res,
								tradeNo: paymentData.tradeNo
							});
						},
						fail: (err) => reject(err), // 支付失败时 reject
					});
				});
			},
			async requestPayment(paymentData) {
				try {
					// 先等待支付成功
					const paymentResult = await this.requestPaymentAsync(paymentData);
					
					// 支付成功后执行 updateUpTime 和 getUserInformation
					const tradeNo = paymentResult.tradeNo;

					await updateUpTime(tradeNo); // 等待 updateUpTime 执行完成
					await this.getUserInformation(); // 等待 getUserInformation 执行完成

					uni.showToast({
						title: '支付成功',
						icon: 'success',
					});
					console.log('支付成功', paymentResult);
				} catch (error) {
					uni.showToast({
						title: '支付失败',
						icon: 'none',
					});
					console.error('支付失败', error);
				}
			},

			getUserInformation() {
				getUserInfo().then(response => {
						const data = response.data;
						this.upTime = data.upTime;
						this.usableTimes = data.usableTimes;
						this.phone = data.phone;
					})
					.catch(error => {
						console.error("出错", error);
					})
			},
			closeModal() { // 关闭弹窗
				this.showModal = false;
			},
			// ******************************************************
			async getPayAmount() {
				try {
					const response = await getPaymentAmount(); // 假设 getHots 直接返回 Promise
					const data = response.data;
					this.paymentAmount = data;
					console.log(this.paymentAmount)
				} catch (error) {
					console.error("请求出错:", error); // 捕获错误
				}
			},

			isUpTimePast() {
				// 将 upTime 字符串转换为 Date 对象
				const upTimeDate = new Date(this.upTime);
				const currentDate = new Date(); // 当前时间
				return upTimeDate <= currentDate; // 返回比较结果
			},

			//详情页
			navCollection() {
				uni.navigateTo({
					url: '/pages/collection/collection'
				});
			},
			navModel() {
				uni.navigateTo({
					url: '/pages/person/model'
				});
			},

			getDay(releaseTime) {
				const date = new Date(releaseTime);
				const year = date.getFullYear();
				const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始
				const day = String(date.getDate()).padStart(2, '0');
				return `${year}-${month}-${day}`; // 格式化为 YYYY-MM-DD
			},

		}
	}
</script>

<style>
	Page {
		padding-top: 85px;
	}

	#app {
		/* background-color: #F1F5FF; */
		width: 100%;
		height: 100%;
	}

	.uni-container {
		display: flex;
		justify-content: center;
		background-color: rgba(24, 186, 244, 0.6);
		/* 80%淡化的颜色 */
		position: relative;
		width: 100%;
		font-size: 0.8em;
	}

	/* 分割线 */
	.divider {
		width: 100%;
		height: 0.4px;
		background-color: #ccc;
		margin: 10px 0;
	}

	.top {
		background-color: #fff;
		width: 80%;
		padding: 25rpx 25rpx;
		border-radius: 25rpx;
		margin: 30rpx auto;
		display: flex;
		margin-top: 80rpx;
		border-bottom: 1px solid #8b8b8b;
		background-color: #fff;
		position: relative;
		top: -100px;
		flex-direction: column;
		align-items: flex-start;
		justify-content: center;
	}

	.line {
		display: block;
		margin-bottom: 10px;
		/* 可选，调整行间距 */
	}

	.function {
		width: 80%;
		/* 横向排列 */
		box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
		padding: 10px;
		position: relative;
		left: 8%;
		border-radius: 10px;
		/* 设置圆角，值可以调整 */
		top: -100px;
	}

	.icon {
		width: 20px;
		height: 20px;
		margin-right: 10px;
	}

	.icon image {
		width: 100%;
		height: 100%;
		object-fit: cover;
	}

	.text {
		flex-grow: 1;
		font-size: 16px;
	}




	/* 弹窗背景 */
	.modal {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background: rgba(0, 0, 0, 0.7);
		/* 半透明背景 */
		display: flex;
		justify-content: center;
		align-items: center;
		z-index: 9999;
	}

	.modal-content {
		position: relative;
		width: 80%;
		max-width: 500px;
		padding: 20px;
		background-color: #fff;
		border-radius: 10px;
		box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	}

	.modal-body {
		display: flex;
		flex-direction: column;
		align-items: center;
		text-align: center;
	}

	.modal-header {
		height: 40rpx;
		margin-left: 90%;
	}

	.close-btn {
		background: transparent;
		border: none;
		font-size: 24px;
		color: #333;
		cursor: pointer;
		width: 30px;
		height: 30px;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	/*   弹窗正文   */
	.modal-body {
		padding-top: 40px;
	}

	.modal-message {
		font-size: 14px;
		color: #555;
		margin-bottom: 20px;
	}

	.price-box {
		width: 120px;
		height: 120px;
		padding: 10px;
		border-radius: 8px;
		margin-bottom: 20px;
		border: 1px solid #ddd;
		box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}

	.price-title {
		font-size: 14px;
		font-weight: bold;
		color: #333;
	}

	.price-value {
		font-size: 32px;
		font-weight: bold;
		color: #e74c3c;
		margin-top: 10px;
	}

	/* 立即开通按钮 */
	.pay-btn {
		background-color: #3498db;
		color: white;
		padding: 8px 0;
		border: none;
		border-radius: 50px;
		/* 圆弧框 */
		cursor: pointer;
		font-size: 16px;
		width: 66%;
		max-width: 300px;
	}

	.pay-btn:hover {
		background-color: #2980b9;
	}
</style>