import request from '@/common/axios.js'

export function getReplies(){
	return request({
		url: "/replyList",
		method: "POST",
		headers:{
			isToken:true
		}
		
	})
}

export function addQuestion(form){
	return request({
		url: "/giveReply",
		method: "POST",
		data: {
			text: form
		},
		headers:{
			isToken:true
		}
	})
}
