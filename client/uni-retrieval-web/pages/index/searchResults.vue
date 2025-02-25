<template>
	<view class="search-result" v-if="Resul.length > 0">
		<view class="container">
			<view style="position: relative; height: 20px; width: 120%; right: 10%; background-color: #25579e;">
				<!-- background: linear-gradient(to right, #1144ff, #29a2ff); -->
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
							{{item.institution}}
						</view>
					</view>
					<view style="color: #999; font-size: 13px; font-family: '楷体';">
						该单位共招 {{item.count}} 人
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
						<span style="color: #1787E0;">查看详情</span>
					</view>

					<view v-if="Inid === item.id" class="institution-buttom">
						<view class="canvas"></view>
						<view v-for="(post, postIndex) in item.posts" :key="postIndex" style="margin-top: 8px;">
							<view style="display: flex;" @click="sendId(post.id)">
								<view @click="toggleLike(post)" class="collection"> <!-- 使用 post.id  -->
									<img :src="post.liked ? '../../static/collection-true.png' : '../../static/collection-false.png'"
										alt="Collection status" />
								</view>
								<view style="font-size: 16px; font-family: '黑体'; margin-top: 8px;">
									<a style="color: #1787E0; display: inline-block; font-family: '楷体';"
										@click="sendId(post.id)"> <!-- 使用 post.id -->
										{{ post.postName }}
									</a>
									<view
										style="width: 36px; border-radius: 0 10px 10px 0; height: 18px; background-color: #eef0fd;
					                           color: #144dff; font-size: 10px; display: inline-flex; align-items: center; justify-content: center; margin-left: 4px;">
										{{ post.postGrade || '--' }}
									</view>
									</p>
									<a style="display: inline; font-size: 12px; margin-right: 3px; color: #999;">
										共招 </a>
									<span style="font-size: 12px; color: #32CD32; margin-right: 3px;">
										{{ post.recruitingNum || '--' }}</span>
									<a style="display: inline; font-size: 12px; margin-right: 50px; color: #999;">
										人 </a>
									<!-- <a style="display: inline; font-size: 13px;"> ️ </a> -->
									<image src="../../static/eye.png"
										style="height: 16px; width: 24px; position: relative; top: 5px; margin-right: 3px;">
									</image>
									<span style="font-size: 12px; color: #32CD32;">
										{{ post.view || '--' }}</span>
									</p>
									<a style="display: inline; font-size: 13px;">岗位类型：</a>
									<span style="font-size: 12px; color: #999; margin-right: 50px;">
										{{ post.postStatement || '--' }}</span>
									<a style="display: inline; font-size: 13px;">学历要求： </a>
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

</template>

<script>
	export default {
		data() {
			return {
				Resul: [],
				institutions: [],
			};
		},
		onLoad(options) {
			const eventChannel = this.getOpenerEventChannel();
			eventChannel.on('acceptDataFromOpenerPage', (data) => {
				this.Resul = data.data;
			});
		},
		watch: {
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
			processResults() {
				const instMap = new Map();
				this.Resul.forEach(item => {
					// 创建组合键，使用机构名和 releaseTime
					const key = `${item.institution}|${item.releaseTime}`;

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
			toggleInst(id) {
				this.Inid = this.Inid === id ? null : id;
			},
			//详情页
			sendId(id) {
				uni.navigateTo({
					url: `/pages/pageData/pageData?id=${id}`
				});
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
		}
	}
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
	}

	.personalitySearch.expanded {
		/* opacity: 1; */
		max-height: none;
	}

	.overlay {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		height: 100px;
		background: linear-gradient(to bottom, rgba(255, 255, 255, 0), rgba(255, 255, 255, 1));
		/* pointer-events: none; */
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
		font-size: 12px;
	}

	.label {
		margin-right: 8px;
		color: #666666;
		font-weight: bold;
	}

	.dropdown {
		/* display: flex; */
	}

	.dropdown span {
		white-space: nowrap;
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
		/* color: #1787E0; */
		color: #25579e;
	}

	.selected3 {
		/* color: #1787E0; */
		color: #25579e;
	}

	.selected4 {
		/* color: #1787E0; */
		color: #25579e;
	}

	.selected5 {
		/* color: #1787E0; */
		color: #25579e;
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
		background-color: #feece5;
		color: #ff5733;
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
		width: 30px;
		height: 30px;
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

	/* .search-result */
</style>