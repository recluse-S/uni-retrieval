<template>
	<view class="login-page">
		<view class="text">注册页面</view>
		<view class="container">
			<view class="row justify-content-center mt-4">
				<view class="col-md-6">
					<view class="card mb-2">
						<view class="card-body">
							<form @submit.prevent="registering">

								<view class="form-group">
									<label style="font-weight: bolder;" for="username">用户名</label>
									<input id="username" v-model="form.username" @input="validateUsername" type="text"
										class="form-control" placeholder="请输入您的用户名" required />
									<span v-if="errors.username" class="error-text">{{ errors.username }}</span>
								</view>

								<view class="form-group">
									<label style="font-weight: bolder;" for="phone">手机号</label>
									<input id="phone" v-model="form.phone" type="text" class="form-control"
										placeholder="请输入您的手机号" required>
									<span v-if="errors.phone" class="error-text">{{ errors.phone }}</span>
								</view>

								<!-- <view class="form-group">
									<label style="font-weight: bolder;" for="code">验证码</label>
									<div class="input-button-container">
										<input id="code" v-model="form.code" type="text" class="form-control"
											placeholder="请输入您的验证码" required>
										<button @click="sendCode" type="primary" :disabled="disabled"
											style="font-size: 16px;">
											{{!codeTime ? '获取验证码' : codeTime + 's'}}
										</button>
									</div>
									<span v-if="errors.code" class="error-text">{{ errors.code }}</span>
								</view> -->

								<view class="form-group">
									<label style="font-weight: bolder;" for="code">验证码</label>
									<div class="input-button-container">
										<input id="code" v-model="form.code" type="text" class="form-control"
											placeholder="请输入您的验证码" required>
										<button @click="sendCode" type="primary" :disabled="disabled || codeTime > 0"
											style="font-size: 16px;">
											{{ codeTime > 0 ? codeTime + 's' : '获取验证码' }}
										</button>
									</div>
									<span v-if="errors.code" class="error-text">{{ errors.code }}</span>
								</view>

								<view class="form-group">
									<label style="font-weight: bolder;" for="school">报考学校</label>
									<input id="school" v-model="form.school" type="text" class="form-control"
										placeholder="请输入您想要报考的学校" required />
								</view>

								<view class="form-group">
									<label style="font-weight: bolder;" for="station">报考岗位</label>
									<input id="station" v-model="form.station" type="text" class="form-control"
										placeholder="请输入您想要报考的岗位" required />
								</view>

								<view class="button-container">
									<button type="submit" class="btn btn-primary btn-sm rounded-pill"
										@click="registering()">注册</button>
									<!-- <button class="btn btn-primary1 btn-sm rounded-pill" @click="toLogin()">前往登录</button> -->
								</view>
							</form>

							<a @click="toLogin()" class="login-link">已有账号，前往登录</a>

						</view>
					</view>

					<view v-if="errorMessage" class="alert alert-danger mt-3 text-center">
						{{ errorMessage }}
					</view>
				</view>
			</view>
		</view>

		<view>
			<!-- 提示信息弹窗 -->
			<uni-popup ref="message" type="message">
				<uni-popup-message :type="msgType" :message="messageText" :duration="2000"></uni-popup-message>
			</uni-popup>
		</view>

	</view>

</template>

<script>
	import {
		setCache,
		getCache
	} from '../../utils/cache.js'
	import {
		code,
		register
	} from '@/common/apis.js'
	export default {
		data() {
			return {
				codeTime: 0,
				form: {
					phone: '',
					username: '',
					school: '',
					station: '',
					code: '',
				},
				errors: {
					phone: '',
					username: '',
					school: '',
					station: '',
					code: '',
				},
				radio: "",
				disabled: false, // 发送短信按钮
			};
		},

		methods: {
			validatePhone() {
				if (this.form.phone.length !== 11) {
					this.errors.phone = '请输入正确的手机号';
					return false;
				}
				this.errors.phone = '';
				return true;
			},

			validateUsername() {
				const username = this.form.username;
				const usernameRegex = /^[a-zA-Z0-9]{4,8}$/;

				if (username.length === 0) {
					this.errors.username = '用户名不能为空';
				} else if (!usernameRegex.test(username)) {
					this.errors.username = '用户名必须是4到8个字符，且只能由数字和字母组成';
				} else {
					this.errors.username = '';
				}
			},

			// sendCode() {
			// 	if (this.validatePhone()) {
			// 		if (this.codeTime > 0) {
			// 			uni.showToast({
			// 				title: '不能重复获取',
			// 				icon: "none"
			// 			});
			// 			return;
			// 		} else {
			// 			code(this.form.phone).then(resp => {
			// 				this.codeTime = 60
			// 				let timer = setInterval(() => {
			// 					this.codeTime--;
			// 					if (this.codeTime < 1) {
			// 						clearInterval(timer);
			// 						this.codeTime = 0
			// 					}
			// 				}, 1000)
			// 			})

			// 		}
			// 	}
			// },

			sendCode() {
				if (this.validatePhone()) {
					if (this.codeTime > 0) {
						uni.showToast({
							title: '请稍后再获取',
							icon: "none"
						});
						return;
					}

					// 直接调用获取验证码的函数
					code(this.form.phone); // 这里调用获取验证码的函数

					// 开始倒计时
					this.codeTime = 60;
					let timer = setInterval(() => {
						this.codeTime--;
						if (this.codeTime < 1) {
							clearInterval(timer);
							this.codeTime = 0; // 重置为0
						}
					}, 1000);
				}
			},

			validateForm() {
				let isValid = true;
				this.errors = {
					phone: '',
					username: '',
					school: '',
					station: '',
					code: ''
				};
				// 验证用户名
				if (!this.form.username) {
					this.errors.username = '用户名不能为空';
					isValid = false;
				}
				if (!this.form.phone) {
					this.errors.phone = '电话号码不能为空';
					isValid = false;
				}
				if (!this.form.code) {
					this.errors.code = '验证码不能为空';
					isValid = false;
				}
				return isValid;
			},

			registering() {
				if (!this.validateForm()) {
					uni.showToast({
						title: '注册失败',
						icon: 'error',
						duration: 2000
					});
					return;
				}
				register(this.form.username, this.form.school, this.form.station, this.form.phone, this.form.code)
					.then(response => {
						// 假设 response.data 是后端返回的整个响应
						if (response.code === 0) {
							// 用户名重复的处理
							uni.showToast({
								title: response.msg,
								icon: 'none', // 使用 'none' 来显示文本而不是图标
								duration: 3000
							});
						} else if (response.msg === 'success') {
							// 注册成功
							uni.showToast({
								title: '注册成功',
								icon: 'success',
								duration: 2000
							});
							uni.reLaunch({
								url: '/pages/login/login'
							})
						} else {
							// 其他错误情况
							uni.showToast({
								title: '注册失败',
								icon: 'error',
								duration: 2000
							});
						}
					})
					.catch(error => {
						// 网络或其他错误
						uni.showToast({
							title: '注册失败，请稍后再试',
							icon: 'error',
							duration: 2000
						});
					});
			},

			toLogin() {
				uni.reLaunch({
					url: '/pages/login/login'
				})
			}
		}
	};
</script>

<style scoped>
	.login-page>input {
		font-size: 10px;
	}

	input::-webkit-input-placeholder {
		font-size: 10px;
	}

	.text {
		position: absolute;
		top: 8vh;
		font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;
		font-size: 2vh;
		font-weight: bolder;
		font-size: 30px;
	}

	.container {
		width: 80%;
		height: 20vh;
	}

	.login-page {
		display: flex;
		align-items: center;
		justify-content: center;
		height: 60vh;
	}

	.form-control {
		border-bottom: 1px solid #cccccc;
		margin-bottom: 1vh;
	}

	.card-header {
		font-weight: bolder;
		padding: 10px;
		border-top-left-radius: 10px;
		border-top-right-radius: 10px;
		text-align: center;
	}

	.form-group {
		margin-bottom: 15px;
		/* font-size: 20px; */
	}

	.button-container {
		display: flex;
		justify-content: space-between;
		gap: 10px;
		padding: 10px;
	}

	.btn-primary {
		background-color: #007bff;
		color: #fff;
		border: none;
		border-radius: 20px;
		padding: 2px 20px;
		width: 60%;
		height: 80%;
	}

	.btn-primary:hover {
		background-color: #0056b3;
	}

	.btn-primary:focus {
		transform: scale(1.1);
		outline: none;
	}

	.btn-primary1 {
		background-color: #007bff;
		color: #fff;
		border: none;
		border-radius: 20px;
		padding: 2px 20px;
		width: 60%;
		height: 70%;
	}

	.btn-primary1:hover {
		background-color: #0056b3;
	}

	.btn-primary1:focus {
		transform: scale(1.1);
		outline: none;
	}

	.login-link {
		display: block;
		/* 使链接成为块级元素，以便可以水平居中 */
		text-align: center;
		/* 水平居中对齐 */
		text-decoration: underline;
		/* 添加下划线 */
		color: blue;
		/* 设置文本颜色为蓝色 */
		font-size: 16px;
		/* 可选：设置字体大小 */
	}

	.error-text {
		color: red;
		font-size: 14px;
	}

	.input-button-container {
		display: flex;
		align-items: center;
	}

	.input-button-container input {
		flex: 1;
		margin-right: 10px;
	}

	.input-button-container button {
		position: relative;
		top: -10px;
		width: auto;
		height: 36px;
		background-color: #17abe3;
	}
</style>