

<script>
export default {
  onLaunch: function () {
    console.log('App Launch')
    this.checkForUpdate() // 检查更新
  },

  onShow: function () {
    console.log('App Show')
    this.checkForUpdate() // 也可以在这里检查更新
  },

  onHide: function () {
    console.log('App Hide')
  },

  methods: {
    checkForUpdate() {
      // 仅在非开发工具环境中检查更新
      if (uni.getSystemInfoSync().platform !== 'devtools') {
        const updateManager = uni.getUpdateManager()

        // 检查更新的回调
        updateManager.onCheckForUpdate(function (res) {
          console.log('是否有更新:', res.hasUpdate)
          if (res.hasUpdate) {
            // 有更新时
            updateManager.onUpdateReady(function () {
              // 新版本已下载完成，提示用户重启小程序
              uni.showModal({
                title: '更新提示',
                content: '新版本已准备好，是否重启应用？',
                success: (res) => {
                  if (res.confirm) {
                    // 用户点击确定，应用更新
                    updateManager.applyUpdate()
                  }
                }
              })
            })

            updateManager.onUpdateFailed(function () {
              // 更新失败的回调
              uni.showToast({
                title: '新版本下载失败，请稍后再试。',
                icon: 'none'
              })
            })
          }
        })
      }
    }
  }
}
</script>

<style>
  /*每个页面公共css */
</style>
