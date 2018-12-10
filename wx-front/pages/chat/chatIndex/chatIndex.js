var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

var app = getApp();

Page({
  data: {
    cartGoods: [],
    cartTotal: {
      "goodsCount": 0,
      "goodsAmount": 0.00,
      "checkedGoodsCount": 0,
      "checkedGoodsAmount": 0.00
    },
    isEditCart: false,
    checkedAllStatus: true,
    editCartList: [],

    chatList: [],
    offsetTime: null,
    size: 10,
  },
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数


  },
  onReady: function() {
    // 页面渲染完成

  },
  onShow: function() {
    // 页面显示
    let now = new Date();
    console.log(now.toISOString())

    this.setData({
      offsetTime: now.toISOString()
    })

    this.getChatList();
  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭

  },
  getChatList: function() {
    let that = this;
    util.request(api.ChatIndex, {
      size: this.data.size,
      offsetTime: this.data.offsetTime
    }).then(function(res) {
      if (res.errno === 0) {
        console.log(res.data);
        that.setData({
          chatList: that.data.chatList.concat(res.data),
        });
      } else {
        console.log(res)
      }
    })
  },
  onPullDownRefresh: function () {
    console.log("上拉刷新")
    this.setData({
      chatList: [],
      offsetTime: null,
      size: 10,
    })
    this.onShow()
    setTimeout(function callback() {
      wx.stopPullDownRefresh()
    }, 500)


  },
  onReachBottom: function () {
    console.log("拉到底")
    let chatList = this.data.chatList;
    let offsetTime = chatList[chatList.length - 1].offsetTime;
    this.setData({
      offsetTime: offsetTime,
    })

    this.getChatList()
  },
})