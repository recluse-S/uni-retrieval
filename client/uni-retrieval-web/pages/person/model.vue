<template>
	<view class="container">
		<view> 用户 {{phone}}, 您的订单信息： </view>

		<view v-if="isUpTimePast()">
			<view style="display: flex; align-items: center;">
				<view> 您还不是会员 </view>
			</view>
		</view>
		<view v-else>
			<view> 硕博优才甄选会员截止日期到{{getDay(upTime)}} </view>
		</view>

	</view>
</template>

<script>
	import {
		getUserInfo
	} from "@/common/apis.js"

	import {
		getCache
	} from '../../utils/cache.js'

	export default {
		data() {
			return {
				showPay: false, // 控制弹窗的显示与隐藏
				orderId: '', // 用来保存订单号 *微信支付用
				paymentData: null, // 用来保存支付数据 *微信支付用
				upTime: '',
				usableTimes: '',
				showModal: false, // 控制弹窗的显示与隐藏
				phone: '',
			};
		},

		mounted() {
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
			isUpTimePast() {
				// 将 upTime 字符串转换为 Date 对象
				const upTimeDate = new Date(this.upTime);
				const currentDate = new Date(); // 当前时间
				return upTimeDate <= currentDate; // 返回比较结果
			},

			getUserInformation() {
				getUserInfo().then(response => {
						const data = response.data;
						this.upTime = data.upTime;
						this.phone = data.phone;
					})
					.catch(error => {
						console.error("出错", error);
					})
			},

			getDay(releaseTime) {
				const date = new Date(releaseTime);
				const year = date.getFullYear();
				const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始
				const day = String(date.getDate()).padStart(2, '0');
				return `${year}-${month}-${day}`; // 格式化为 YYYY-MM-DD
			},
		}
	};
</script>

<style scoped>
	.container {
		padding: 20px;
	}

	.pay-btn {
		background-color: #1aad19;
		color: white;
		font-size: 18px;
		padding: 10px 20px;
		border-radius: 5px;
		width: 200px;
		text-align: center;
	}
</style>