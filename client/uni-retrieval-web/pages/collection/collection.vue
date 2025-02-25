<template>
	<view class="box">
		<view style="position: relative; height: 20px; width: 120%; right: 10%; background-color: #25579e;">
			<view style="color: white; font-size: 13px;display: flex; justify-content: center; align-items: center;">
				我的收藏 - 山东高校考试公告
			</view>
		</view>

		<view v-if="Resul.length === 0" style="position: relative; top: 200px; color: #999; text-align: center;">
			暂未收藏，快去看看吧 ~ </view>

		<!-- <view class="home">
			<navigator @click="navigateBack()" style="position: fixed; top: 10px; right: 10px;">
				<image src="../../static/personal.png" style="width: 34px; height: 30px; margin-left: 6rpx;"></image>
				<view style="font-size: 10px; color: #1787E0;"> 个人中心 </view>
			</navigator>
		</view> -->

		<view class="container">
			<view class="canvas"></view>
			<view style="position: relative; top: -16px;" v-for="(item,index) in Resul">
				<view style="margin-top: 30px;">
					<view style="color: #1787E0; font-size: 18px; font-family: '楷体'; font-weight: bold;"
						@click="sendId(item.formId)">
						{{ item.institution.replace(/\s+/g, '') }}
					</view>
					
					<view style="display: flex; justify-content: space-between; align-items: center; margin-left: 10px;">
					    <!-- 左侧文本部分 -->
					    <view style="font-size: 14px; font-family: '黑体'; margin-top: 6px;">
					        <p>主管部门：{{ item.postName.replace(/\s+/g, '') }}</p>
					        <p>岗位类型：{{ item.postStatement }}</p>
					        <p>岗位等级：{{ item.reqDegree }}</p>
					    </view>
					
					    <!-- 右侧收藏部分 -->
					    <!-- <view @click.stop="postLike(post)" class="collection" style="display: flex; flex-direction: column; align-items: center;">
					        <img src="../../static/collection-true.png" alt="Collection status" />
					        <text style="font-size: 12px;">取消收藏</text> 
					    </view> -->
					</view>

				</view>
			</view>
		</view>

	</view>
</template>

<script>
	import {
		getOwn,
		unlike,
		postOwnLike
	} from "@/common/apis.js"
	export default {
		onShareAppMessage() {},
		onShareTimeline() {},
		
		data() {
			return {
				Resul: {
					// postName:'' ,
					// insDepartment:'',
					// postStatement:'',
					// reqDegree:''
				},
			}
		},
		
		// mounted() {
		// 	this.init();
		// },
		
		onShow() {
			// 每次页面展示时重新加载数据
			this.init();
		},
		
		methods: {
			init() {
				getOwn()
					.then(response => {
						console.log("结果", response.data);
						const data = response.data;

						this.Resul = response.data;
					})
					.catch(error => {
						console.error("出错", error);
					})
			},

			postLike(id) {
				uni.showModal({
					title: '确认取消收藏',
					content: '是否确认取消收藏？',
					confirmText: '确认',
					cancelText: '取消',
					success: (res) => {
						if (res.confirm) {
							this.postUnlike(id); // 用户点击确认执行取消收藏
						} else if (res.cancel) {
							// 用户点击取消，不执行任何操作
						}
					}
				});
			},
			
			postUnlike(id) {
				postOwnLike(id).then(resp => {
					if (resp.data.msg === "success") {
						console.log(resp.data);
						this.init(); // 取消收藏成功后，执行页面初始化或其他操作
					} else {
						console.log("取消收藏失败");
					}
				});
				this.init();
			},

			navigateBack() {
				uni.reLaunch({
					url: `/pages/person/center`
				});
			},

			//详情页
			sendId(id) {
				uni.navigateTo({
					url: `/pages/pageData/pageData?id=${id}`
				});
			},
		}
	}
</script>

<style>
	.box {
		position: relative;
		top: 3vh;
		width: 80%;
		margin: 0 auto;
	}

	.container {
		position: relative;
		top: 20px;
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
	}

	/* .collection {
		position: relative;
		left: 260px;
	} */

	.collection>image {
		position: relative;
		width: 20px;
		height: 20px;
	}
</style>