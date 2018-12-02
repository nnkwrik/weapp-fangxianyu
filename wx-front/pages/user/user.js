var util = require('../../utils/util.js');
var api = require('../../config/api.js');
var user = require('../../services/user.js');
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userId: '',
    page: 1,
    size: 10000,
    userInfo: {},
    historyList: {},
    soldCount: 0,
    userDates:0
  },
  getUserPage() {
    let that = this;
    util.request(api.UserPage + '/' + this.data.userId, {
      page: this.data.page,
      size: this.data.size
    }).then(function(res) {
      if (res.errno === 0) {
        console.log(res.data);


        for (var list in res.data.userHistory) {
          for (var i = 0; i < res.data.userHistory[list].length; i++) {
            var goods = res.data.userHistory[list][i];
            if (goods.time) {
              res.data.userHistory[list][i].time = goods.time.split(' ')[0]
            }
            if (goods.postTime) {
              res.data.userHistory[list][i].postTime = goods.postTime.split(' ')[0]
            }
            if (goods.soldTime) {
              res.data.userHistory[list][i].soldTime = goods.soldTime.split(' ')[0]
            }
          }

          
        }

        //计算卖家来平台第几天
        let registerTime = res.data.user.registerTime
        let duration = new Date().getTime() - new Date(registerTime).getTime();
        let dates = parseInt(Math.floor(duration) / (1000 * 60 * 60 * 24));

        that.setData({
          userInfo: res.data.user,
          historyList: res.data.userHistory,
          soldCount: res.data.soldCount,
          userDates: dates
        });

      } else {
        console.log(res.errmsg)
      }
    });
  },


  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.setData({
      userId: options.userId
    });
    this.getUserPage();
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
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  },

  openGoods(event) {
    let goodsId = event.currentTarget.dataset.id

    wx.navigateTo({
      url: '/pages/goods/goods?id=' + goodsId,
    });
  },
  onReachBottom: function() {
    console.log("拉到底")
    this.setData({
      page: this.data.page + 1
    })
    // this.getUserPageMore()
  },

})