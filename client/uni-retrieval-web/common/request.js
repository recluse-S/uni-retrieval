// import axios from "axios";
// import {
// 	UniAdapter
// } from "uniapp-axios-adapter";
// import {
// 	getCache
// } from "@/utils/cache.js";
// import {
// 	tansParams
// } from "@/utils/params.js";
// import {
// 	redirectToLogin
// } from "@/common/navigation.js"; // 导入跳转登录的工具函数

// // 每次请求都创建一个新的实例
// const request = axios.create({
// 	// baseURL: "http://localhost:8089",
// 	// baseURL: "http://8.153.38.226:8089",
// 	// baseURL: "https://recluse.asia",
// 	baseURL: "https://fjk.iirl.top/stu-api",
// 	timeout: 1000000,
// 	adapter: UniAdapter,
// 	headers: {
// 		'Content-Type': 'application/json'
// 	}
// });

// request.interceptors.request.use((config) => {
// 	const isToken = (config.headers || {}).isToken !== false;

// 	// 带上token
// 	if (getCache("token") && isToken) {
// 		config.headers['Authorization'] = 'Bearer ' + getCache("token");
// 		console.log("该请求携带了权限token");
// 	}

// 	// 处理请求参数
// 	if (config.params && config.method === 'post') {
// 		config.url = config.url + '?' + tansParams(config.params);
// 	}

// 	if (config.params && config.method === 'get') {
// 		config.url = config.url + '?' + tansParams(config.params);
// 	}

// 	return config;
// });

// request.interceptors.response.use(
// 	(response) => {
// 		const msg = response.data.msg;
// 		if (msg && msg.includes("token解析出错") || msg.includes("系统错误")) {
// 			// 如果出现token解析出错的错误信息
// 			console.log("Token失效，跳转到登录界面");
// 			redirectToLogin(); // 跳转到登录页面
// 		}
// 		return response;
// 	});

// export default request;


 
import { getCache } from "@/utils/cache.js";
import { tansParams } from "@/utils/params.js";
import { redirectToLogin } from "@/common/navigation.js"; // 确保路径正确

// 定义一个通用的请求函数
export function sendRequest(options) {
  const { url, method, data, params, headers = {} } = options; // 确保 headers 有默认值

  // 处理 URL 参数
  let requestUrl = url;
  if (params && (method === 'GET' || method === 'POST')) {
    requestUrl += '?' + tansParams(params);
  }

  // 添加请求拦截器
  const isToken = headers.isToken !== false;
  if (getCache("token") && isToken) {
    headers['Authorization'] = 'Bearer ' + getCache("token");
	headers['token'] = getCache("token");
    console.log("该请求携带了权限token");
  }

  return new Promise((resolve, reject) => {
    wx.request({
      url: requestUrl,
      method,
      data,
      header: headers,
      success: (res) => {
        const msg = res.data?.msg;
        if (msg && (msg.includes("token解析出错") || msg.includes("系统错误"))) {
          console.log("Token失效，跳转到登录界面");
          redirectToLogin();
          reject(new Error("Token失效"));
          return;
        }
        resolve(res.data); // 直接返回 data
      },
      fail: (err) => {
        reject(err);
      }
    });
  });
}



