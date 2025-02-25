import request from "@/common/req.js"

// //登录
// export function login(username, password) {
// 	return request({
// 		url: "/client/login",
// 		method: "POST",
// 		data: {
// 			name: username,
// 			password: password,
// 		}
// 	});
// }

// export function code(phone) {
// 	return request({
// 		url: "/sends/send/" + phone,
// 		method: "GET",

// 	});
// }

// export function getRecommend(day) {
// 	return request({
// 		url: "/client/retrieval/recommend",
// 		method: "GET",
// 		params: {
// 			day: day,
// 			}
// 	});
// }

// //注册
// export function register(username, school, station, phone, code) {
// 	return request({
// 		url: "/client/register",
// 		method: "POST",
// 		data: {
// 			name: username,
// 			school: school,
// 			station: station,
// 			phone: phone,
// 			code: code,
// 		}
// 	});
// }

// export function getSearchResults(req, input, major, location, postName, isUnder, postGrade, remark, releaseTime, pageSize) {
//     return request({
//         url: "/client/retrieval/resultList",
//         method: "POST",
//         params: {
//             req: req,
//             input: input,
//             major: major,
//             location: location,
//             postName: postName,
//             isUnder: isUnder,
//             postGrade: postGrade,
//             remark: remark,
//             releaseTime: releaseTime,
//             // pageNum: pageNum, 
//             pageSize: pageSize // 添加每页大小参数
//         }
//     });
// }

// //查询信息1
// export function findFirstSub() {
// 	return request({
// 		url: "/client/retrieval/firstSub",
// 		method: "GET"
// 	})
// }

// //查询信息2
// export function getSecondSub(id, academic) {
// 	return request({
// 		url: "/client/retrieval/secSub",
// 		method: "GET",
// 		params: {
// 			id: id,
// 			academic: academic
// 		}
// 	})
// }

// //查询信息3
// export function getThirdlySub(id) {
// 	return request({
// 		url: "/client/retrieval/thirdSub",
// 		method: "GET",
// 		params: {
// 			father: id,
// 		}
// 	})
// }

// //特殊查询
// export function getAllSelect(id, theId) {
// 	return request({
// 		url: "/client/retrieval/allSelect",
// 		method: "GET",
// 		params: {
// 			code: id,
// 			academic: theId
// 		}
// 	})
// }

// //点赞
// export function postOwnLike(id) {
// 	return request({
// 		url: "/client/retrieval/like",
// 		method: "POST",
// 		params: {
// 			id: id,
// 		}
// 	});
// }

// export function getResult(d) {
// 	return request({
// 		url: "/client/retrieval/getResult",
// 		method: "POST",
// 		data: {
// 			...d
// 		}
// 	})
// }

// //查询详情页面
// export function getTheDetail(id) {
// 	return request({
// 		url: "/client/retrieval/getDetail",
// 		method: "GET",
// 		params: {
// 			id: id,
// 		}
// 	})
// }

// //查询详情页面Excel
// export function getTheDetailExcel(name, year) {
// 	return request({
// 		url: "/client/retrieval/getOther",
// 		method: "GET",
// 		params: {
// 			institution: name,
// 			year: year
// 		}
// 	})
// }

// //查询收藏页面
// export function getOwn() {
// 	return request({
// 		method: 'GET',
// 		url: '/client/retrieval/likeList',
// 		headers: {
// 			isToken: true
// 		}
// 	});
// }

// //取消点赞
// export function unlike(id) {
// 	return request({
// 		url: "/client/retrieval/unlike",
// 		method: "POST",
// 		params: {
// 			id: id,
// 		}
// 	});
// }

// //获取搜索结果
// export function getALL() {
// 	return request({
// 		url: "/client/retrieval/getAll",
// 		method: "GET",
// 	});
// }

// // 获取用户信息
// export function getUserInfo() {
// 	return request({
// 		method: 'GET',
// 		url: '/client/retrieval/getUserInfo',
// 		headers: {
// 			isToken: true
// 		}
// 	});
// }

// // 修改密码
// export function updatePassword(password) {
// 	return request({
// 		method: 'POST',
// 		url: '/client/retrieval/updatePassword',
// 		params: {
// 			password: password,
// 		}
// 	});
// }



import {
	sendRequest
} from './request.js'; // 请确保路径正确

// 登录
export function login(username, password) {
	return new Promise((resolve, reject) => {
		wx.request({
			url: "https://recluse.asia/stu-api/client/login",
			method: "POST",
			data: {
				name: username,
				password: password,
			},
			success: (res) => resolve(res.data),
			fail: (err) => reject(err),
		});
	});
}

export function code(phone) {
	return new Promise((resolve, reject) => {
		wx.request({
			url: "https://recluse.asia/stu-api/sends/send/" + phone,
			method: "GET",
			success: (res) => resolve(res.data),
			fail: (err) => reject(err),
		});
	});
}

export function getRecommend(day) {
	return new Promise((resolve, reject) => {
		wx.request({
			url: "https://recluse.asia/stu-api/client/retrieval/recommend",
			method: "GET",
			data: {
				day: day,
			},
			success: (res) => resolve(res.data),
			fail: (err) => reject(err),
		});
	});
}

export function getHot() {
	return new Promise((resolve, reject) => {
		wx.request({
			url: "https://recluse.asia/stu-api/client/retrieval/getHot",
			method: "GET",
			success: (res) => resolve(res.data),
			fail: (err) => reject(err),
		});
	});
}

export function getHots() {
	return new Promise((resolve, reject) => {
		wx.request({
			url: "https://recluse.asia/stu-api/client/retrieval/getHots",
			method: "GET",
			success: (res) => resolve(res.data),
			fail: (err) => reject(err),
		});
	});
}

// export function getRecommend(day) {
// 	return request({
// 		url: "https://recluse.asia/stu-api/client/retrieval/recommend",
// 		method: "GET",
// 		params: {
// 			day: day,
// 		},
// 	});
// }

// 注册
export function register(username, school, station, phone, code) {
	return new Promise((resolve, reject) => {
		wx.request({
			url: "https://recluse.asia/stu-api/client/register",
			method: "POST",
			data: {
				name: username,
				school: school,
				station: station,
				phone: phone,
				code: code,
			},
			success: (res) => resolve(res.data),
			fail: (err) => reject(err),
		});
	});
}

export function getSearchResults(req, input, major, location, postName, isUnder, postGrade, remark, releaseTime,
	pageSize, token) {
	return sendRequest({
		url: "https://recluse.asia/stu-api/client/retrieval/resultList",
		// url: "http://localhost:8089/client/retrieval/resultList",
		method: "POST",
		data: {
			req,
			input,
			major,
			location,
			postName,
			isUnder,
			postGrade,
			remark,
			releaseTime,
			pageSize,
		},
		headers: {
			Authorization: `Bearer ${token}`, // 添加 token 到 headers 中
		}
	});
}

// 查询信息1
export function findFirstSub() {
	return new Promise((resolve, reject) => {
		wx.request({
			url: "https://recluse.asia/stu-api/client/retrieval/firstSub",
			method: "GET",
			success: (res) => resolve(res.data),
			fail: (err) => reject(err),
		});
	});
}

// 查询信息2
export function getSecondSub(id, academic) {
	return new Promise((resolve, reject) => {
		wx.request({
			url: "https://recluse.asia/stu-api/client/retrieval/secSub",
			method: "GET",
			data: {
				id: id,
				academic: academic
			},
			success: (res) => resolve(res.data),
			fail: (err) => reject(err),
		});
	});
}

// 查询信息3
export function getThirdlySub(id) {
	return new Promise((resolve, reject) => {
		wx.request({
			url: "https://recluse.asia/stu-api/client/retrieval/thirdSub",
			method: "GET",
			data: {
				father: id,
			},
			success: (res) => resolve(res.data),
			fail: (err) => reject(err),
		});
	});
}

export function getAllMajors() {
	return new Promise((resolve, reject) => {
		wx.request({
			url: "https://recluse.asia/stu-api/client/retrieval/allMajors",
			method: "GET",
			success: (res) => resolve(res.data),
			fail: (err) => reject(err),
		});
	});
}

export function getFilteredMajors() {
	return new Promise((resolve, reject) => {
		wx.request({
			url: "https://recluse.asia/stu-api/client/retrieval/allMajors",
			method: "GET",
			params: {
				query: keyword
			},
			success: (res) => resolve(res.data),
			fail: (err) => reject(err),
		});
	});
}

// 特殊查询
export function getAllSelect(id, theId) {
	return new Promise((resolve, reject) => {
		wx.request({
			url: "https://recluse.asia/stu-api/client/retrieval/allSelect",
			method: "GET",
			data: {
				code: id,
				academic: theId
			},
			success: (res) => resolve(res.data),
			fail: (err) => reject(err),
		});
	});
}

// 点赞
export function postOwnLike(id, token) {
	return sendRequest({
		url: "https://recluse.asia/stu-api/client/retrieval/like",
		method: "POST",
		params: {
			id: id,
		},
		headers: {
			Authorization: `Bearer ${token}`, // 添加 token 到 headers 中
		}
	});
}

export function getResult(d) {
	return new Promise((resolve, reject) => {
		wx.request({
			url: "https://recluse.asia/stu-api/client/retrieval/getResult",
			method: "POST",
			data: {
				...d
			},
			success: (res) => {
				resolve(res.data);
			},
			fail: (err) => {
				reject(err);
			}
		});
	});
}

// 查询详情页面
// export function getTheDetail(id) {
// 	return new Promise((resolve, reject) => {
// 		wx.request({
// 			url: "https://recluse.asia/stu-api/client/retrieval/getDetail",
// 			method: "GET",
// 			// header: headers,
// 			data: {
// 				id: id,
// 			},
// 			success: (res) => resolve(res.data),
// 			fail: (err) => reject(err),
// 		});
// 	});
// }

export function getTheDetail(id) {
	return sendRequest({
		url: "https://recluse.asia/stu-api/client/retrieval/getDetail",
		method: "GET",
		params: {
			id
		}, // 将 id 放入 params 中
		headers: {} // 如果需要，可以在这里添加 headers
	});
}

// 查询详情页面Excel
export function getTheDetailExcel(name, year) {
	return new Promise((resolve, reject) => {
		wx.request({
			url: "https://recluse.asia/stu-api/client/retrieval/getOther",
			method: "GET",
			data: {
				institution: name,
				year: year
			},
			success: (res) => resolve(res.data),
			fail: (err) => reject(err),
		});
	});
}

// 查询收藏页面
export function getOwn(token) {
	return sendRequest({
		url: "https://recluse.asia/stu-api/client/retrieval/likeList",
		method: "GET",
		params: {}, // 如果需要，可以在这里添加参数
		headers: {
			Authorization: `Bearer ${token}`, // 添加 token 到 headers 中
			isToken: true // 根据需要保留 isToken
		}
	});
}

// 取消点赞
export function unlike(id, token) {
	return sendRequest({
		url: "https://recluse.asia/stu-api/client/retrieval/unlike",
		method: "POST",
		params: {
			id: id,
		},
		headers: {
			Authorization: `Bearer ${token}`, // 添加 token 到 headers 中
		}
	});
}

// 获取搜索结果
export function getALL() {
	return new Promise((resolve, reject) => {
		wx.request({
			url: "https://recluse.asia/stu-api/client/retrieval/getAll",
			method: "GET",
			success: (res) => resolve(res.data),
			fail: (err) => reject(err),
		});
	});
}

// 获取用户信息
export function getUserInfo(token) {
	return sendRequest({
		url: "https://recluse.asia/stu-api/client/retrieval/getUserInfo",
		method: "GET",
		header: {
			Authorization: `Bearer ${token}`, // 添加 token 到 headers 中
		},
	});
}

export function deleteUsableTimes(token) {
	return sendRequest({
		url: "https://recluse.asia/stu-api/client/retrieval/deleteUsableTimes",
		method: "POST",
		header: {
			Authorization: `Bearer ${token}`,
		},
	});
}

export function updateUpTime(tradeNo, token) {
	return sendRequest({
		url: "https://recluse.asia/stu-api/client/retrieval/updateUpTime",
		method: "POST",
		params: {
			tradeNo: tradeNo,
		},
		header: {
			Authorization: `Bearer ${token}`,
		},
	});
}

// 修改密码
export function updatePassword(password) {
	return new Promise((resolve, reject) => {
		wx.request({
			method: 'POST',
			url: 'https://recluse.asia/stu-api/client/retrieval/updatePassword',
			data: {
				password: password,
			},
			success: (res) => resolve(res.data),
			fail: (err) => reject(err),
		});
	});
}

// 支付功能
export async function wxchatPay(token) {
	try {
		// 获取支付金额
		const amount = await fetchPaymentAmount();
		return sendRequest({
			method: 'POST',
			url: 'https://recluse.asia/stu-api/wechat/pay/createOrder',
			data: {
				orderId: generateOrderId(), // 动态生成订单号
				amount: amount, // 从后端获取的支付金额
				description: '商品描述', // 商品描述
			},
			headers: {
				token: token, // 添加 token 到 headers 中
			},
		});
	} catch (error) {
		console.error('支付请求失败:', error);
		throw error;
	}
}

// 生成一个唯一的订单号（可以参考后端方法）
function generateOrderId() {
	const timestamp = Date.now(); // 当前时间戳
	const random = Math.floor(Math.random() * 1000000); // 随机数
	return `${timestamp}${random}`; // 订单号 = 时间戳 + 随机数
}

// 获取支付金额的函数
export function fetchPaymentAmount() {
	return new Promise((resolve, reject) => {
		wx.request({
			method: 'GET',
			url: 'https://recluse.asia/stu-api/wechat/pay/getAmount',
			success: (res) => {
				resolve(res.data.data); // 返回金额，单位为分
			},
			fail: (err) => {
				console.error('获取支付金额失败:', err);
				reject(err);
			},
		});
	});
}

// 获取支付金额的函数
export function getPaymentAmount(password) {
	return new Promise((resolve, reject) => {
		wx.request({
			method: 'GET',
			url: 'https://recluse.asia/stu-api/wechat/pay/getAmount',
			success: (res) => resolve(res.data),
			fail: (err) => reject(err),
		});
	});
}
