<template>
	<view id="app">
		<!-- <scroll-view :scroll-y="true" :style="{ height: '100vh'}" @scrolltolower="loadMore" :enable-back-to-top="true"> -->
		<scroll-view :scroll-y="true" :style="{ height: '100vh'}" @scrolltolower="loadMore" :enable-back-to-top="true">
			<view class="personalitySearch" :class="{ expanded: showSearch }">
				<!-- <div class="overlay" v-if="showOverlay"></div> -->
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
								<text
									class="price-value">¥{{ (paymentAmount / 100).toFixed(2).replace(/\.00$/, '') }}</text>
							</view>
							<view style="color: #999; font-size: 14px; margin-bottom: 20px;"> *温馨提示:
								本产品为虚拟内容服务，购买成功后不支持退款 </view>
							<button class="pay-btn" @click="onPay">立即开通</button>
						</view>
					</view>
				</view>

				<view class="search-3">
					<div class="input-dropdown">
						<image src="../../static/院校.png" style="width: 20px; height: 20px;"></image>
						<span class="input-label">院校 </span>
						<view class="input-container">
							<input v-model="keyword1" placeholder="请输入您想查询的院校(选填)"
								style="border: none; outline: none; background-color: transparent; padding: 5px;" />
						</view>
					</div>
				</view>
				<div class="divider"></div>

				<view class="search-3">
					<div class="input-dropdown" @click="onFocus">
						<image src="../../static/专业.png" style="width: 20px; height: 20px;"></image>
						<span class="input-label">专业 </span>
						<view class="input-container">
							<input v-model="keyword3" @input="onInput" type="text" placeholder="请输入您想查询的专业(选填)"
								style="border: none; outline: none; background-color: transparent; padding: 5px;" />
						</view>
					</div>
				</view>
				<div class="divider"></div>
				<!-- 弹出模块 -->
				<div v-if="showDropdown" class="overlay" @click="onOverlayClick"></div>
				<div v-if="showDropdown" class="dropdown-container">
					<div class="search-container" style="display: flex; align-items: center;">
						<image src="../../static/专业.png" style="width: 20px; height: 20px; margin-left: 6px;"></image>
						<input v-model="keyword3" @input="onInput" type="text" placeholder="请输入专业"
							class="search-input" />
					</div>
					<div class="dropdown-list">
						<ul>
							<li @click="onOverlayClick" style="color: #25579e;"> {{keyword3}} </li>
							<li v-for="major in filteredMajors" :key="major.major" @click="selectMajor(major)">
								{{ major.major }}
							</li>
						</ul>
					</div>
				</div>

				<view class="search-3">
					<div class="dropdown">
						<image src="../../static/地区.png" style="width: 20px; height: 20px;"></image>
						<span class="label">地区 </span>
						<view>
							<span @click="selectArea(null)" :class="{ selected2: keyword4 === null }">
								全部
							</span>
							<span v-for="area in areas" :key="area" @click="selectArea(area)" :class="{ 
							        selected2: keyword4 === area, 
							        'red-text': area === '省外', 
							        'blue-text': keyword4 === area && area === '省外' 
							      }">
								{{ area }}
							</span>

						</view>
					</div>
				</view>
				<div class="divider"></div>

				<view class="search-3">
					<div class="dropdown">
						<image src="../../static/年份.png" style="width: 20px; height: 20px;"></image>
						<span class="label">年份 </span>
						<view>
							<span @click="selectTime(null)" :class="{ selected5: keyword9 === null }">
								全部
							</span>
							<span v-for="area in time" :key="area" @click="selectTime(area)"
								:class="{ selected5: keyword9 === area }">
								{{ area }}
							</span>
						</view>
					</div>
				</view>
				<div class="divider"></div>

				<view class="search-3">
					<div class="dropdown">
						<image src="../../static/学历.png" style="width: 20px; height: 20px;"></image>
						<span class="label">学历 </span>
						<view>
							<span @click="selectGrade(null)" :class="{ selected3: keyword2 === null }">
								全部
							</span>
							<span v-for="area in grade" :key="area" @click="selectGrade(area)"
								:class="{ selected3: keyword2 === area }">
								{{ area }}
							</span>
						</view>
					</div>
				</view>
				<div class="divider"></div>

				<view class="search-3">
					<div class="dropdown">
						<image src="../../static/岗位.png" style="width: 20px; height: 20px;"></image>
						<span class="label">岗位 </span>
						<view>
							<span @click="selectPosts(null)" :class="{ selected6: keyword5 === null }">
								全部
							</span>
							<span v-for="area in posts" :key="area" @click="selectPosts(area)"
								:class="{ selected6: keyword5 === area }">
								{{ area }}
							</span>
						</view>
					</div>
				</view>
				<div class="divider"></div>

				<view class="search-3">
					<div class="dropdown">
						<image src="../../static/机构性质.png" style="width: 20px; height: 20px;"></image>
						<span class="label">性质 </span>
						<view>
							<span @click="selectUnits(null)" :class="{ selected9: keyword6 === null }">
								全部
							</span>
							<span v-for="area in units" :key="area" @click="selectUnits(area)"
								:class="{ selected9: keyword6 === area }">
								{{ area }}
							</span>
						</view>
					</div>
				</view>
				<div class="divider"></div>

				<view class="search-3">
					<div class="dropdown">
						<image src="../../static/学历层次.png" style="width: 20px; height: 20px;"></image>
						<span class="label">层次 </span>
						<view>
							<span @click="selectLevels(null)" :class="{ selected10: keyword8 === null }">
								全部
							</span>
							<span v-for="area in levels" :key="area" @click="selectLevels(area)"
								:class="{ selected10: keyword8 === area }">
								{{ area }}
							</span>
						</view>
					</div>
				</view>
				<div class="divider"></div>

				<view class="search-3">
					<div class="dropdown">
						<image src="../../static/等级.png" style="width: 20px; height: 20px;"></image>
						<span class="label">等级 </span>
						<view>
							<span @click="selectEstate(null)" :class="{ selected4: keyword7 === null }">
								全部
							</span>
							<span v-for="area in estates" :key="area" @click="selectEstate(area)"
								:class="{ selected4: keyword7 === area }">
								{{ area }}
							</span>
						</view>
					</div>
				</view>
				<div class="divider"></div>

				<!-- <div class="major-title"> 专业选择 </div> -->

				<!-- <view class="education">
					<template>
						<radio-group class="major-container">
							<view class="item" @click="selectMajor(null)"
								:style="{ backgroundColor: selectedMajor === null ? '#25579e' : 'white' }">
								<text :style="{ color: selectedMajor === null ? 'white' : 'black' }">全部</text>
							</view>
							<view class="item" @click="selectMajor(1)"
								:style="{ backgroundColor: selectedMajor === 1 ? '#25579e' : 'white' }">
								<text :style="{ color: selectedMajor === 1 ? 'white' : 'black' }">学术型硕士</text>
							</view>
							<view class="item" @click="selectMajor(2)"
								:style="{ backgroundColor: selectedMajor === 2 ? '#25579e' : 'white' }">
								<text :style="{ color: selectedMajor === 2 ? 'white' : 'black' }">专业学位硕士</text>
							</view>
							<view class="item" @click="selectMajor(3)"
								:style="{ backgroundColor: selectedMajor === 3 ? '#25579e' : 'white' }">
								<text :style="{ color: selectedMajor === 3 ? 'white' : 'black' }">学术型博士</text>
							</view>
							<view class="item" @click="selectMajor(4)"
								:style="{ backgroundColor: selectedMajor === 4 ? '#25579e' : 'white' }">
								<text :style="{ color: selectedMajor === 4 ? 'white' : 'black' }">专业学位博士</text>
							</view>
						</radio-group>
					</template>
				</view>

				<view class="major-list" v-if="showMajorList">
					<div class="main-list">

						<ul class="first-category">
							<li v-for="(item, index) in majorPro" :key="index" @click="toggleActive_max(index, item.id)"
								:class="{ 'active': activeIndex_max === index }" v-show="true">
								&nbsp; {{ item.major }}
								<text v-show="activeIndex_max === 0"> </text>
							</li>
						</ul>

						<ul class="second-category">
							<li @click="toggleActiveSecond()" :class="{ 'active': activeIndex === 'all' }">
								&nbsp; 全选
								<text v-show="activeIndex === 'all'"> </text>
							</li>
							<li v-for="(item, index) in majorSec" :key="index" @click="toggleActive(index, item.id)"
								:class="{ 'active': theSelectedIndices.includes(index) }">
								&nbsp; {{item.major}}
								<text v-show="activeIndex === index"> </text>
							</li>
						</ul>

						<div class="third-category">
							<table>
								<view class="table-row">
									<view class="table-cell-1" style="font-weight: 900"> 专业名称 </view>
									<view class="table-cell-2" style="color: #ccc;"> </view>
								</view>
								<view v-if="theShowRow" class="table-row">
									<view class="table-cell">全选</view>
									<view class="table-cell-2" @click="selectAll3"> 选择 </view>
								</view>
								<view v-if="showRow" class="table-row">
									<view class="table-cell">已选择该门类所有学科</view>
								</view>
								<view class="table-row" v-for="(item, index) in majorThi" :key="index"
									:class="{ 'selected': selectedIndices.includes(index) }"
									@click="toggle(index,item.major)">
									<view class="table-cell"> {{item.major}} </view>
									<view class="table-cell-2"
										:class="{ 'selected1': selectedIndices.includes(index) }"> 选择
									</view>
								</view>
							</table>
						</div>
					</div>
				</view> -->

				<button @click="searchex2" class="notarize"> 立 即 搜 索 </button>

				<view style="font-size: 12px; margin-top: 14px; color: #999; margin-left: 16px; margin-right: 16px;">
					* 重要提醒： 请在报考前仔细阅读对应岗位招录要求并与该院校官网确认，此微信小程序展示内容仅供参考。(此版本为测试版，系统正在不断升级维护中，使用过程中如发现问题请及时反馈，感谢您的配合)
				</view>
			</view>
			<!-- <view @click="toggleSearch" v-if="Resul.length > 0"
				style="display: flex; justify-content: center; margin-top: 10px; color: #1144ff; font-family: '楷体';">
				{{ showSearch ? '∧ 点击收起' : '∨ 点击展开个性搜索' }}
			</view> -->

			<!-- 搜索结果 -->
			<view class="search-result" v-if="Resul.length > 0">
				<view class="container">
					<view style="position: relative; height: 20px; width: 120%; right: 10%; background-color: #25579e;">
						<view
							style="color: white; font-size: 13px;display: flex; justify-content: center; align-items: center;">
							山东高校考试公告汇总
						</view>
					</view>

					<view v-for="(item, index) in institutions" :key="index" class="firstRes">
						<view class="cornerBox"> <a>{{item.location}}</a> </view>

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
							<view class="toggle-arrow" @click="toggleInst(item.id)">
								<span style="color: #25579e;">查看详情</span>
							</view>

							<view v-if="Inid === item.id" class="institution-buttom">
								<view class="canvas"></view>
								<view v-for="(post, postIndex) in item.posts" :key="postIndex" style="margin-top: 8px;">
									<view style="display: flex;" @click="sendId(post.id)">
										<!-- <view @click="toggleLike(post)" class="collection">
											<img :src="post.liked ? '../../static/collection-true.png' : '../../static/collection-false.png'"
												alt="Collection status" />
										</view> -->

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
											<a
												style="display: inline; font-size: 12px; margin-right: 3px; color: #999;">
												共招 </a>
											<span style="font-size: 12px; color: #32CD32; margin-right: 3px;">
												{{ post.recruitingNum || '--' }}</span>
											<a
												style="display: inline; font-size: 12px; margin-right: 50px; color: #999;">
												人 </a>
											<image src="../../static/eye.png"
												style="height: 16px; width: 24px; position: relative; top: 5px; margin-right: 3px;">
											</image>
											<span style="font-size: 12px; color: #32CD32;">
												{{ post.view || '--' }}</span>
											</p>
											<a style="display: inline; font-size: 12px; font-weight: bold;">岗位类别：</a>
											<span style="font-size: 12px; color: #999; margin-right: 50px;">
												{{ post.postCategory || '--' }}</span>
											<a style="display: inline; font-size: 12px;  font-weight: bold;">学历要求： </a>
											<span style="font-size: 12px; color: #999;">
												{{ post.reqDegree || '--' }}</span>
										</view>
									</view>
								</view>
							</view>
						</view>
					</view>

					<div v-if="isLoadingMore" class="loading-more"
						style="margin-top: 10px; position: relative; text-align: center;">
						正在加载... ...</div>
					<div v-if="showNoMore" class="no-more-data"
						style="margin-top: 10px; position: relative; text-align: center;">
						没有更多数据啦~</div>
				</view>
			</view>

			<!-- 底部留白 -->
			<view style="position: relative; height: 50px;"> </view>

		</scroll-view>
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
		getAllMajors,
		getFilteredMajors,
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
		onShareAppMessage() {},
		onShareTimeline() {},

		data() {
			return {
				pageNum: 1, // 当前页码
				pageSize: 20, // 每页显示的数据条数
				hasMore: true, // 标记是否还有更多数据

				keyword1: '', // 院校、学校
				keyword2: null, // 学历：硕士、博士
				keyword3: '', // 专业
				keyword4: null, // 地区
				keyword5: null, // 岗位
				keyword6: null, // 单位性质（公办、民办）
				keyword7: null, // 等级：初级、高级
				keyword8: null, // 学历层次
				keyword9: null, // 年份

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
				// selectedIndicesName: [],
				selectedIndex: 0, // 记录选中的索引
				selectedMajor: 1, // 用于存储选中的专业
				Resul: [],
				institutions: [],
				Inid: null,
				// 展示全部专业用
				allMajors: [],
				filteredMajors: [],
				showDropdown: false,

				activeIndex: -1, // 初始值为-1，表示没有选中任何项
				activeIndex_max: -1, // 初始值为-1，表示没有选中任何项

				showMajorList: true, // 控制 major-list 显示的状态
				showRow: false,
				theShowRow: true,
				showSearch: true,
				showOverlay: false,
				hasMoreData: true, // 判断是否还有更多数据
				isLoadingMore: false, // 新增：是否正在加载更多
				showNoMore: false, // 新增：是否显示“没有更多数据啦~”

				areas: ['济南', '青岛', '淄博', '枣庄', '东营', '烟台', '潍坊', '济宁', '泰安', '威海', '日照', '临沂', '德州', '聊城', '滨州', '菏泽',
					'省外'
				],
				posts: ['教师岗', '辅导员岗', '其他岗', '人才引进'],
				estates: ['初级', '中级', '高级'],
				time: ['2025', '2024', '2023', '2022', '考试动态'],
				grade: ['本科', '硕士', '博士'],
				units: ['公办', '民办'],
				levels: ['中职', '大专（含高职、技师）', '本科'],

				orderId: '', // 用来保存订单号 *微信支付用
				paymentData: null, // 用来保存支付数据 *微信支付用
				upTime: '',
				usableTimes: '',
				showModal: false, // 控制弹窗的显示与隐藏
				isSearch: false,
				paymentAmount: 0,
			};
		},

		// mounted() {
		// this.init();
		// this.getDATA(1, 1);
		// this.getDATA1(1);
		// this.activeIndex_max = 0;
		// this.theSelectedIndices = [0];

		// this.selectedIndices = [0];
		// this.selectedIndicesName = ["教育学"];
		// },

		// onShow() {
		// 	this.getUserInformation();
		// },

		mounted() {
			this.getAllMajorsData();
			// this.getUserInformation();
			this.getPayAmount();
		},

		onScroll(event) {
			if (this.isSearch) {
				const scrollTop = event.detail.scrollTop; // 当前滚动位置
				const scrollHeight = event.detail.scrollHeight; // 内容总高度
				const clientHeight = event.detail.clientHeight; // 可视区域高度
				// 计算距离底部的距离
				const distanceFromBottom = scrollHeight - (scrollTop + clientHeight);
				// 当距离底部小于 1000px 时，加载更多数据
				if (distanceFromBottom < 1000) {
					this.loadMore();
				}
			}
		},

		watch: {
			Resul: 'processResults',
			// firstId: {
			// 	handler(newVal, oldVal) {
			// 		if (newVal !== oldVal) {
			// 			this.getDATA(this.firstId, this.upId);
			// 			this.majorThi = {};
			// 			this.activeIndex = -1;
			// 		}
			// 	},
			// },
			// secondId: {
			// 	handler(newVal, oldVal) {
			// 		if (newVal !== oldVal) {
			// 			this.getDATA1(this.secondId);
			// 		}
			// 	},
			// },
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
					console.log(paymentData);
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
							console.log('===============================');
							if (res && res.code === 1) {
								resolve(res.data); // 返回支付所需的参数
								console.log(res.data.tradeNo);
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

			closeModal() { // 关闭弹窗
				this.showModal = false;
			},

			// ******************************************************
			async getPayAmount() {
				try {
					const response = await getPaymentAmount(); // 假设 getHots 直接返回 Promise
					const data = response.data;
					this.paymentAmount = data;
				} catch (error) {
					console.error("请求出错:", error); // 捕获错误
				}
			},

			// 输入框获取焦点时，显示弹出模块
			onFocus() {
				this.showDropdown = true;
			},

			// 输入框内容改变时，过滤专业并发送请求
			onInput() {
				this.filterMajors(); // 过滤专业列表
				this.fetchFilteredMajors(); // 根据输入动态请求
			},

			// 过滤专业列表
			filterMajors() {
				if (this.keyword3.trim() === '') {
					this.filteredMajors = this.allMajors; // 如果没有输入，显示所有专业
				} else {
					this.filteredMajors = this.allMajors.filter(major =>
						major.major.toLowerCase().includes(this.keyword3.toLowerCase())
					);
				}
			},

			// 点击专业时，更新输入框内容并关闭弹出模块
			selectMajor(major) {
				// 只有点击列表中的专业时，才会更新输入框的内容
				this.keyword3 = major.major; // 更新输入框的值为选中的专业
				this.showDropdown = false; // 关闭弹出模块
			},

			// 点击灰色区域时，关闭弹出模块
			onOverlayClick() {
				this.showDropdown = false;
			},

			// 获取所有专业数据
			getAllMajorsData() {
				getAllMajors().then(resp => {
					// 使用 map 提取专业名称（假设每个专业对象有 "major" 字段）
					const uniqueMajors = Array.from(
						new Set(resp.data.map(item => item.major)) // 去重专业名称
					).map(major => {
						// 通过去重后的专业名称重新构建对象（根据需要扩展）
						return resp.data.find(item => item.major === major);
					});

					this.allMajors = uniqueMajors; // 将去重后的专业数据赋值给 allMajors
					this.filterMajors(); // 过滤专业列表
				});
			},

			// 根据输入的内容动态请求专业列表
			fetchFilteredMajors() {
				if (this.keyword3.trim() === '') return;

				getFilteredMajors(this.keyword3).then(resp => {
					// 使用相同的去重方法处理过滤后的数据
					const uniqueFilteredMajors = Array.from(
						new Set(resp.data.map(item => item.major))
					).map(major => {
						return resp.data.find(item => item.major === major);
					});

					this.filteredMajors = uniqueFilteredMajors; // 更新过滤后的专业列表
				});
			},

			// 加载更多数据的方法
			loadMore() {
				if (this.hasMore && !this.isLoadingMore && this.isSearch) {
					this.isLoadingMore = true; // 标记正在加载更多
					this.pageSize += 50; // 增加页码
					this.searchex(); // 重新调用搜索方法
				}
			},

			handleScroll() {
				const scrollTop = window.scrollY || document.documentElement.scrollTop;
				const windowHeight = window.innerHeight;
				const documentHeight = document.documentElement.scrollHeight;

				// 检测是否滚动到底部
				if (scrollTop + windowHeight >= documentHeight - 50) { // 50 是一个预留的值，可以根据需要调整
					this.loadMore();
				}
			},

			selectArea(area) {
				this.keyword4 = area;
			},

			selectEstate(area) {
				this.keyword7 = area;
			},

			selectTime(area) {
				this.keyword9 = area;
			},

			selectGrade(area) {
				this.keyword2 = area;
			},

			selectPosts(area) {
				this.keyword5 = area;
			},

			selectUnits(area) {
				this.keyword6 = area;
			},

			selectLevels(area) {
				this.keyword8 = area;
			},

			// selectMajor(id) {
			// 	this.selectedMajor = id;
			// 	if (id !== null) {
			// 		this.sendIdToBackend(id);
			// 	} else {
			// 		this.allMajor();
			// 	}
			// 	if (this.majorPro && this.majorPro.length > 0) {
			// 		// this.activeIndex_max = 1;
			// 		this.toggleActive_max(0, 1);
			// 	}
			// },

			toggleInst(id) {
				this.Inid = this.Inid === id ? null : id;
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

			//-------------------------搜索----------------------
			// searchex2() {
			// 	this.Resul = [];
			// 	if (this.selectedMajor === 1 || this.selectedMajor === 2) {
			// 		this.keyword2 = "硕士";
			// 	} else if (this.selectedMajor === 3 || this.selectedMajor === 4) {
			// 		this.keyword2 = "博士";
			// 	}
			// 	this.keyword3 = this.selectedIndicesName.slice();
			// 	this.loading = true; 
			// 	getSearchResults(this.keyword2, this.keyword1, this.keyword3, this.keyword4, this.keyword5, this.keyword6,
			// 			this.keyword7, this.keyword8, this.keyword9, this.pageSize)
			// 		.then(response => {
			// 			const data = response.data;
			// 			if (Array.isArray(data) && data.length > 0) {
			// 				const uniqueData = data.filter(newItem => !this.Resul.some(existingItem => existingItem
			// 					.id === newItem.id));
			// 				this.Resul = this.Resul.concat(uniqueData); 
			// 			} else {
			// 				wx.showModal({
			// 					title: '提示',
			// 					content: '没有信息符合这些检索条件，请尝试调整关键词或条件。',
			// 					showCancel: false,
			// 					confirmText: '确定'
			// 				});
			// 			}
			// 		})
			// 		.catch(error => {
			// 			console.error("出错", error);
			// 		})
			// 		.finally(() => {
			// 			this.loading = false;
			// 			this.isLoadingMore = false;
			// 		});
			// },

			// searchex() {
			// 	if (this.selectedMajor === 1 || this.selectedMajor === 2) {
			// 		this.keyword2 = "硕士";
			// 	} else if (this.selectedMajor === 3 || this.selectedMajor === 4) {
			// 		this.keyword2 = "博士";
			// 	}
			// 	this.keyword3 = this.selectedIndicesName.slice();
			// 	this.loading = true; 
			// 	getSearchResults(this.keyword2, this.keyword1, this.keyword3, this.keyword4, this.keyword5, this.keyword6,
			// 			this.keyword7, this.keyword8, this.keyword9, this.pageSize)
			// 		.then(response => {
			// 			const data = response.data;
			// 			if (Array.isArray(data) && data.length > 0) {
			// 				const uniqueData = data.filter(newItem => !this.Resul.some(existingItem => existingItem
			// 					.id === newItem.id));
			// 				this.Resul = this.Resul.concat(uniqueData); 
			// 			} else {
			// 				this.hasMore = false; 
			// 				this.showNoMore = true; 
			// 			}
			// 		})
			// 		.catch(error => {
			// 			console.error("出错", error);
			// 		})
			// 		.finally(() => {
			// 			this.loading = false; 
			// 			this.isLoadingMore = false; 
			// 		});
			// },

			async searchex2() {
				try {
					this.isSearch = true;
					// 等待 getUserInformation 完成
					await this.getUserInformation();
					// 判断条件
					const currentTime = new Date();
					// 将 this.upTime 字符串转为 Date 对象
					const upTimeDate = new Date(this.upTime);
					// 如果条件不满足，则显示弹窗并终止
					if (upTimeDate <= currentTime && this.usableTimes <= 0) {
						this.showModal = true; // 显示弹窗
						return; // 跳出执行
					}
					// 更新可用次数
					this.usableTimes--;
					// 清空结果和加载状态
					this.Resul = [];
					this.loading = true;
					// 删除可用次数
					deleteUsableTimes();
					// 获取搜索结果
					const response = await getSearchResults(this.keyword2, this.keyword1, this.keyword3, this.keyword4,
						this.keyword5, this.keyword6, this.keyword7, this.keyword8, this.keyword9, this.pageSize);
					const data = response.data;
					if (Array.isArray(data) && data.length > 0) {
						// 去重操作
						const uniqueData = data.filter(newItem => !this.Resul.some(existingItem => existingItem.id ===
							newItem.id));
						this.Resul = this.Resul.concat(uniqueData);
					} else {
						wx.showModal({
							title: '提示',
							content: '没有信息符合这些检索条件，请尝试调整关键词或条件。',
							showCancel: false,
							confirmText: '确定'
						});
					}
				} catch (error) {
					// 异常处理
					console.error("获取用户信息失败", error);
				} finally {
					// 无论如何都要执行的操作
					this.loading = false;
					this.isLoadingMore = false;
				}
			},

			searchex() {
				this.loading = true;
				// this.keyword3 = this.selectedIndicesName.slice(); 
				getSearchResults(this.keyword2, this.keyword1, this.keyword3, this.keyword4, this.keyword5, this.keyword6,
						this.keyword7, this.keyword8, this.keyword9, this.pageSize)
					.then(response => {
						const data = response.data;
						if (Array.isArray(data) && data.length > 0) {
							const uniqueData = data.filter(newItem => !this.Resul.some(existingItem => existingItem
								.id === newItem.id));
							this.Resul = this.Resul.concat(uniqueData);
						} else {
							this.hasMore = false;
							this.showNoMore = true;
						}
					})
					.catch(error => {
						console.error("出错", error);
					})
					.finally(() => {
						this.loading = false;
						this.isLoadingMore = false;
					});
			},

			// 获取全部
			getAll() {
				getALL().then(response => {
						const data = response.data;
						this.Resul = data;
					})
					.catch(error => {
						console.error("出错", error);
					})
			},
			//第一级菜单
			init() {
				findFirstSub().then(resp => {
					this.majorPro = resp.data
				})
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
			//第一个
			toggleActive_max(index, id) {
				if (this.activeIndex_max === index) {
					this.selectedIndices = [];
					this.selectedIndicesName = [];
					this.activeIndex = -1;
				} else {
					this.firstId = id;
					getSecondSub(this.firstId, this.upId).then(resp => {
						this.majorSec = resp.data;
						// console.log('------------------' ,this.majorSec[0].id);	
						this.toggleActive(0, this.majorSec[0].id);
					})
					this.selectedIndices = [];
					this.selectedIndicesName = [];
					this.theSelectedIndices = [];
					this.activeIndex_max = index;
					// this.toggleActive(0, 1);
					this.majorThi = [];
					this.activeIndex = -1;
					this.Inid = null;
					// this.toggleActive(0,this.majorSec[0].id);
					// console.log('-----------------------------' ,this.majorSec[0].id);	
				}
			},
			//第二个
			toggleActiveSecond() {
				if (this.activeIndex === 'all') {

					this.theSelectedIndices = [];
					this.activeIndex = -1;
					this.showRow = false;
					this.theShowRow = true;
					this.selectedIndices = [];
					this.selectedIndicesName = [];
				} else {
					this.theSelectedIndices = this.majorSec.map((_, idx) => idx);
					this.activeIndex = 'all';
					this.showRow = true;
					this.theShowRow = false;
					this.majorThi = [];
					this.secondId = '';
					this.selectedIndices = [];
					this.selectedIndicesName = [];
					//叶想法   点击全部后直接获得第二类标签的每个边签下所有第三便签，走第三个一样的
					getAllSelect(this.firstId, this.upId).then(response => {
							this.selectedIndicesName = response.data;
						})
						.catch(error => {
							console.error("出错", error);
						})
				}
			},
			toggleActive(index, id) {
				if (this.activeIndex === index) {
					this.theSelectedIndices = [];
					// this.activeIndex = -1;
					this.selectedIndices = [];
					this.selectedIndicesName = [];
				} else {
					this.showRow = false;
					this.theShowRow = true;
					this.theSelectedIndices = [index];
					// this.activeIndex = index;
					// this.secondId = id;
					this.selectedIndices = [];
					this.selectedIndicesName = [];
					// console.log(JSON.stringify(this.theSelectedIndices))
				}
				this.getDATA1(id);
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
			//第二个获得数据
			getDATA(id, academic) {
				getSecondSub(id, academic).then(resp => {
					this.majorSec = resp.data
				})
			},
			//第三个获得数据
			getDATA1(id) {
				getThirdlySub(id).then(resp => {
					this.majorThi = resp.data
					// console.log(this.majorThi)
				})
			},
			//收藏
			toggleLike(post) {
				// 切换本地状态
				post.liked = !post.liked;

				// 调用postLike进行服务器请求
				this.postLike(post.id);
			},

			postLike(id) {
				console.log("进行到")
				postOwnLike(id).then(resp => {
					if (resp.msg === "success") {
						console.log(resp.data);
						// 弹窗提示 “点赞成功”
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
						// this.searchex();
					} else {
						console.log("收藏失败")
					}
				})
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
			}
		}
	};
</script>

<style>
	#app {
		width: 96%;
		margin: 0 auto;
		top: 10px;
	}

	/* 搜索框
	*************************
	******
	*/
	.personalitySearch {
		overflow: hidden;
		max-height: 100px;
		transition: max-height 0.3s ease;
		background: linear-gradient(to bottom, rgba(255, 255, 255, 1), rgba(255, 255, 255, 0));
		border: 1px solid #ccc;
		/* 设置边框 */
		box-shadow: 0 10px 12px rgba(0, 0, 0, 0.2);
		/* 设置阴影 */
		border-radius: 8px;
		/* 可选：设置圆角 */
		padding: 10px;
		/* 可选：设置内边距 */
	}

	.personalitySearch.expanded {
		/* opacity: 1; */
		max-height: none;
	}

	.overlay {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.5);
		z-index: 999;
	}

	.dropdown-container {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		height: 60%;
		background: #fff;
		padding: 10px;
		z-index: 1000;
		box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
	}

	/* .dropdown-list {
		max-height: 300px;
		overflow-y: auto;
	} */

	.dropdown-list ul {
		max-height: 500px;
		position: relative;
		height: 80%;
		overflow-y: auto;
	}

	.dropdown-list li {
		padding: 10px;
		cursor: pointer;
	}

	.search-container {
		position: relative;
		width: 100%;
		/* max-width: 400px; */
		height: 10%;
		border: 1px solid #ccc;
		padding: 10px;
		box-sizing: border-box;
		transition: border-color 0.3s ease;
		border-radius: 20px;
	}

	.search-input {
		position: relative;
		height: 100%;
		width: 100%;
		padding-left: 10px;
		margin-top: -2px;
		/* padding: 10px; */
		font-size: 14px;
		/* border: 1px solid #ccc; */
		box-sizing: border-box;
		transition: border-color 0.3s ease;
	}

	.search-input:focus {
		border-color: #4A90E2;
		/* 聚焦时改变边框颜色 */
	}

	.no-results {
		padding: 10px;
		color: #888;
	}

	.search {
		/* top: -100px; */
		position: relative;
		display: grid;
		grid-template-columns: 1fr auto;
		align-items: center;
		/* 垂直居中对齐 */
	}

	.search>input {
		height: 32px;
		/* border: 1px solid #1787E0; */
		border: 1px solid #25579e;
		font-size: 12px;
		padding-left: 10px;
	}

	.search>button {
		height: 34px;
		line-height: 34px;
		width: 80px;
		font-size: 16px;
		/* background-color: #1787E0; */
		background-color: #25579e;
		color: white;
		border: none;
		cursor: pointer;
		border-radius: 0;
		/* 按钮四个角为尖角无弧度 */
	}

	.search-2 {
		position: relative;
		top: 5px;
		width: 100%;
		padding-bottom: 3px;
		display: flex;
		align-items: stretch;
	}

	.search-2>button {
		height: 24.5px;
		width: 76px;
		/* background-color: #1787E0; */
		background-color: #25579e;
		color: white;
		border-radius: 0;
		font-size: 12px;
		line-height: 25px;
		/* 使文本垂直居中 */
		margin-right: 0;
		/* 不需要额外的边距 */
	}

	.search-3 {
		margin-top: 8px;
		display: flex;
		align-items: center;
		font-size: 13px;
	}

	.input-container {
		display: flex;
		align-items: center;

		position: relative;
	}

	.input-container>input {
		position: relative;
		width: 200px;
	}

	/* 分割线 */
	.divider {
		width: 100%;
		height: 0.4px;
		background-color: #ccc;
		margin: 10px 0;
	}

	.label {
		position: relative;
		top: -5px;
		margin-right: 4px;
		/* color: #666666; */
		font-weight: bold;
		/* font-size: 18px; */
		font-size: 16px;
	}

	.input-label {
		position: relative;
		top: 0px;
		margin-right: 10px;
		/* color: #666666; */
		font-weight: bold;
		font-size: 16px;
	}

	.input-dropdown {
		display: flex;
		align-items: center;
		position: relative;
	}

	.input-dropdown span {
		white-space: nowrap;
		margin-left: 14px;
	}

	.input-dropdown image {
		/* white-space: nowrap; */
		margin-left: 8px;
	}

	.dropdown span {
		white-space: nowrap;
		margin-left: 14px;
	}

	.dropdown image {
		/* white-space: nowrap; */
		margin-left: 8px;
	}

	/* 学历选择器 
	*************************
	******
	*/
	.major-container {
		display: flex;
		flex-wrap: wrap;
		/* 允许换行 */
		gap: 2px;
		/* 设置项之间的间距 */
		position: relative;
		top: 10px;
	}

	.major-list {
		position: relative;
		top: -20px;
	}

	.education {
		position: relative;
		font-size: 12px;
	}

	.education .item {
		/* margin-bottom: 10px; */
		padding: 6px;
	}

	.education .item text {
		color: white;
		text-decoration: underline;
		/* text-decoration-color: #1787E0; */
		text-decoration-color: #25579e;
		text-decoration-thickness: 2px;
		text-underline-offset: 6px;
		/* 下划线与文本的垂直距离 */
		color: inherit;
	}

	.selected2 {
		color: #144dff;
		/* color: #25579e; */
	}

	.selected3 {
		color: #144dff;
		/* color: #25579e; */
	}

	.selected4 {
		color: #144dff;
		/* color: #25579e; */
	}

	.selected5 {
		color: #144dff;
		/* color: #25579e; */
	}

	.selected6 {
		color: #144dff;
		/* color: #25579e; */
	}

	.selected7 {
		color: #144dff;
		/* color: #25579e; */
	}

	.selected8 {
		color: #144dff;
		/* color: #25579e; */
	}

	.selected9 {
		color: #144dff;
		/* color: #25579e; */
	}

	.selected10 {
		color: #144dff;
		/* color: #25579e; */
	}

	.custom-radio {
		width: 12px;
		height: 12px;
	}

	.notarize {
		height: 34px;
		line-height: 34px;
		width: 150px;
		font-size: 16px;
		/* background-color: #1787E0; */
		background-color: #25579e;
		color: white;
		border: none;
		cursor: pointer;
		border-radius: 0;
		margin-top: 20px;
	}

	/* 专业选择器
	*************************
	******
	*/
	.major-list {
		display: flex;
		flex-direction: row;
		position: relative;
		top: 10px;
		font-size: 12px;
	}

	.main-list>ul {
		list-style-type: none;
	}

	.main-list {
		display: flex;
		flex-direction: row;
		flex-wrap: nowrap;
		width: 100%;
	}

	.first-category {
		position: relative;
		width: 20%;
		/* background-color: rgba(24, 186, 244, 0.2); */
		/* background-color: #25579e; */
		margin-right: 2px;
		/* padding-left: 10px; */
		padding-top: 10px;
		height: 360px;
		overflow-y: auto;
	}

	.first-category>li {
		padding: 4px;
	}

	.first-category .active {
		/* background-color: rgba(24, 186, 244, 0.5); */
		background-color: #25579e;
		color: white;
		font-weight: bold;
	}

	.second-category {
		position: relative;
		width: 30%;
		/* background-color: rgba(24, 186, 244, 0.2); */
		/* background-color: #25579e; */
		margin-right: 2px;
		/* padding-left: 10px; */
		padding-top: 10px;
		height: 360px;
		overflow-y: auto;
	}

	.second-category>li {
		padding: 4px;
	}

	.third-category {
		position: relative;
		width: 50%;
		height: 360px;
		overflow-y: auto;
	}

	.third-category>td {
		white-space: nowrap;
	}

	table {
		width: 100%;
		border-collapse: collapse;
	}

	.table-row {
		display: flex;
		width: 100%;
		border-bottom: 1px solid #ccc;
	}

	.table-cell {
		text-align: center;
		padding: 4px;
		position: relative;
		width: 80%;
		/* border-right: 1px solid #ccc; */
	}

	.table-cell-1 {
		text-align: center;
		padding: 4px;
		position: relative;
		width: 80%;
		margin-top: 10px;
	}

	.table-cell-2 {
		text-align: center;
		padding: 4px;
		position: relative;
		width: 20%;
		/* border-right: 1px solid #ccc; */
	}

	.selected1 {
		color: blue;
		font-weight: bolder;
	}

	.first-category>li.active {
		background-color: white;
		/* color: #17abe3; */
		color: #25579e;
	}

	.first-category>li.active::after {
		content: " > ";
		/* 箭头符号 */
		position: absolute;
		right: 10px;
		/* 调整箭头位置 */
	}

	.second-category>li.active {
		background-color: white;
		/* color: #17abe3; */
		color: #25579e;
	}

	.second-category>li.active::after {
		content: " > ";
		/* 箭头符号 */
		position: absolute;
		right: 10px;
		/* 调整箭头位置 */
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
		color: #f6542d; */
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
		top: 10px;
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

	.red-text {
		color: red;
	}

	.blue-text {
		color: #144dff;
	}
</style>