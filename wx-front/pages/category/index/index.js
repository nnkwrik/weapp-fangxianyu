var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

Page({
  data: {
    navList: [],
    currentCategory: {},
    currentCategoryId: 0,
    currentCategoryName: '',
  },
  onLoad: function(options) {
    this.getCatalog();
  },
  getCatalog: function() {
    let that = this;
    wx.showLoading({
      title: '加载中...',
    });
    util.request(api.CatalogList).then(function(res) {
      that.setData({
        navList: res.data.allCategory,
        currentCategory: res.data.subCategory,
        currentCategoryId: res.data.allCategory[0].id,
        currentCategoryName: res.data.allCategory[0].name
      });
      wx.hideLoading();
    });

  },
  getCurrentCategory: function(id,name) {
    let that = this;
    this.setData({
      currentCategoryId: id,
      currentCategoryName: name
    })
    util.request(api.CatalogCurrent + "/" + id)
      .then(function(res) {
        that.setData({
          currentCategory: res.data,
        });
      });
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
  switchCate: function(event) {
    var that = this;
    var currentTarget = event.currentTarget;
    if (this.data.currentCategory.id == event.currentTarget.dataset.id) {
      return false;
    }
    this.getCurrentCategory(event.currentTarget.dataset.id, event.currentTarget.dataset.name);
  }
})