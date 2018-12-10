var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
// pages/chat/chatForm/chatForm.js
// https://blog.csdn.net/qq_35713752/article/details/78688311
// https://blog.csdn.net/qq_35713752/article/details/80811397
Page({

  /**
   * 页面的初始数据
   */
  data: {
    id: 0,
    historyList: [],
    otherSide: {},
    goods: {},
    isU1: false,
    myAvatar: '',
    scrollTop: 0,
    offsetTime: null,
    size: 10,
    scrollHeight: 0,
    newScrollHeight: 0,
    noMore: false,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let now = new Date();
    this.setData({
      id: options.id,
      myAvatar: wx.getStorageSync('userInfo').avatarUrl,
      offsetTime: now.toISOString()
    })
    this.getHistory();


  },

  getHistory: function() {
    let that = this;
    util.request(api.ChatForm + '/' + this.data.id, {
      offsetTime: this.data.offsetTime,
      size: this.data.size
    }).then(function(res) {
      if (res.errno === 0) {
        console.log(res.data);
        that.setData({
          otherSide: res.data.otherSide,
          historyList: res.data.historyList.concat(that.data.historyList),
          goods: res.data.goods,
          isU1: res.data.isU1,
          offsetTime: res.data.offsetTime,
        });

        if (res.data.historyList.length < that.data.size) {
          that.setData({
            noMore: true
          })
        }

        console.log(that.data.historyList.length)
        if (that.data.historyList.length < 11) {
          wx.setNavigationBarTitle({
            title: that.data.otherSide.nickName
          })

          // that.culScroll();   
          // setTimeout(function () {
          //   that.setData({
          //     scrollTop: 5000,
          //     scrollHeight: that.data.newScrollHeight
          //   })

          // }, 100);

          let _this = that
          that.getScrollHeight().then((res) => {
            var scroll = res - _this.data.scrollHeight
            _this.setData({
              scrollTop: 5000,
              scrollHeight: res,
            })
          })


        } else {
          console.log("加载了么")
          // that.queryMultipleNodes();   
          // setTimeout(function() {
          //   var scroll = that.data.newScrollHeight - that.data.scrollHeight
          //   console.log("加载了么: scrollTop " + scroll)
          //   console.log("加载了么: newScrollHeight " + that.data.newScrollHeight)
          //   console.log("加载了么: scrollHeight " + that.data.scrollHeight)
          //   that.setData({
          //     scrollTop: scroll,
          //     scrollHeight: that.data.newScrollHeight,
          //   })


          // }, 100);

          let _this=that
   
          that.getScrollHeight().then((res) => {
            var scroll = res - _this.data.scrollHeight
            _this.setData({
              scrollTop: scroll,
              scrollHeight: res,
            })
          })


        }
      } else {
        console.log(res)
      }
    })
  },
  culScroll: function() {
    let that = this

    var query = wx.createSelectorQuery()
    query.select('#hei').boundingClientRect()
    query.selectViewport().scrollOffset()
    query.exec(function(res) {
      console.log("同步设置newScrollHeight" + res[0].top)
      that.setData({
        newScrollHeight: res[0].top
      })

    })

  },
  toGoods: function(event) {
    let goodsId = event.target.dataset.id;
    wx.navigateTo({
      url: '/pages/goods/goods?id=' + goodsId,
    });
  },
  more: function() {
    console.log("到顶加载更多")
    if (!this.data.noMore) {
      this.getHistory()
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

  },
  getScrollHeight: function() {
    let that = this
    return new Promise(function(resolve, reject) {
      var query = wx.createSelectorQuery()
      query.select('#hei').boundingClientRect()
      query.selectViewport().scrollOffset()
      query.exec(function(res) {
        console.log("异步设置newScrollHeight" + res[0].top)
        resolve(res[0].top);
      })
    });

  }

})