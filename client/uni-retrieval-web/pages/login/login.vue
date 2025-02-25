<template>
	<view class="login-page">
		<view style="top: 50px; position: absolute;">
			<image src="../../static/home.png" style="width: 100px; height: 80px;"></image>
		</view>
		<view class="text"> 高校信息检索系统 </view>
		<view class="container">
			<view class="row justify-content-center mt-4">
				<view class="col-md-6">
					<view class="card mb-2">
						<view class="card-header text-center">

						</view>
						<view class="card-body">
							<form @submit.prevent="login">
								<view class="form-group">
									<label style="font-weight: bolder;" for="username">用户名</label>
									<input id="username" v-model="form.username" type="text" class="form-control"
										placeholder="请输入您的用户名" required>
								</view>

								<view class="button-container">
									<button type="submit" class="btn btn-primary btn-sm rounded-pill"
										@click="logining()">登录</button>
								</view>
							</form>

							<a @click="toRegister()" class="login-link">没有账号？前往注册~</a>

						</view>
					</view>

					<view v-if="errorMessage" class="alert alert-danger mt-3 text-center">
						{{ errorMessage }}
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		setCache,
		getCache
	} from '../../utils/cache.js'
	import {
		login
	} from "@/common/apis.js"
	export default {
		data() {
			return {
				form: {
					username: '',
					password: ''
				},
				errorMessage: ''
			};
		},

		methods: {
			logining() {
			    login(this.form.username, this.form.password)
			        .then(response => {
			            if (response.msg === 'success') {
			                const res = {
			                    data: {
			                        access_token: response.data,
			                        expires_in: 3600 * 1000 // 注意这里的单位是毫秒
			                    }
			                };
			                setCache("token", res.data.access_token, res.data.expires_in);
			
			                const token = getCache("token"); // 获取 token
			                console.log("当前 token:", token);
			
			                uni.reLaunch({
			                    url: '/pages/home/main'
			                });
			            } else {
			                uni.showToast({
			                    title: '登录失败，请检查用户名或密码',
			                    icon: 'error',
			                    duration: 2000
			                });
			            }
			        })
			        .catch(error => {
			            uni.showToast({
			                title: '登录失败，请检查用户名或密码',
			                icon: 'error',
			                duration: 2000
			            });
			        });
			},

			toRegister() {
				uni.reLaunch({
					url: '/pages/register/register'
				})
			}
		}
	};
</script>

<style scoped>
	.login-link {
		display: block;
		text-align: center;
		text-decoration: underline;
		color: blue;
		font-size: 16px;
	}

	.text {
		position: absolute;
		top: 28vh;
		font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;
		font-size: 4vh;
		font-weight: bolder;
	}

	.container {
		width: 80%;
		height: 20vh;
	}

	.login-page {
		display: flex;
		align-items: center;
		justify-content: center;
		height: 100vh;
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
		font-size: 20px;
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
</style>