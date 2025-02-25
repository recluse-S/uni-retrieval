// @/utils/navigation.js
export const redirectToLogin = () => {
  // 这里的逻辑可以根据你的uni-app路由进行修改
  uni.redirectTo({
    url: '/pages/test/login' // 假设登录页面路径为/pages/login/login
  });
};
