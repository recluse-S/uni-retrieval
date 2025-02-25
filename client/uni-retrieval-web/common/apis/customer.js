import request from '@/common/axios.js'


//获取全部通告

export function getList(){
	return request({
		url: "/notice",
		method: "get",
		headers:{
			isToken:true
		},
	})
}

export function getDetail(id){
	return request({
		url: "/notice/detailNotice",
		method: "get",
		params:{
					id: id
				},
		headers:{
			isToken:true
		},
	})
}