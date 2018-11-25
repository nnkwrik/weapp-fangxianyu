const util = require('../../utils/util.js');
const api = require('../../config/api.js');
const user = require('../../services/user.js');

//获取应用实例
const app = getApp()
Page({
  data: {
    banner: [],
    channel: [],
    indexGoods: []
    // newGoods: [],
    // hotGoods: [],
    // topics: [],
    // brands: [],
    // floorGoods: [],
    // banner: [],
    // channel: []
  },
  onShareAppMessage: function () {
    return {
      title: '古早交易平台',
      desc: '一款开源仿闲鱼交易平台！',
      path: '/pages/index/index'
    }
  },

  getIndexData: function () {
    let that = this;
    util.request(api.IndexUrl).then(function (res) {
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
  onLoad: function (options) {
    this.getIndexData();
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },
})
