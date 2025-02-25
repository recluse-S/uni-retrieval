<template>
	<view class="container">
		<!-- <view class="home" @click="postLike(id)">
			<button style="position: fixed; top: 15px; right: 0px; border: none; background: none; background-color: white; 
			               height: 60px; width: 40px; border-radius: 10px 0 0 10px; border: 2px solid gray;
			               display: flex; flex-direction: column; align-items: center; justify-content: center;">
				<img :src="liked ? '../../static/collection-true.png' : '../../static/collection-false.png'"
					alt="Collection status"
					style="position: relative; width: 36px; height: 34px; margin-bottom: 3px;" />
				<view style="margin-top: -2px; white-space: nowrap; font-size: 10px;"> 收 藏 </view>
			</button>
		</view> -->

		<view class="title"> {{ pageTitle.replace(/\s+/g, '') }} </view>
		<view style="font-size: 14px; color: #1787E0; margin-bottom: 30px;"> {{ postName.replace(/\s+/g, '') }} </view>

		<view style="display: flex; align-items: center;">
			<view class="title-2">基本信息</view>
			<view @click="postLike(id)" style="display: flex; align-items: center; position: relative; left: 48%;">
				<!-- <view style="width: 2px; height: 18px; background-color: #999; margin-right: 14px; margin-top: -3px;">
				</view> -->
				<img :src="liked ? '../../static/collection-true.png' : '../../static/collection-false.png'"
					alt="Collection status"
					style="position: relative; width: 26px; height: 26px; margin-bottom: 3px;" />
				<!-- 根据liked状态改变文字和颜色 -->
				<view
					:style="{ color: liked ? '#000' : '#999', marginTop: '-4px', whiteSpace: 'nowrap', fontSize: '12px', marginLeft: '2px' }">
					{{ liked ? '取消收藏' : '点击收藏' }}
				</view>
			</view>
		</view>

		<view class="table">
			<view class="row" v-if="postName !== null && postName !=='' ">
				<view class="cell left">岗位名称</view>
				<view class="cell right"> {{ postName.replace(/\s+/g, '') }} </view>
			</view>
			<view class="row" v-if="postNature !== null && postNature !=='' ">
				<view class="cell left">岗位性质</view>
				<view class="cell right">{{ postNature }}</view>
			</view>
			<view class="row" v-if="postGrade !== null && postGrade !=='' ">
				<view class="cell left">岗位等级</view>
				<view class="cell right">{{postGrade}}</view>
			</view>
			<view class="row" v-if="postStatement !== null && postStatement !=='' ">
				<view class="cell left">岗位类型</view>
				<view class="cell right">{{postStatement}}</view>
			</view>
			<view class="row" v-if="postDescription !== null && postDescription !=='' ">
				<view class="cell left">岗位描述</view>
				<view class="cell right">{{postDescription}}</view>
			</view>
			<view class="row" v-if="postCategory !== null && postCategory !=='' ">
				<view class="cell left">岗位类别</view>
				<view class="cell right">{{postCategory}}</view>
			</view>
			<view class="row" v-if="postCode !== null && postCode !=='' ">
				<view class="cell left">岗位代码</view>
				<view class="cell right">{{postCode}}</view>
			</view>
			<view class="row" v-if="postNumber !== null && postNumber !=='' ">
				<view class="cell left">岗位编号</view>
				<view class="cell right">{{postNumber}}</view>
			</view>
			<view class="row" v-if="postClassification !== null && postClassification !=='' ">
				<view class="cell left">岗位分类</view>
				<view class="cell right">{{postClassification}}</view>
			</view>
			<view class="row" v-if="fundsType !== null && fundsType !=='' ">
				<view class="cell left">经费形式</view>
				<view class="cell right">{{fundsType}}</view>
			</view>
			<view class="row" v-if="insType !== null && insType !=='' ">
				<view class="cell left">单位类型</view>
				<view class="cell right">{{insType}}</view>
			</view>
			<view class="row" v-if="insDepartment !== null && insDepartment !=='' ">
				<view class="cell left">主管部门</view>
				<view class="cell right">{{insDepartment}}</view>
			</view>
			<view class="row" v-if="goverType !== null && goverType !=='' ">
				<view class="cell left">编制性质</view>
				<view class="cell right">{{goverType}}</view>
			</view>
			<view class="row" v-if="recruitingNum !== null && recruitingNum !=='' ">
				<view class="cell left">招聘人数</view>
				<view class="cell right">{{recruitingNum}}</view>
			</view>
			<view class="row" v-if="reqEducation !== null && reqEducation !=='' ">
				<view class="cell left">学历要求</view>
				<view class="cell right">{{reqEducation}}</view>
			</view>
			<view class="row" v-if="reqDegree !== null && reqDegree !=='' ">
				<view class="cell left">学位要求</view>
				<view class="cell right">{{reqDegree}}</view>
			</view>
			<view class="row" v-if="reqMajorGraduate !== null && reqMajorGraduate !=='' ">
				<view class="cell left">研究生专业要求</view>
				<view class="cell right">{{reqMajorGraduate}}</view>
			</view>
			<view class="row" v-if="reqMajorUndergraduate !== null && reqMajorUndergraduate !=='' ">
				<view class="cell left">本科专业要求</view>
				<view class="cell right">{{reqMajorUndergraduate}}</view>
			</view>
			<view class="row" v-if="reqMajorJunior !== null && reqMajorJunior !=='' ">
				<view class="cell left">大学专科专业要求</view>
				<view class="cell right">{{reqMajorJunior}}</view>
			</view>
			<view class="row" v-if="reqDirection !== null && reqDirection !=='' ">
				<view class="cell left">研究方向</view>
				<view class="cell right">{{reqDirection}}</view>
			</view>
			<view class="row" v-if="reqElse !== null && reqElse !=='' ">
				<view class="cell left">其他条件</view>
				<view class="cell right">{{reqElse}}</view>
			</view>
			<view class="row" v-if="examForm !== null && examForm !=='' ">
				<view class="cell left">考试形式</view>
				<view class="cell right">{{examForm}}</view>
			</view>
			<view class="row" v-if="examRatio !== null && examRatio !=='' ">
				<view class="cell left">开考比例</view>
				<view class="cell right">{{examRatio}}</view>
			</view>
			<view class="row" v-if="examTypeRatio !== null && examTypeRatio !=='' ">
				<view class="cell left">笔试和面试成绩比例</view>
				<view class="cell right">{{examTypeRatio}}</view>
			</view>
			<view class="row" v-if="examInterview !== null && examInterview !=='' ">
				<view class="cell left">面试形式</view>
				<view class="cell right">{{examInterview}}</view>
			</view>
			<view class="row" v-if="examWritten !== null && examWritten !=='' ">
				<view class="cell left">笔试试卷类型</view>
				<view class="cell right">{{examWritten}}</view>
			</view>
			<view class="row" v-if="serviceLength !== null && serviceLength !=='' ">
				<view class="cell left">服务年限</view>
				<view class="cell right">{{serviceLength}}</view>
			</view>
			<view class="row" v-if="contactInfo !== null && contactInfo !=='' ">
				<view class="cell left">联系方式</view>
				<view class="cell right">{{contactInfo}}</view>
			</view>
			<view class="row" v-if="contactPhone !== null && contactPhone !=='' ">
				<view class="cell left">咨询电话</view>
				<view class="cell right">{{contactPhone}}</view>
			</view>
			<view class="row" v-if="contactEmail !== null && contactEmail !=='' ">
				<view class="cell left">电子邮箱</view>
				<view class="cell right">{{contactEmail}}</view>
			</view>
			<view class="row" v-if="contactPerson !== null && contactPerson !=='' ">
				<view class="cell left">联系人</view>
				<view class="cell right">{{contactPerson}}</view>
			</view>
			<view class="row" v-if="workplace !== null && workplace !=='' ">
				<view class="cell left">工作地点</view>
				<view class="cell right">{{workplace}}</view>
			</view>
			<view class="row" v-if="reqAge !== null && reqAge !=='' ">
				<view class="cell left">年龄要求</view>
				<view class="cell right">{{reqAge}}</view>
			</view>
			<view class="row" v-if="reqForeign !== null && reqForeign !=='' ">
				<view class="cell left">外语水平</view>
				<view class="cell right">{{reqForeign}}</view>
			</view>
			<view class="row" v-if="workTask !== null && workTask !=='' ">
				<view class="cell left">岗位职责</view>
				<view class="cell right">{{workTask}}</view>
			</view>
			<view class="row" v-if="remark !== null && remark !=='' ">
				<view class="cell left">备注</view>
				<view class="cell right">{{remark}}</view>
			</view>
			<view class="row" v-if="releaseTime !== null && releaseTime !=='' ">
				<view class="cell left">招考年份</view>
				<view class="cell right"> {{getDay(releaseTime)}} </view>
			</view>
			<view class="row" v-if="location !== null && location !=='' ">
				<view class="cell left">招考地区</view>
				<view class="cell right">{{location}}</view>
			</view>
		</view>

		<view v-if="text !== '' && text !== null ">
			<view class="title-2" style="position: relative; top: 15px;"> 招聘简章 </view>
			<view class="table" style="position: relative; top: 15px;">
				<view>
					<view class="content" v-html="formattedText" ref="content"></view>
				</view>

				<!-- <image :src="lastImageUrl" @click="previewImage"
					style="height: 200px; width: 200px; margin-left: 30px;">
				</image> -->

				<!-- 绑定图片源 -->
				<image :src="lastImageUrl" @click="previewImage" @touchstart="touchStart" @touchend="touchEnd"
					style="height: 150px; width: 150px; margin-left: auto; margin-right: auto; display: block;">
				</image>
				<p>{{ result }}</p>

				<!-- <button @click="changeUrl">点击跳转</button> -->

				<!-- web-view 页面 -->
				<!-- <web-view :src="url"></web-view> -->

				<view class='wrapper' v-if="link != '' && link != null">
					<button class='wepay' @click="goBaidu">
						<view style="font-size: 16px;"> 点击复制简章官网 </view>
						<view style="font-size: 12px; margin-top: -12px;"> （您可以选择在浏览器中打开该链接） </view>
					</button>
				</view>

			</view>
		</view>

		<!-- <image :show-menu-by-longpress="true" src="../../static/好友码.jpg" @click="previewImage2"></image> -->

		<!-- 底部留白 -->
		<view style="position: relative; height: 50px;"> </view>

	</view>

</template>

<script>
	import {
		getTheDetail,
		getTheDetailExcel,
		postOwnLike,
	} from "@/common/apis.js"
	import {
		jsQR
	} from 'jsqr';

	export default {
		onShareAppMessage() {},
		onShareTimeline() {},

		onLoad(options) {
			// 处理 id 参数
			if (options && options.id) {
				this.id = options.id;
			} else {
				console.error('未找到有效的 id 参数');
			}
		},

		data() {
			return {
				id: '',
				pageTitle: '',

				excelData: [],

				//数据
				contactEmail: '',
				contactInfo: '',
				contactPerson: '',
				contactPhone: '',
				createdTime: '',
				examForm: '',
				examInterview: '',
				examRatio: '',
				examTypeRatio: '',
				examWritten: '',
				fundsType: '',
				goverType: '',
				id: '',
				insDepartment: '',
				insType: '',
				institution: '',
				postCategory: '',
				postClassification: '',
				postCode: '',
				postDescription: '',
				postGrade: '',
				postName: '',
				postNature: '',
				postNumber: '',
				postStatement: '',
				recruitingNum: '',
				releaseTime: '',
				remark: '',
				reqAge: '',
				reqDegree: '',
				reqDirection: '',
				reqEducation: '',
				reqElse: '',
				reqForeign: '',
				reqMajorGraduate: '',
				reqMajorJunior: '',
				reqMajorUndergraduate: '',
				serviceLength: '',
				workTask: '',
				workplace: '',
				location: '',
				genRegulation: '',
				text: '',
				liked: '',
				link: '',

				lastImageUrl: '',
				imageUrls: [],
				result: '',
			}
		},

		mounted() {
			this.init(this.id);
			// this.decodeQRCode();
		},

		// computed: {
		// 	formattedText() {
		// 		// 替换图片的 src，同时添加 class 和 data-src 属性
		// 		return this.text.replace(/<img src="([^"]+)"[^>]*>/g,
		// 			'<img class="qrcode-img" data-src="$1" src="$1" style="width: 200px; height: auto; cursor: pointer;">'
		// 		);
		// 	}
		// },

		computed: {
			formattedText() {
				const regex = /<img\s+src="([^"]+)"[^>]*>/g; // 匹配 img 标签的正则表达式
				let match;
				let lastSrc = '';
				// 获取所有图片的 src，并且获取最后一张图片的 src
				while ((match = regex.exec(this.text)) !== null) {
					lastSrc = match[1];
				}
				// 更新 lastImageUrl 为最后一张图片的 URL
				this.lastImageUrl = lastSrc;
				// 如果找到了最后一张图片的 src，则删除该图片标签
				if (lastSrc) {
					const imgTagRegex = new RegExp(`<img\\s+src="${lastSrc}"[^>]*>`, 'g'); // 生成匹配具体 src 的正则
					this.text = this.text.replace(imgTagRegex, ''); // 删除最后一张图片标签
				}
				return this.text; // 返回处理后的文本
			},
			lastImageUrlWithTimestamp() {
				return this.lastImageUrl ? `${this.lastImageUrl}?t=${Date.now()}` : '';
			}
		},

		// computed: {
		// 	formattedText() {
		// 		// 使用正则提取最后一张图片的 src
		// 		const regex = /<img\s+src="([^"]+)"[^>]*>/g;
		// 		let match;
		// 		let lastSrc = '';

		// 		// 找到最后一张图片
		// 		while ((match = regex.exec(this.text)) !== null) {
		// 			lastSrc = match[1];
		// 		}

		// 		// 更新 lastImageUrl
		// 		this.lastImageUrl = lastSrc;

		// 		// 从原始文本中删除最后一张图片
		// 		if (lastSrc) {
		// 			const escapedSrc = thisescapeRegExp(lastSrc); // 对 src 进行转义
		// 			this.text = this.text.replace(new RegExp(`<img\\s+src="${escapedSrc}"[^>]*>`, 'g'), '');
		// 		}

		// 		// 返回更新后的文本
		// 		return this.text;
		// 	},
		// },

		methods: {
			// 点击按钮后改变url
			changeUrl() {
				this.url = 'http://jshrss.jiangsu.gov.cn/art/2024/11/12/art_78506_11416741.html'; // 可以根据需要动态设置跳转的 URL
			},

			goBaidu() {
				var link = this.link;
				//   const url = `/pages/pageData/officialWeb?link=${link}`;
				//   wx.navigateTo({
				//       url: url
				// // url: this.link
				//   });

				wx.setClipboardData({
					data: link,
					success(res) {
						wx.showTabBar({
							title: '链接已复制，您可以去浏览器中打开访问'
						})
					}
				});
			},

			touchStart(e) {
				this.touchStartTime = Date.now(); // 记录按下时间
			},

			touchEnd(e) {
				const touchEndTime = Date.now(); // 记录抬起时间
				const touchDuration = touchEndTime - this.touchStartTime; // 计算触摸时长

				if (touchDuration > 500) { // 如果按下时长超过500ms，则认为是长按
					this.autoRecognizeQRCode(); // 自动识别二维码
				}
			},

			// 获取图片信息
			getImageInfo(url) {
				return new Promise((resolve, reject) => {
					wx.getImageInfo({
						src: url,
						success: (res) => {
							resolve(res);
						},
						fail: (err) => {
							reject(err);
						}
					});
				});
			},

			// 从图片路径识别二维码
			scanQRCodeFromImage(imagePath) {
				wx.scanCode({
					scanType: ['qrCode'], // 限定扫描二维码类型
					success: (res) => {
						console.log('扫描成功:', res);
						if (res.result) {
							wx.showToast({
								title: `二维码内容: ${res.result}`,
								icon: 'none'
							});
						} else {
							wx.showToast({
								title: '未识别到二维码',
								icon: 'none'
							});
						}
					},
					fail: (err) => {
						console.error('二维码识别失败:', err);
						wx.showToast({
							title: '二维码识别失败',
							icon: 'none'
						});
					}
				});
			},

			previewImage() {
				console.log("预览图片: ", this.lastImageUrl);

				if (!this.lastImageUrl) {
					console.warn('No image URL provided.');
					return;
				}

				const urls = [this.lastImageUrl];

				uni.previewImage({
					urls: urls,
					current: this.lastImageUrl,
					indicator: 'default',
					loop: false,
					success: res => {
						console.log('Preview success:', res);
					},
					fail: err => {
						console.log('Preview failed:', err);
					}
				});
			},


			init(id) {
				getTheDetail(id)
					.then(response => {
						const data = response.data;
						// console.log(data)
						this.pageTitle = data.institution;
						this.contactEmail = data.contactEmail;
						this.contactInfo = data.contactInfo;
						this.contactPerson = data.contactPerson;
						this.contactPhone = data.contactPhone;
						this.createdTime = data.createdTime;
						this.examForm = data.examForm;
						this.examInterview = data.examInterview;
						this.examRatio = data.examRatio;
						this.examTypeRatio = data.examTypeRatio;
						this.examWritten = data.examWritten;
						this.fundsType = data.fundsType;
						this.goverType = data.goverType;
						this.insDepartment = data.insDepartment;
						this.insType = data.insType;
						this.institution = data.institution;
						this.postCategory = data.postCategory;
						this.postClassification = data.postClassification;
						this.postCode = data.postCode;
						this.postDescription = data.postDescription;
						this.postGrade = data.postGrade;
						this.postName = data.postName;
						this.postNature = data.postNature;
						this.postNumber = data.postNumber;
						this.postStatement = data.postStatement;
						this.recruitingNum = data.recruitingNum;
						this.releaseTime = data.releaseTime;
						this.remark = data.remark;
						this.reqAge = data.reqAge;
						this.reqDegree = data.reqDegree;
						this.reqDirection = data.reqDirection;
						this.reqEducation = data.reqEducation;
						this.reqElse = data.reqElse;
						this.reqForeign = data.reqForeign;
						this.reqMajorGraduate = data.reqMajorGraduate;
						this.reqMajorJunior = data.reqMajorJunior;
						this.reqMajorUndergraduate = data.reqMajorUndergraduate;
						this.serviceLength = data.serviceLength;
						this.workTask = data.workTask;
						this.workplace = data.workplace;
						this.text = data.text;
						this.genRegulation = data.genRegulation;
						this.liked = data.liked;
						this.link = data.link;
						console.log(this.text)
					})
					.catch(error => {
						console.error("出错", error);
					})
			},

			postLike(id) {
				postOwnLike(id).then(resp => {
					if (resp.data.msg === "success") {
						console.log(resp.data.data);
						// 根据服务器响应调整提示
						if (resp.data.data === "新增成功") {
							uni.showToast({
								title: '收藏成功',
								icon: 'success', // 显示成功图标
								duration: 2000 // 持续时间，单位为毫秒
							});
						} else if (resp.data.data === "刪除成功") {
							uni.showToast({
								title: '取消收藏',
								icon: 'success', // 显示成功图标
								duration: 2000 // 持续时间，单位为毫秒
							});
						}
						// this.searchex(); // 如果需要在这里更新数据，可以取消注释
					} else {
						console.log("收藏失败");
					}
				}).catch(error => {
					console.log("请求失败", error);
				});
				this.init(this.id);
			},

			postUnlike(id) {
				unlike(id).then(resp => {
					if (resp.data.msg === "success") {
						// console.log(resp.data.data);
						this.init(); // 取消收藏成功后，执行页面初始化或其他操作
					} else {
						console.log("取消收藏失败");
					}
				});
			},

			navigateBack() {
				wx.navigateBack({
					delta: 1
				});
				// uni.reLaunch({
				// 	url: `/pages/home/main`
				// });
			},

			formatDate(date) {
				const d = new Date(date);
				const options = {
					year: 'numeric',
					month: '2-digit',
					day: '2-digit'
				};
				return d.toLocaleDateString('zh-CN', options);
			},

			getDay(releaseTime) {
				const date = new Date(releaseTime);
				const year = date.getFullYear();
				const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始
				const day = String(date.getDate()).padStart(2, '0');
				return `${year}-${month}-${day}`; // 格式化为 YYYY-MM-DD
			},

		},
	}
</script>

<style>
	.container {
		padding: 20rpx;
	}

	.home {
		position: absolute;
		width: 10%;
		height: 10%;
		z-index: 99;
	}

	.title {
		font-size: 3vh;
		font-weight: bold;
		margin-bottom: 20rpx;
		/* text-align: center; */
		font-family: '楷体';
		color: #1787E0;
	}

	.title-2 {
		color: #000;
		font-size: 18px;
		margin-bottom: 20rpx;
		text-align: left;
		font-weight: bold;
	}

	.table {
		border: 1px solid #ccc;
		border-bottom: none;
		border-collapse: collapse;
		width: 100%;
		font-size: 13px;
	}

	.row {
		display: flex;
		border-bottom: 1px solid #ccc;
	}

	.cell {
		padding: 8px;
	}

	.left {
		flex: 0 0 20%;
		border-right: 1px solid #ccc;
		word-break: break-word;
		background-color: #eaeaea;
	}

	.right {
		flex: 1;
		padding-left: 8px;
	}

	.row:last-child .cell {
		border-bottom: none;
	}

	.row:last-child .left {
		border-right: none;
	}

	.theRow {
		display: flex;
		flex-direction: column;
		border-bottom: 1px solid #ccc;
	}

	.content {
		padding: 10px;
		line-height: 1.8;
		text-indent: 2em;
		margin: 0;
	}

	.wrapper {
		top: 50px;
		width: 100%;
		height: 100%;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.wepay {
		width: 80%;
		/* height: 30px; */
		padding: 2px;
		font-weight: bold;
		color: #25579e;
		background-color: #99ccff;
		border: 2px solid #25579e;
		border-radius: 5px;
		cursor: pointer;
		text-align: center;
	}

	.wepay:hover {
		background-color: #99ccff;
	}
</style>