//index.js
//获取应用实例
const app = getApp()
Page({
  data: {
    motto: 'Hello World',
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo')//组件是否可用
  },
  //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  onLoad: function () {
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse){
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        console.log("page.userInfoReadyCallback" + res.iv)
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      wx.getUserInfo({
        success: res => {
          app.globalData.userInfo = res.userInfo
          this.setData({
            userInfo: res.userInfo,
            hasUserInfo: true
          })
        }
      })
    }
  },
  bindGetUserInfo: function(e) {
    console.log("page.bindGetUserInfo" + e.detail.iv)
    console.log(e)
    app.globalData.userInfo = e.detail.userInfo
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  },
  login: function() {
    // 获取userInfo
    wx.getUserInfo({
        success: res => {
            app.globalData.rawData = res.rawData
            app.globalData.signature = res.signature
          console.log(app.globalData.rawData)
          console.log(app.globalData.signature)
        }
      }),
      wx.login({
        success(res) {
          if (res.code) {

            //发起网络请求
            wx.request({
              url: app.globalData.host + "/register",
              data: {
                jsCode: res.code,
                rawData: app.globalData.rawData,
                signature: app.globalData.signature
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
          } else {
            console.log('登录失败！' + res.errMsg)
          }
        }
      })
  }

})