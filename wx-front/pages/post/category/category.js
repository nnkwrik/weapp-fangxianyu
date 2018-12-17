var app = getApp();
var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    parentId: 0,
    name: '',
    cateList: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    var that = this;
    if (options.id && options.name) {
      that.setData({
        parentId: parseInt(options.id),
        name: parseInt(options.name)
      });
    }

    that.getCateList();

  },

  getCateList() {
    let that = this;
    util.request(api.PostCateList + '/' + that.data.parentId).then(function(res) {
      if (res.errno === 0) {
        if (res.data.length > 0) {
          that.setData({
            cateList: res.data
          })
        } else {
          console.log(that.data.name)
          app.post.cate.id = that.data.parentId;
          app.post.cate.name = that.data.name;
          wx.navigateBack({
            delta: 1
          })

        }
      }
      console.log(res)
    });
  },
  tapCate: function(event) {
    if (event.currentTarget.dataset.id != this.data.parentId) {
      this.setData({
        parentId: event.currentTarget.dataset.id,
        name: this.data.name + ' ' + event.currentTarget.dataset.name,
      })
      this.getCateList();
    }

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