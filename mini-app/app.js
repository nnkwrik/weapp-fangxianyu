//app.js
App({
  onLaunch: function() {
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    wx.getUserInfo({
      success: res => {
        // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
        // 没授权的状态下调用getUserInfo会弹框

        this.globalData.userInfo = res.userInfo
        this.globalData.rawData = res.rawData
        this.globalData.signature = res.signature
        console.log("获取用户信息" + res.rawData)

        // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
        // 所以此处加入 callback 以防止这种情况
        if (this.userInfoReadyCallback) {
          console.log("app.userInfoReadyCallback" + res.iv)
          this.userInfoReadyCallback(res)
        }
      }
    })
    // 检查sessionKey
    wx.checkSession({
      fail() {
        // session_key 已经失效，需要重新执行登录流程
        wx.login({
          success: res => {
            var userInfo = wx.getStorageSync("userInfo")
            if (userInfo == null && wx.setStorageSync("token") == null){
              //注册
              console.log("注册")
              wx.setStorage("userInfo", this.globalData.userInfo)
            } else if (userInfo != this.globalData.userInfo) {
              //更新 userInfo 和 token
              console.log("更新 userInfo 和 token")
              wx.setStorage("userInfo", this.globalData.userInfo)
            } else  {
              //更新token
              console.log("更新token")
            } 
          }
        })
      }
    })

    // 获取用户信息
    // wx.getSetting({
    //   success: res => {
    //     if (res.authSetting['scope.userInfo']) {
    //       // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
    //       // 没授权的状态下调用getUserInfo会弹框
    //       wx.getUserInfo({
    //         success: res => {
    //           console.log("app.getUserInfo" + res.iv)
    //           // 可以将 res 发送给后台解码出 unionId
    //           this.globalData.userInfo = res.userInfo
    //           this.globalData.rawData = res.rawData
    //           this.globalData.signature = res.signature
    //           this.globalData.encryptedData = res.encryptedData
    //           this.globalData.iv = res.iv

    //           // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
    //           // 所以此处加入 callback 以防止这种情况
    //           if (this.userInfoReadyCallback) {
    //             console.log("app.userInfoReadyCallback" + res.iv)
    //             this.userInfoReadyCallback(res)
    //           }
    //         }
    //       })
    //     }
    //   }
    // })
  },
  register: function () {
    //注册新用户
    wx.request({
      url: this.globalData.host + "/register",
      data: {
        jsCode: res.code,
        rawData: this.globalData.rawData,
        signature: this.globalData.signature
      },
      header: {
        'content-type': 'application/json' // 默认值
      },
      dataType: "json",
      method: "POST",
      success(res) {
        console.log(res.data)
      }
    })
  },
  globalData: {
    userInfo: null,
    rawData: '',
    signature: '',
    host: "https://77ee0a14.ngrok.io"
  }
})

/*
已登录的情况
app.getUserInfosCddGUbsg0MB3oB7tvlmfA==
app.userInfoReadyCallbacksCddGUbsg0MB3oB7tvlmfA==
page.userInfoReadyCallbacksCddGUbsg0MB3oB7tvlmfA==
*/
/*
未登录
先点，点击登录授权
page.bindGetUserInfoBObXMSFjVRixIoyncg2kyA==
*/