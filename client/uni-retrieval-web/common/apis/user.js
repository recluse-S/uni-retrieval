import request from '@/common/axios.js'



export function login(loginForm){
	return request({
		url: "/auth/login",
		method: "POST",
		data:{
			username:loginForm.username,
			password:loginForm.password,
			code:loginForm.code,
			uuid:loginForm.uuid
		},
		headers:{
			isToken:false
		}
		
	})
}
// 获取验证码
export function getCodeImg() {
  return request({
    url: '/code',
    // headers: {
    //   isToken: false
    // },
    method: 'get',
    timeout: 20000
  })
}
//获取用户详细信息
export function getInfo() {
  return request({
    url: '/system/user/getInfo',
    method: 'get'
  })
}

//根据用户名获取用户信息
export function getUserByUserName(username) {
  return request({
    url: '/system/user/username/'+username,
    method: 'get'
  })
}

//根据用户名获取用户信息
export function getUserByUserId(userId) {
  return request({
    url: '/system/user//'+userId,
    method: 'get'
  })
}

//新增用户
export function addUser(data) {
  return request({
    url: '/system/user',
    method: 'post',
	data:data
  })
}

//删除用户
export function delUser(userId) {
  return request({
    url: '/system/user/'+userId,
    method: 'delete',
  })
}

//修改用户
export function updateUser(data) {
  return request({
    url: '/system/user',
    method: 'put',
	data:data
  })
}

// 退出方法
export function logout() {
	uni.clearStorage()
  return request({
    url: '/auth/logout',
    method: 'delete'
  })
}

// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/system/user/profile',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/system/user/profile',
    method: 'put',
    data: data
  })
}

// 查询我的代理列表
export function listMyAgent() {
  return request({
    url: '/system/user/myagent',
    method: 'get',
  })
}

// 用户密码重置
export function updateUserPwd(oldPassword, newPassword) {
  return request({
    url: '/system/user/profile/updatePwd?oldPassword='+oldPassword+'&newPassword='+newPassword,
    method: 'put',
  })
}