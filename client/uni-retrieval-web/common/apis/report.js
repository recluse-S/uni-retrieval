import request from '@/common/axios.js'


//获取全部通告

export function getReportList(){
	return request({
		url: "/report/allList",
		method: "get",
		headers:{
			isToken:true
		}
	})
}