<template>
	<view>

		<view class="education">
			<template>
				<radio-group class="major-container">
					<!-- <view class="item" @click="selectMajor(null)"
						:style="{ backgroundColor: selectedMajor === null ? '#25579e' : 'white' }">
						<text :style="{ color: selectedMajor === null ? 'white' : 'black' }">全部</text>
					</view> -->
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
					<!-- <li @click="toggleActiveSecond()" :class="{ 'active': activeIndex === 'all' }">
						&nbsp; 全选
						<text v-show="activeIndex === 'all'"> </text>
					</li> -->
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
						<!-- <view v-if="theShowRow" class="table-row">
							<view class="table-cell">全选</view>
							<view class="table-cell-2" @click="selectAll3"> 选择 </view>
						</view> -->
						<!-- <view v-if="showRow" class="table-row">
							<view class="table-cell">已选择该门类所有学科</view>
						</view> -->
						<view class="table-row" v-for="(item, index) in majorThi" :key="index"
							:class="{ 'selected': selectedIndices.includes(index) }" @click="toggle(index,item.major)">
							<view class="table-cell"> {{item.major}} </view>
							<view class="table-cell-2" :class="{ 'selected1': selectedIndices.includes(index) }">
							</view>
						</view>
					</table>
				</div>
			</div>
		</view>
	</view>
</template>

<script>
	import {
		findFirstSub,
		getSecondSub,
		getThirdlySub,
		getAllSelect,
		getAllMajors,
		getFilteredMajors,
	} from "@/common/apis.js"

	import {
		getCache
	} from '../../utils/cache.js'

	export default {
		onShareAppMessage() {},
		onShareTimeline() {},

		data() {
			return {
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
			};
		},

		mounted() {
			this.init();
			this.getDATA(1, 1);
			this.getDATA1(1);
			this.activeIndex_max = 0;
			this.theSelectedIndices = [0];

			this.selectedIndices = [0];
			this.selectedIndicesName = ["教育学"];
		},

		// onShow() {
		// 	this.getUserInformation();
		// },

		// mounted() {
		// 	this.getAllMajorsData();
		// },

		methods: {
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

			selectMajor(id) {
				this.selectedMajor = id;
				this.sendIdToBackend(id);
				if (this.majorPro && this.majorPro.length > 0) {
					// this.activeIndex_max = 1;
					this.toggleActive_max(0, 1);
				}
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

			toggleInst(id) {
				this.Inid = this.Inid === id ? null : id;
			},

			sendIdToBackend(id) {
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
			//第一级菜单
			init() {
				findFirstSub().then(resp => {
					this.majorPro = resp.data
				})
			},
			//第一个
			toggleActive_max(index, id) {
				if (this.activeIndex_max === index) {
					this.selectedIndices = [];
					this.selectedIndicesName = [];
					this.activeIndex = -1;
				} else {
					this.firstId = id;
					getSecondSub(this.firstId, this.selectedMajor).then(resp => {
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

		}
	};
</script>

<style>
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
</style>