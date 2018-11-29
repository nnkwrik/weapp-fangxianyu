var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

var app = getApp();

Page({
  data: {
    soldList: []
  },
  getCollectList() {
    let that = this;
    util.request(api.SoldList).then(function (res) {
      if (res.errno === 0) {
        console.log(res.data);
        that.setData({
          soldList: res.data
        });
      }
    });
  },
  onLoad: function (options) {
    this.getCollectList();
  },
  onReady: function () {

  },
  onShow: function () {

  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭
  },
  openGoods(event) {
    let goodsId = this.data.soldList[event.currentTarget.dataset.index].id;

    wx.navigateTo({
      url: '/pages/goods/goods?id=' + goodsId,
    });
  },

})