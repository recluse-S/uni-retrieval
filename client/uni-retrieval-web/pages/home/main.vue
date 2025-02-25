<template>
	<view id="app">
		<!-- 弹窗 -->
		<view v-if="showPay" class="modal">
			<view class="modal-content-pay" v-if="isUpTimePast()">
				<view class="modal-header">
					<button class="close-btn" @click="closePayModal"> × </button>
				</view>
				<view class="modal-body">
					<text class="modal-message">您的查询次数已用完，请充值开通获得查询资格</text>
					<view class="price-box">
						<text class="price-title">开通一年</text>
						<text class="price-value">
							¥{{ (paymentAmount / 100).toFixed(2).replace(/\.00$/, '') }}
						</text>

					</view>
					<view style="color: #999; font-size: 14px; margin-bottom: 20px;"> *温馨提示: 本产品为虚拟内容服务，购买成功后不支持退款
					</view>
					<button class="pay-btn" @click="onPay">立即开通</button>
				</view>
			</view>
			<view v-else class="modal-content-pay">
				<view class="modal-header">
					<button class="close-btn" @click="closePayModal"> × </button>
				</view>
				<view> 您已是会员用户，无需再次开通 ~ </view>
			</view>
		</view>

		<view class="logo">
			<image src="../../static/CMlogo.png" style="width: 50px; height: 50px;"></image>
			<view class="logo-text">
				<a style="font-family: '宋体'; font-weight: bold; margin-top: 3px;"> 山东高校考试资讯总览 </a>
				<!-- <a style="font-family: '宋体'; font-weight: bold; margin-top: 3px;"> 山东高校考试总览 </a> -->
				<p></p>
			</view>
		</view>
		

		<view style="position: fixed; bottom: 220px; right: 20px; width: 50px; height: 50px; background-color: #25579e;
			           color: white; text-align: center; line-height: 16px; border-radius: 50%; cursor: pointer; z-index: 99;
			           font-size: 12px; display: flex; flex-direction: column; justify-content: center; align-items: center;
					   border: 4px solid white;" @click="navigateToXCX">
			<span>真题</span>
			<span>资料</span>
		</view>

		<view style="position: fixed; bottom: 300px; right: 20px; width: 50px; height: 50px; background-color: #25579e; 
		               color: white; text-align: center; line-height: 16px; border-radius: 50%; cursor: pointer; z-index: 99;
		               font-size: 12px; display: flex; flex-direction: column; justify-content: center; align-items: center;
					   border: 4px solid white;" @click="showModal = true">
			<span>点我</span>
			<span>咨询</span>
		</view>
		<!-- 弹窗 -->
		<view v-if="showModal" class="modal" @click.self="closeModal">
			<view class="modal-content">
				<!-- <image src="https://via.placeholder.com/300" alt="图片" class="modal-image"></image> -->
				<image :show-menu-by-longpress="true" src="../../static/二维码.jpg" @click="previewImage2"
					class="modal-image"></image>
			</view>
		</view>

		<view style="position: fixed; bottom: 140px; right: 20px; width: 50px; height: 50px; background-color: #25579e;
		               color: white; text-align: center; line-height: 16px; border-radius: 50%; cursor: pointer; z-index: 99;
		               font-size: 12px; display: flex; flex-direction: column; justify-content: center; align-items: center;
					   border: 4px solid white;" @click="showPay = true">
			<span>开通</span>
			<span>会员</span>
		</view>

		<!-- 搜索结果 -->
		<view class="search-result">
			<view class="container">
				<!-- <view style="position: relative; height: 20px; width: 120%; right: 10%; background-color: #25579e;">
					<view
						style="color: white; font-size: 13px;display: flex; justify-content: center; align-items: center;">
						山东高校考试公告汇总
					</view>
				</view> -->

				<!-- 推送简章 -->
				<view v-for="(item, index) in insHOT" :key="index" class="firstRes">
					<view class="cornerBox"> <a>{{ item.location }}</a> </view>
					<view style="padding: 10px;">
						<view class="institution-header">
							<view style="width: 36px; border-radius: 10px 0 0 10px; height: 18px; background-color: #eef0fd; 
							                 color: #144dff; font-size: 10px; display: flex; align-items: center; justify-content: center;">
								{{ getYear(item.release_time) }}
							</view>
							<view
								style="font-size: 18px; font-family: '楷体'; font-weight: bold; margin-left: 10px; position: relative; width: 75%; display: flex; align-items: center;">
								<view>{{ item.institution }}</view>
								<view class="dynamic">hot</view>
							</view>

						</view>
						<view style="color: #999; font-size: 13px; font-family: '楷体';">
							该单位共招 &nbsp;
							<span
								style="margin-left: 4px; margin-right: 4px;">{{ item.count !== 0 && item.count !== null ? item.count : '- -' }}</span>
							&nbsp; 人
						</view>
						<view style="margin-top: 6px; margin-left: 4px;">
							<a style="display: inline; font-size: 12px; font-weight: bold;">报名时间：</a>
							<span style="font-size: 12px; color: #999;"> {{item.created_time}}</span>
							<p></p>
							<a style="display: inline; font-size: 12px; font-weight: bold;">公告时间： </a>
							<span style="font-size: 12px; color: #999;"> {{getDay(item.release_time)}} </span>
						</view>
						<view class="toggle-arrow" @click="toggleInst(item.id)">
							<span style="color: #25579e;">查看详情</span>
						</view>

						<view v-if="Inid === item.id" class="institution-buttom">
							<view class="canvas"></view>
							<view v-for="(post, postIndex) in item.posts" :key="postIndex" style="margin-top: 8px">
								<view style="display: flex;" @click="sendId(post.id)">
									<view style="font-size: 16px; font-family: '黑体'; margin-top: 8px;">
										<a style="display: inline-block; ">
											{{ post.postName }}
										</a>
										<view
											style="width: 36px; border-radius: 0 10px 10px 0; height: 18px; background-color: #eef0fd;
						                             color: #144dff; font-size: 10px; display: inline-flex; align-items: center; justify-content: center; margin-left: 4px;">
											{{ post.postGrade || '--' }}
										</view>
										</p>
										<a style="display: inline; font-size: 12px; margin-right: 3px; color: #999;">
											共招
										</a>
										<span style="font-size: 12px; color: #32CD32; margin-right: 3px;">
											{{ post.recruitingNum || '--' }}</span>
										<a style="display: inline; font-size: 12px; margin-right: 50px; color: #999;">
											人
										</a>
										<!-- <a style="display: inline; font-size: 13px;"> ️ </a> -->
										<image src="../../static/eye.png"
											style="height: 16px; width: 24px; position: relative; top: 5px; margin-right: 3px;">
										</image>
										<span style="font-size: 12px; color: #32CD32;">
											{{ post.view || '--' }}</span>
										</p>
										<a style="display: inline; font-size: 12px; font-weight: bold;">岗位类别：</a>
										<span style="font-size: 12px; color: #999; margin-right: 50px;">
											{{ post.postCategory || '--' }}</span>
										<a style="display: inline; font-size: 12px; font-weight: bold;">学历要求： </a>
										<span style="font-size: 12px; color: #999;">
											{{ post.reqDegree || '--' }}</span>
									</view>
								</view>
							</view>
						</view>
					</view>
				</view>

				<!-- 推荐列表 -->
				<view v-for="(item, index) in institutions" :key="index" class="firstRes">
					<view class="cornerBox"> <a>{{ item.location }}</a> </view>

					<view style="padding: 10px;">
						<view class="institution-header">
							<view style="width: 36px; border-radius: 10px 0 0 10px; height: 18px; background-color: #eef0fd; 
							                 color: #144dff; font-size: 10px; display: flex; align-items: center; justify-content: center;">
								{{ getYear(item.release_time) }}
							</view>
							<view
								style=" font-size: 18px; font-family: '楷体'; font-weight: bold; margin-left: 10px;position: relative; width: 75%;">
								{{ item.institution }}
							</view>
						</view>
						<view style="color: #999; font-size: 13px; font-family: '楷体';">
							该单位共招 &nbsp;
							<span
								style="margin-left: 4px; margin-right: 4px;">{{ item.count !== 0 && item.count !== null ? item.count : '- -' }}</span>
							&nbsp; 人
						</view>
						<view style="margin-top: 6px; margin-left: 4px;">
							<a style="display: inline; font-size: 12px; font-weight: bold;">报名时间：</a>
							<span style="font-size: 12px; color: #999;"> {{item.created_time}}</span>
							<p></p>
							<a style="display: inline; font-size: 12px; font-weight: bold;">公告时间： </a>
							<span style="font-size: 12px; color: #999;"> {{getDay(item.release_time)}} </span>
						</view>
						<!-- :class="{ 'expanded': Inid === item.id }" -->
						<view class="toggle-arrow" @click="toggleInst(item.id)">
							<span style="color: #25579e;">查看详情</span>
						</view>

						<view v-if="Inid === item.id" class="institution-buttom">
							<view class="canvas"></view>
							<view v-for="(post, postIndex) in item.posts" :key="postIndex" style="margin-top: 8px">
								<view style="display: flex;" @click="sendId(post.id)">
									<!-- <view @click="postLike(post.id)" class="collection">
										<img :src="post.liked ? '../../static/collection-true.png' : '../../static/collection-false.png'"
											alt="Collection status" />
									</view> -->

									<!-- 点击收藏时只触发 toggleLike 方法，使用 .stop 阻止事件冒泡 -->
									<!-- <view @click.stop="toggleLike(post)" class="collection"
										style="display: flex; flex-direction: column; align-items: center; margin-left: 10px;">
										<img :src="post.liked ? '../../static/collection-true.png' : '../../static/collection-false.png'"
											alt="Collection status" />
										<text style="font-size: 12px;">收藏</text> 
									</view> -->

									<view style="font-size: 16px; font-family: '黑体'; margin-top: 8px;">
										<!-- <a style="color: #1787E0; display: inline-block; font-family: '楷体';"> -->
										<a style="display: inline-block; ">
											{{ post.postName }}
										</a>
										<view
											style="width: 36px; border-radius: 0 10px 10px 0; height: 18px; background-color: #eef0fd;
						                             color: #144dff; font-size: 10px; display: inline-flex; align-items: center; justify-content: center; margin-left: 4px;">
											{{ post.postGrade || '--' }}
										</view>
										</p>
										<a style="display: inline; font-size: 12px; margin-right: 3px; color: #999;">
											共招
										</a>
										<span style="font-size: 12px; color: #32CD32; margin-right: 3px;">
											{{ post.recruitingNum || '--' }}</span>
										<a style="display: inline; font-size: 12px; margin-right: 50px; color: #999;">
											人
										</a>
										<!-- <a style="display: inline; font-size: 13px;"> ️ </a> -->
										<image src="../../static/eye.png"
											style="height: 16px; width: 24px; position: relative; top: 5px; margin-right: 3px;">
										</image>
										<span style="font-size: 12px; color: #32CD32;">
											{{ post.view || '--' }}</span>
										</p>
										<a style="display: inline; font-size: 12px; font-weight: bold;">岗位类别：</a>
										<span style="font-size: 12px; color: #999; margin-right: 50px;">
											{{ post.postCategory || '--' }}</span>
										<a style="display: inline; font-size: 12px; font-weight: bold;">学历要求： </a>
										<span style="font-size: 12px; color: #999;">
											{{ post.reqDegree || '--' }}</span>
									</view>
								</view>
							</view>
						</view>
					</view>
				</view>

			</view>
		</view>

		<button v-if="hasMoreData" @click="loadMore"
			style="position: relative; width: 120px; background-color: #25579e; color: white; font-size: 16px;">
			加 载 更 多
		</button>

		<!-- 底部留白 -->
		<view style="position: relative; height: 50px;"> </view>
	</view>
</template>

<script>
	import {
		findFirstSub,
		getSecondSub,
		getThirdlySub,
		getSearchResults,
		postOwnLike,
		getALL,
		getAllSelect,
		getRecommend,
		wxchatPay,
		getUserInfo,
		deleteUsableTimes,
		updateUpTime,
		getHot,
		getHots,
		getPaymentAmount
	} from "@/common/apis.js"

	import {
		getCache
	} from '../../utils/cache.js'

	export default {
		onShareAppMessage() {},
		onShareTimeline() {},

		data() {
			return {
				day: 6,
				maxAttempts: 10, // 最大尝试次数
				currentAttempt: 0, // 当前尝试次数

				academic: 1,
				upId: 1,
				firstId: 1,
				secondId: 1,
				majorPro: {},
				majorSec: {},
				theSelectedIndices: [],
				majorThi: {
					id: 0,
					major: '',
					index: 0
				},
				selectedIndices: [], //多选
				selectedIndicesName: [],
				selectedIndex: 0, // 记录选中的索引
				selectedMajor: 1, // 用于存储选中的专业
				Resul: [],
				ResultHOT: [],
				institutions: [],
				insHOT: [],
				Inid: null,
				inInid: null,
				countSum: 0,

				activeIndex: -1, // 初始值为-1，表示没有选中任何项
				activeIndex_max: -1, // 初始值为-1，表示没有选中任何项

				showMajorList: true, // 控制 major-list 显示的状态
				showRow: false,
				theShowRow: true,
				showSearch: false,
				showOverlay: true,
				hasMoreData: true, // 判断是否还有更多数据

				showPay: false, // 控制弹窗的显示与隐藏
				orderId: '', // 用来保存订单号 *微信支付用
				paymentData: null, // 用来保存支付数据 *微信支付用
				upTime: '',
				usableTimes: '',
				showModal: false, // 控制弹窗的显示与隐藏
				paymentAmount: 0,
			};
		},

		// onShow() {
		// 	this.getUserInformation();
		// },

		mounted() {
			this.recommendHOT();
			this.recommend();
			this.getPayAmount();
		},

		watch: {
			ResultHOT: 'processResultHots',
			Resul: 'processResults',

			firstId: {
				handler(newVal, oldVal) {
					if (newVal !== oldVal) {
						this.getDATA(this.firstId, this.upId);
						this.majorThi = {};
						this.activeIndex = -1;
					}
				},
			},
			secondId: {
				handler(newVal, oldVal) {
					if (newVal !== oldVal) {
						this.getDATA(this.firstId, this.upId);
						this.getDATA1(this.secondId);
					}
				},
			},
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
			// 修改 getUserInformation，返回一个 Promise
			getUserInformation() {
				return new Promise((resolve, reject) => {
					getUserInfo()
						.then(response => {
							const data = response.data;
							this.upTime = data.upTime;
							this.usableTimes = data.usableTimes;
							resolve(); // 异步操作成功时调用 resolve
						})
						.catch(error => {
							console.error("出错", error);
							reject(error); // 异常时调用 reject
						});
				});
			},
			closePayModal() { // 关闭弹窗
				this.showPay = false;
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

			navigateToXCX() {
				this.isNavigating = true;
				wx.navigateToMiniProgram({
					shortLink: '#小程序://诚铭微学堂/Pdu2XalHjahEpNz',
					//develop开发版；trial体验版；release正式版
					envVersion: 'release',
					success(res) {
						// 打开成功
						console.log("跳转小程序成功！", res);
					}
				})
			},

			// 这里定义点击事件的方法
			navigateToMiniProgram() {
				this.isNavigating = true;

				wx.navigateToMiniProgram({
					appId: 'wxa3589f1193170aa3',
					path: 'page/home',
					envVersion: 'release', // 环境版本，可选：'develop'、'trial'、'release'
					success(res) {
						// 打开成功时的回调
						console.log("跳转小程序成功！", res);
						wx.showToast({
							title: '成功跳转到小程序',
							icon: 'success',
							duration: 2000
						});
					},
					fail(err) {
						// 打开失败时的回调
						console.error("跳转小程序失败！", err);
						wx.showToast({
							title: '跳转失败，请稍后再试。',
							icon: 'none',
							duration: 2000
						});
					},
					complete() {
						// 无论成功或失败都会执行
						console.log('跳转操作已完成');
						this.isNavigating = false; // 完成后恢复状态
					}
				});
			},

			async recommendHOT() {
				try {
					const response = await getHots(); // 假设 getHots 直接返回 Promise
					const data = response.data;

					if (data && data.length > 0) {
						const uniqueData = data.filter(newItem =>
							!this.ResultHOT.some(existingItem => existingItem.id === newItem.id)
						);
						this.ResultHOT = this.ResultHOT.concat(uniqueData);
					}
				} catch (error) {
					console.error("请求出错:", error); // 捕获错误
				}
			},

			async recommend() {
				if (!this.hasMoreData || this.currentAttempt >= this.maxAttempts) return;
				try {
					const response = await getRecommend(this.day); // 假设 getRecommend 直接返回 Promise
					const data = response.data;

					if (data && data.length > 0) {
						const uniqueData = data.filter(newItem =>
							!this.Resul.some(existingItem => existingItem.id === newItem.id)
						);

						if (uniqueData.length > 0) {
							this.Resul = this.Resul.concat(uniqueData);
							this.day += 2; // 每次成功请求后更改 day 的值
							this.currentAttempt = 0; // 重置尝试次数
						} else {
							this.day += 3; // 没有新数据时增加 day 的值
							this.currentAttempt++; // 增加尝试次数
							await this.recommend(); // 递归调用推荐方法
						}
					} else {
						this.day += 3; // 没有数据时增加 day 的值
						this.currentAttempt++; // 增加尝试次数
						if (this.currentAttempt < this.maxAttempts) {
							await this.recommend(); // 递归调用推荐方法
						} else {
							this.hasMoreData = false; // 达到最大尝试次数
							console.log("没有更多数据可提供");
						}
					}
				} catch (error) {
					console.error("请求出错:", error); // 捕获错误
				}

				console.log('-----------------recommend-----------------')
				console.log(this.Resul)
				console.log(this.insHOT)
			},

			loadMore() {
				this.recommend(); // 点击“加载更多”时调用推荐方法
			},
			selectArea(area) {
				this.keyword4 = area;
			},
			selectPost(area) {
				this.keyword5 = area;
			},
			selectEstate(area) {
				this.keyword7 = area;
			},
			selectTime(area) {
				this.keyword9 = area;
			},
			selectMajor(id) {
				this.selectedMajor = id;
				if (id !== null) {
					this.sendIdToBackend(id);
				} else {
					this.allMajor();
				}
			},
			toggleInst(id) {
				this.Inid = this.Inid === id ? null : id;
			},
			toggleInInst(id) {
				this.inInid = this.inInid === id ? null : id;
			},

			processResults() {
				const instMap = new Map();
				this.Resul.forEach(item => {
					// 创建组合键，使用机构名和 releaseTime
					// const key = `${item.institution}|${item.releaseTime}`;
					const key = `${item.genRegulation}`;

					// 如果组合键不存在，则初始化
					if (!instMap.has(key)) {
						instMap.set(key, {
							id: item.id, // 这里可以选择保留第一个项目的机构 ID
							count: 0,
							posts: [],
							institution: item.institution, // 保存机构名
							location: item.location,
							release_time: item.releaseTime,
							created_time: item.createdTime,
							liked: item.liked,
							view: item.view
						});
					}

					const institution = instMap.get(key);
					institution.count += item.recruitingNum; // 累加招聘人数
					institution.posts.push({
						id: item.id, // 添加岗位的 ID
						postName: item.postName,
						postGrade: item.postGrade,
						postStatement: item.postStatement,
						reqDegree: item.reqDegree,
						postCategory: item.postCategory,
						recruitingNum: item.recruitingNum,
						liked: item.liked,
						view: item.view
						// 这里可以根据需要添加其他的岗位属性
					});
				});

				// 将 Map 转换为数组
				this.institutions = Array.from(instMap.values()).map(({
					id,
					count,
					posts,
					institution,
					location,
					release_time,
					created_time,
					liked,
					view
				}) => ({
					institution,
					id, // 这里保留整个机构的第一个 ID
					count,
					posts, // 岗位列表保留原有 ID
					location,
					release_time,
					created_time,
					liked,
					view
				}));

				// 检查机构数量，如果少于 3 个，则调用 loadMore 方法
				if (this.institutions.length < 3) {
					this.loadMore(); // 触发加载更多数据的方法
				}
			},

			processResultHots() {
				const instMap = new Map();
				this.ResultHOT.forEach(item => {
					// 创建组合键，使用机构名和 releaseTime
					// const key = `${item.institution}|${item.releaseTime}`;
					const key = `${item.genRegulation}`;

					// 如果组合键不存在，则初始化
					if (!instMap.has(key)) {
						instMap.set(key, {
							id: item.id, // 这里可以选择保留第一个项目的机构 ID
							count: 0,
							posts: [],
							institution: item.institution, // 保存机构名
							location: item.location,
							release_time: item.releaseTime,
							created_time: item.createdTime,
							liked: item.liked,
							view: item.view
						});
					}

					const institution = instMap.get(key);
					institution.count += item.recruitingNum; // 累加招聘人数
					institution.posts.push({
						id: item.id, // 添加岗位的 ID
						postName: item.postName,
						postGrade: item.postGrade,
						postStatement: item.postStatement,
						reqDegree: item.reqDegree,
						postCategory: item.postCategory,
						recruitingNum: item.recruitingNum,
						liked: item.liked,
						view: item.view
						// 这里可以根据需要添加其他的岗位属性
					});
				});

				// 将 Map 转换为数组
				this.insHOT = Array.from(instMap.values()).map(({
					id,
					count,
					posts,
					institution,
					location,
					release_time,
					created_time,
					liked,
					view
				}) => ({
					institution,
					id, // 这里保留整个机构的第一个 ID
					count,
					posts, // 岗位列表保留原有 ID
					location,
					release_time,
					created_time,
					liked,
					view
				}));
			},

			sendIdToBackend(id) {
				// 处理逻辑
				if (id === 0) {
					this.this.selectedIndicesName = [];
					this.hideMajorList();
				}
				this.activeIndex = -1;
				this.majorSec = [];
				this.majorThi = [];
				this.activeIndex_max = null;
				this.selectedIndices = [];
				this.selectedIndicesName = [];
				this.theSelectedIndices = [];
				this.showRow = false;
				this.theShowRow = false;
			},
			hideMajorList() {
				this.selectedIndicesName = [];
				this.showMajorList = false;
				this.keyword6 = '本科';
				this.keyword3 = '';
			},
			allMajor() {
				this.showMajorList = false;
				this.keyword6 = '';
				this.keyword3 = '';
				this.selectedIndicesName = [];
			},
			sendIdToBackend(id) {
				this.upId = id;
				this.majorSec = [];
				this.majorThi = [];
				this.activeIndex = -1;
				this.activeIndex_max = null;
				this.selectedIndices = [];
				this.selectedIndicesName = [];
				this.theSelectedIndices = [];
				this.showMajorList = id === 0 ? false : true;
				this.keyword6 = '';
				this.showRow = false;
				this.theShowRow = false;
			},
			//详情页
			async sendId(id) {
				try {
					// 等待 getUserInformation 执行完
					await this.getUserInformation();
					// 获取当前时间
					const currentTime = new Date();
					// 将 this.upTime 字符串转为 Date 对象
					const upTimeDate = new Date(this.upTime);
					// 如果条件不满足，则显示支付弹窗并终止
					if (upTimeDate <= currentTime && this.usableTimes <= 0) {
						this.showPay = true; // 显示弹窗
						return; // 跳出执行
					}
					// 执行删除可用次数
					deleteUsableTimes();
					// 跳转到目标页面
					uni.navigateTo({
						url: `/pages/pageData/pageData?id=${id}`
					});
				} catch (error) {
					// 异常处理
					console.error("获取用户信息失败", error);
				}
			},

			toggleActive(index, id) {
				if (this.activeIndex === index) {
					this.theSelectedIndices = [];
					this.activeIndex = -1;
					this.selectedIndices = [];
					this.selectedIndicesName = [];
				} else {
					this.showRow = false;
					this.theShowRow = true;
					this.theSelectedIndices = [index];
					this.activeIndex = index;
					this.secondId = id;
					this.selectedIndices = [];
					this.selectedIndicesName = [];

					console.log(JSON.stringify(this.theSelectedIndices))
				}
			},
			//第三个
			toggle(index, name) {
				if (this.selectedIndices.includes(index)) {
					this.selectedIndices = this.selectedIndices.filter(i => i !== index);
					this.selectedIndicesName = this.selectedIndicesName.filter(i => i !== name);
				} else {
					this.selectedIndices.push(index);
					this.selectedIndicesName.push(name);
				}
			},
			selectAll3() {
				this.selectedIndices = this.majorThi.map((_, index) => index);
				this.selectedIndicesName = this.majorThi.map((_, name) => name);
			},
			deselectAll3() {
				this.selectedIndices = [];
			},
			//收藏
			toggleLike(post) {
				// 切换本地状态
				post.liked = !post.liked;

				// 调用postLike进行服务器请求
				this.postLike(post.id);
			},

			postLike(id) {
				postOwnLike(id).then(resp => {
					if (resp.msg === "success") {
						console.log(resp.data);
						// 根据服务器响应调整提示
						if (resp.data === "新增成功") {
							uni.showToast({
								title: '收藏成功',
								icon: 'success', // 显示成功图标
								duration: 2000 // 持续时间，单位为毫秒
							});
						} else if (resp.data === "刪除成功") {
							uni.showToast({
								title: '取消收藏',
								icon: 'success', // 显示成功图标
								duration: 2000 // 持续时间，单位为毫秒
							});
						}
						// this.searchex(); // 如果需要在这里更新数据，可以取消注释
					} else {
						console.log("收藏失败");
						// 如果失败，可以恢复原状态
					}
				}).catch(error => {
					console.log("请求失败", error);
				});
			},
			toggleSearch() {
				this.showSearch = !this.showSearch;
				this.showOverlay = !this.showSearch; // 点击时切换 overlay
			},
			getYear(dateString) {
				const date = new Date(dateString);
				return date.getFullYear();
			},

			getDay(releaseTime) {
				const date = new Date(releaseTime);
				const year = date.getFullYear();
				const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始
				const day = String(date.getDate()).padStart(2, '0');
				return `${year}-${month}-${day}`; // 格式化为 YYYY-MM-DD
			},

			// 关闭弹窗
			closeModal() {
				this.showModal = false;
			},
			
			isUpTimePast() {
				this.getUserInformation();
				// 将 upTime 字符串转换为 Date 对象
				const upTimeDate = new Date(this.upTime);
				const currentDate = new Date(); // 当前时间
				return upTimeDate <= currentDate; // 返回比较结果
			},
		}
	};
</script>

<style>
	#app {
		width: 96%;
		margin: 0 auto;
		top: 10px;
	}

	.logo {
		display: flex;
		align-items: center;
	}

	.logo-image {
		width: 50px;
		height: auto;
	}

	.logo-text {
		margin-left: 30px;
		/* 使文字紧靠图片 */
	}


	/* 查询结果
	*************************
	******
	*/
	.firstRes {
		background-color: white;
		border-radius: 10px;
		box-shadow: 0 4px 10px rgba(0, 0, 0, 0.5);
		width: 110%;
		position: relative;
		right: 5%;
		margin-top: 5px;
		padding: 0 0 20px;
	}

	.cornerBox {
		position: absolute;
		top: 0;
		right: 0;
		font-size: 11px;
		max-width: 60px;
		max-height: 26px;
		/* background-color: #feece5;
		color: #f6542d;
		color: #ff5733; */
		background-color: #f6542d;
		color: white;
		border-bottom-left-radius: 10px;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.cornerBox>a {
		margin-right: 8px;
		margin-left: 8px;
		margin-top: 4px;
		margin-bottom: 4px;
	}

	.secondtRes {
		background-color: white;
		border-radius: 10px;
		box-shadow: 0 4px 10px rgba(0, 0, 0, 0.5);
	}

	.search-result {
		position: relative;
		top: -20px;
		width: 90%;
		margin: 0 auto;
	}

	.container {
		position: relative;
		padding-top: 20px;
		/* display: flex; */
		justify-content: center;
		align-items: center;
	}

	.canvas {
		/* 线段 */
		height: 1px;
		position: relative;
		/* left: 5%; */
		background-color: #ccc;
		position: relative;
		/* width: 90%; */
	}

	.collection {
		position: relative;
		left: 45vh;
		z-index: 99;
		margin-top: 30px;
	}

	.collection>image {
		position: relative;
		width: 20px;
		height: 20px;
	}

	.third-category-table {
		font-weight: bolder;
		font-size: 0.6rem;
		padding: 0px 0px;
		/* background-color: rgba(24, 186, 244, 0.2); */
		background-color: #25579e;
		color: black;
		border-radius: 0px;
		border: none;
	}

	.institution-header {
		display: flex;
		position: relative;
	}

	.institution-buttom {
		position: relative;
		margin-top: 28px;
	}

	.toggle-arrow {
		position: absolute;
		right: 10px;
		height: 22px;
		width: 66px;
		font-size: 12px;
		font-weight: bold;
		border-radius: 12px 12px 12px 12px;
		/* border: 1px solid #1787E0; */
		border: 1px solid #25579e;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.underline {
		text-decoration: underline;
	}

	/* 弹窗背景样式 */
	.modal {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.5);
		/* 半透明灰色背景 */
		display: flex;
		justify-content: center;
		align-items: center;
		z-index: 100;
	}

	/* 弹窗内容样式 */
	.modal-content {
		background-color: white;
		padding: 20px;
		border-radius: 10px;
		box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
		display: flex;
		justify-content: center;
		align-items: center;
		max-width: 90vw;
		/* 最大宽度限制为屏幕宽度的 90% */
		max-height: 90vh;
		/* 最大高度限制为屏幕高度的 90% */
	}

	/* 图片样式 */
	.modal-image {
		width: 300px;
		height: 409px;
	}

	/* 点击背景区域时关闭弹窗 */
	.modal[click.self] {
		cursor: pointer;
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

	.modal-content-pay {
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

	.dynamic {
		font-size: 14px;
		margin-left: 5px;
		font-weight: bold;
		background: linear-gradient(90deg, #f6542d, #ebeb41, #f6542d);
		background-size: 200% 100%;
		background-position: -100% 0;
		color: transparent;
		-webkit-background-clip: text;
		animation: colorWave 3s ease-in-out infinite;
		font-style: italic;
	}


	@keyframes colorWave {
		0% {
			color: #f6542d;
		}

		50% {
			color: #ebeb41;
			/* 可以调整为其他颜色 */
		}

		100% {
			color: #f6542d;
		}
	}

	/* .search-result */
</style>