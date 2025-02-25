const app = getApp();
const uPng = require('upng-js');
const {
	default: jsQR
} = require('jsqr');
const qrcodeParser = require('qrcode-parser');

Page({
	data: {
		id: '', // 保留一个id
		pageTitle: '',
		excelData: [],
		// 其他数据项...
		lastImageUrl: '',
		imageUrls: [],
	},

	handleImageLongpress(e) {
		const src = e.currentTarget.dataset.src; // 获取长按的图片源
		wx.showActionSheet({
			itemList: ['识别二维码']
		}).then((res) => {
			wx.createSelectorQuery()
				.select('#canvas')
				.fields({
					node: true
				})
				.exec(res => {
					const canvas = res[0].node;
					const ctx = canvas.getContext('2d');
					const img = canvas.createImage();
					img.src = src; // 使用传入的src
					img.onload = function() {
						ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
						let url = canvas.toDataURL();
						let buff = wx.base64ToArrayBuffer(url.split(',')[1]);
						let imgData = uPng.decode(buff);
						let rgba = uPng.toRGBA8(imgData)[0];
						let code = jsQR(new Uint8ClampedArray(rgba), canvas.width, canvas
							.height);
						console.log(code.data);
					};
				});
		});
	}
});