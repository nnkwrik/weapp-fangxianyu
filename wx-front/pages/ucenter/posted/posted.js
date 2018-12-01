var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

var app = getApp();

Page({
  data: {
    postedList: [],
    page: 1,
    size: 10
  },
  getPostedList() {
    let that = this;
    util.request(api.PostedList, {
      page: this.data.page,
      size: this.data.size
    }).then(function (res) {
      if (res.errno === 0) {
        console.log(res.data);
        that.setData({
          postedList: that.data.postedList.concat(res.data),
        });
      }
    });
  },
  onLoad: function (options) {
    this.getPostedList();
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
    let goodsId = this.data.postedList[event.currentTarget.dataset.index].id;

    wx.navigateTo({
      url: '/pages/goods/goods?id=' + goodsId,
    });
  },
  onReachBottom: function () {
    console.log("拉到底")
    this.setData({
      page: this.data.page + 1
    })
    this.getPostedList()
  },

})