export function setCache (key, value, expire = 3600*1000){
    let obj = {
      data: value, //存储的数据
      time: Date.now() / 1000, //记录存储的时间戳
      expire: expire //记录过期时间，单位秒
    }
    console.log('setCache:',value)
    uni.setStorageSync(key, JSON.stringify(obj))
}
 
export function getCache(key){
    
    let val = uni.getStorageSync(key)
    if (!val) {
      return null
    }
    val = JSON.parse(val)
    if (val.expire && Date.now() / 1000 - val.time > val.expire) {
      uni.removeStorageSync(key)
	  uni.navigateTo({
	  	url:'/pages/login/login'
	  })
      return null
    }
	console.log('getCache:',val.data)
    return val.data
  }