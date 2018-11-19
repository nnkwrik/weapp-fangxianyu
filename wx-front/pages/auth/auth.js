// pages/auth/auth.js
var app = getApp();
var api = require('../../config/api.js');
var util = require('../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: app.globalData.userInfo,
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {

  },

  startLogin: function(e) {
    console.log(e);
    this.login(e.detail).then((userInfo) => {
      this.setData({
        userInfo: userInfo,
        hasUserInfo: true
      })
    });
  },

  login: function (detail) {
    let code = null;
    return new Promise(function (resolve, reject) {
      return util.login().then((res) => {
        code = res.code;
      }).then(() => {
        //登录远程服务器
        util.request(api.AuthLoginByWeixin, {
          code: code,
          detail: detail,
          firstLogin: true
        }, 'POST').then(res => {
          if (res.errno === 0) {
            //存储用户信息
            wx.setStorageSync('userInfo', res.data.userInfo);
            wx.setStorageSync('token', res.data.token);

            //反应到当前登录
            app.globalData.userInfo = res.data.userInfo;
            app.globalData.token = res.data.token;

            resolve(res.data.userInfo);
          } else {
            reject(res.data.userInfo);
          }
        }).catch((err) => { //request
          reject(err);
        });
      }).catch((err) => {   //login
        reject(err);
      })
    });
  },
  goback: function () {
    wx.navigateBack({
      delta: 1
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})