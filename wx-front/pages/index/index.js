const util = require('../../utils/util.js');
const api = require('../../config/api.js');
const user = require('../../services/user.js');

//获取应用实例
const app = getApp()
Page({
  data: {
    banner: [],
    channel: [],
    indexGoods: [],
    page: 1,
    size: 10,
    // newGoods: [],
    // hotGoods: [],
    // topics: [],
    // brands: [],
    // floorGoods: [],
    // banner: [],
    // channel: []
  },
  onShareAppMessage: function() {
    return {
      title: '古早交易平台',
      desc: '一款开源仿闲鱼交易平台！',
      path: '/pages/index/index'
    }
  },

  getIndexData: function() {
    let that = this;
    util.request(api.IndexUrl).then(function(res) {
      console.log(res.data)
      // console.log(res.data.hotGoodsList)
      // console.log(res.data.categoryList)
      if (res.errno === 0) {
        that.setData({
          // newGoods: res.data.newGoodsList,
          // hotGoods: res.data.hotGoodsList,
          // topics: res.data.topicList,
          // brand: res.data.brandList,
          // floorGoods: res.data.categoryList,
          indexGoods: res.data.indexGoodsList,
          banner: res.data.banner,
          channel: res.data.channel
        });
      }
    });
  },
  getIndexMore: function() {
    let that = this;
    util.request(api.IndexMore, {
      page: this.data.page,
      size: this.data.size
    }).then(function(res) {
      console.log(res.data)
      if (res.errno === 0) {
        that.setData({
          indexGoods: that.data.indexGoods.concat(res.data),
        });
      }
    });
  },
  onLoad: function(options) {
    this.getIndexData();

  },
  onReady: function() {
    // 页面渲染完成
  },
  onShow: function() {
    // 页面显示
  },
  onHide: function() {
    // 页面隐藏
  },
  onUnload: function() {
    // 页面关闭
  },

  onPullDownRefresh: function() {
    console.log("上拉刷新")
    this.onLoad()
    setTimeout(function callback() {
      wx.stopPullDownRefresh()
    }, 500)


  },
  onReachBottom: function() {
    console.log("拉到底")
    this.setData({
      page: this.data.page + 1
    })
    this.getIndexMore()
  },
})