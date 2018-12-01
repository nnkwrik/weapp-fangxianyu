var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

var app = getApp();

Page({
  data: {
    soldList: [],
    page: 1,
    size: 10
  },
  getSoldList() {
    let that = this;
    util.request(api.SoldList, {
      page: this.data.page,
      size: this.data.size
    }).then(function (res) {
      if (res.errno === 0) {
        console.log(res.data);
        that.setData({
          soldList: that.data.soldList.concat(res.data),
        });
      }
    });
  },
  onLoad: function (options) {
    this.getSoldList();
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
  onReachBottom: function () {
    console.log("拉到底")
    this.setData({
      page: this.data.page + 1
    })
    this.getSoldList()
  },

})