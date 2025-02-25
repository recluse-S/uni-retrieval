<template>
	<view class="container">
		<!-- 按钮 -->
		<button class="login-button" open-type="getPhoneNumber" @getphonenumber="getPhoneNumber" type="primary"
			:disabled="!isAgreed">
			手机号快速登录
		</button>

		<!-- 复选框和协议链接 -->
		<view class="agreement-container">
			<checkbox-group @change="onAgreementChange">
				<label class="agreement-label">
					<checkbox value="agree" class="agreement-checkbox" />
					<text class="agreement-text">我已阅读并同意</text>
					<text class="agreement-link" @touchstart.stop.prevent="openAgreement">《微信小程序相关协议》</text>
				</label>
			</checkbox-group>
		</view>
	</view>
</template>

<script>
	import {
		setCache,
		getCache
	} from '../../utils/cache.js'

	export default {
		data() {
			return {
				isAgreed: false // 新增一个变量用于跟踪协议是否被勾选
			};
		},

		methods: {
			// 复选框勾选事件
			onAgreementChange(e) {
				this.isAgreed = e.detail.value.includes('agree'); // 根据复选框的选中状态更新isAgreed
			},

			// 打开协议链接事件
			openAgreement() {
				// 这里可以添加打开协议页面的逻辑
				// 跳转到主页
				uni.navigateTo({
					url: '/pages/test/xieyi'
				});
			},

			async getPhoneNumber(e, retryCount = 0) {
			    if (retryCount > 3) {
			        this.handleLoginError('请稍后再试');
			        return; // 防止无限递归
			    }
			
			    if (e.detail.errMsg === 'getPhoneNumber:ok') {
			        const { encryptedData, iv } = e.detail;
			
			        try {
			            const loginRes = await wx.login(); // 获取登录凭证 code
			
			            if (loginRes.code) {
			                const openidRes = await uni.request({
			                    url: 'https://recluse.asia/stu-api/client/login/getOpenid',
			                    method: 'POST',
			                    data: {
			                        code: loginRes.code
			                    }
			                });
			
			                if (openidRes.data && openidRes.data.openid) {
			                    const openid = openidRes.data.openid;
			
			                    const newLoginRes = await wx.login(); // 重新获取新的 code
			
			                    if (newLoginRes.code) {
			                        const decryptRes = await this.retryRequest(() => {
			                            return uni.request({
			                                url: 'https://recluse.asia/stu-api/client/login/decryptPhoneNumber',
			                                method: 'POST',
			                                data: {
			                                    code: newLoginRes.code,
			                                    openid: openid,
			                                    encryptedData,
			                                    iv
			                                }
			                            });
			                        });
			
			                        if (decryptRes.data.code === 200) {
			                            const tokenData = {
			                                access_token: decryptRes.data.data.token,
			                                expires_in: 3600 * 1000
			                            };
			                            this.handleLoginSuccess(tokenData);
			                        } else {
			                            await this.getPhoneNumber(e, retryCount + 1); // 递归调用时增加重试次数
			                        }
			                    } else {
			                        this.handleLoginError('获取新的登录凭证失败，请重试');
			                    }
			                } else {
			                    this.handleLoginError('获取 openid 失败，请重试');
			                }
			            } else {
			                this.handleLoginError('获取登录凭证失败，请重试');
			            }
			        } catch (error) {
			            this.handleLoginError('请求失败，请重试');
			        }
			    } else {
			        this.handleLoginError('用户拒绝授权');
			    }
			},

			async retryRequest(fn, retries = 3, delay = 1000) {
				let attempt = 0;
				while (attempt < retries) {
					try {
						return await fn(); // 执行请求
					} catch (error) {
						attempt++;
						if (attempt >= retries) throw error; // 重试次数用尽，抛出错误
						await new Promise(res => setTimeout(res, delay)); // 延迟重试
					}
				}
			},

			// 处理登录成功的逻辑
			handleLoginSuccess(response) {
				const res = {
					data: {
						access_token: response.access_token,
						expires_in: response.expires_in
					}
				};

				// 存储 token 到缓存
				setCache("token", res.data.access_token, res.data.expires_in);
				const token = getCache("token"); // 获取 token
				console.log("当前 token:", token);

				// 跳转到主页
				uni.reLaunch({
					url: '/pages/home/main'
				});
			},

			// 处理登录失败的逻辑
			handleLoginError(message) {
				uni.showToast({
					title: message || '登录失败，请重试',
					icon: 'error',
					duration: 2000
				});
			},

			// 延迟函数，返回一个 Promise，等待指定时间后 resolve
			delay(ms) {
				return new Promise(resolve => setTimeout(resolve, ms));
			}
		}
	};
</script>

<style>
	/* 容器居中 */
	.container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		height: 100vh;
		/* 使用视口高度确保垂直居中 */
		padding: 20px;
		box-sizing: border-box;
	}

	/* 按钮样式 */
	.login-button {
		width: 80%;
		padding: 10px 0;
		border-radius: 50px;
		/* 圆弧状 */
		background-color: black;
		color: white;
		font-size: 16px;
		margin-bottom: 20px;
		/* 与下方内容间隔 */
	}

	/* 复选框和协议链接样式 */
	.agreement-container {
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 14px;
	}

	.agreement-label {
		display: flex;
		align-items: center;
	}

	.agreement-checkbox {
		margin-right: 8px;
		/* 调整复选框与文本的间距 */
	}

	.agreement-text {
		margin-right: 5px;
		/* 调整文本间距 */
	}

	.agreement-link {
		color: blue;
		text-decoration: underline;
	}
</style>













// methods: {
// async getPhoneNumber(e) {
// if (e.detail.errMsg === 'getPhoneNumber:ok') {
// // 用户同意授权，获取手机号信息
// const { encryptedData, iv } = e.detail;

// // 获取用户登录凭证 code
// wx.login({
// success: async (loginRes) => {
// if (loginRes.code) {
// console.log('获取到 code:', loginRes.code);

// // 将 code、encryptedData 和 iv 发送到后端解密
// const res = await uni.request({
// // url: 'http://localhost:8089/client/login/decryptPhoneNumber', // 你的后端解密接口
// url: 'https://fjk.iirl.top/stu-api/client/login/decryptPhoneNumber',
// method: 'POST',
// data: {
// code: loginRes.code,
// encryptedData,
// iv
// }
// });

// if (res.data.code === 200) {
// const phoneNumber = res.data.data.phoneNumber;
// console.log('手机号:', phoneNumber);
// // 在此处处理登录逻辑
// uni.reLaunch({
// url: '/pages/home/main'
// });
// } else {
// console.error('解密手机号失败', res);
// }
// } else {
// console.error('获取 code 失败！', loginRes.errMsg);
// }
// }
// });
// } else {
// console.error('用户拒绝授权', e.detail.errMsg);
// }
// }
// }