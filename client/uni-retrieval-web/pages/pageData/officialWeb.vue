<template>
	<view class="wrapper">
		<web-view :src="url"></web-view>
	</view>
</template>

<script>
	import {
		getTheDetail,
		getTheDetailExcel,
		postOwnLike,
	} from "@/common/apis.js"

	export default {
		name: 'officialWeb',
		data() {
			return {
				url: ''
			}
		},

		onLoad(options) {
			if (options && options.link) {
				this.url = options.link;
			} else {
				console.error('未找到有效的 id 参数');
			}
		},

		methods: {
			
		}
	}
</script>

<style scoped>
	.wrapper {
		width: 100%;
		height: 100%;
	}
</style>