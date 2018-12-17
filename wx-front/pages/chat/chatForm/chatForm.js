var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var websocket = require('../../../services/websocket.js');
// 参考:
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
    input: '',
    typing: '',
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
    this.openListen();

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
          offsetTime: res.data.offsetTime+"",
        });
        

        if (res.data.historyList.length < that.data.size) {
          that.setData({
            noMore: true
          })
        }
        if (that.data.historyList.length < 11) {
          wx.setNavigationBarTitle({
            title: that.data.otherSide.nickName
          })

          let _this = that
          that.getScrollHeight().then((res) => {
            var scroll = res - _this.data.scrollHeight
            _this.setData({
              scrollTop: 5000,
              scrollHeight: res,
            })
          })

        } else {
          //重新设置scroll,scrollTop = 之前的scrollHeigth - 加入了数据后的scrollHeigth
          let _this = that
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
  openListen:function(){
    let that = this
    websocket.listenChatForm(this.data.id).then(res => {
      var newHistory = [{
        chatId: res.chatId,
        u1ToU2: res.senderId < res.receiverId ? true : false,
        messageType: res.messageType,
        messageBody: res.messageBody,
        sendTime: res.sendTime,
      }]
      that.addHistoryList(newHistory)
      that.openListen()

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
  getScrollHeight: function() {
    let that = this
    return new Promise(function(resolve, reject) {
      var query = wx.createSelectorQuery()
      //#hei是位于scroll最低端的元素,求它离scroll顶部的距离,得出ScrollHeight
      query.select('#hei').boundingClientRect()
      query.selectViewport().scrollOffset()
      query.exec(function(res) {
        console.log("异步设置ScrollHeight" + res[0].top)
        resolve(res[0].top);
      })
    });
  },
  inputChange: function(e) {
    console.log(e.detail.value)
    this.setData({
      input: e.detail.value
    })
  },
  sendMsg: function() {

    let that = this
    var data = this.createMsg()
    var input = this.data.input
    this.setData({
      typing: '',
      input: '',
    })
    if (input.trim() == '') {
      return
    }
    //通过webSocket发送消息
    websocket.sendMessage(data).then(res => {
      console.log(res)

      var newHistory = [{
        chatId: this.data.id,
        u1ToU2: wx.getStorageSync('userInfo').openId < this.data.otherSide.openId ? true : false,
        messageType: 1,
        messageBody: input,
        sendTime: util.formatTime(new Date()),
      }]

      that.addHistoryList(newHistory)


    }).catch((res) => {
      console.log(res)
      util.showErrorToast('发送失败')
    });

  },
  addHistoryList: function(historyList) {
    //把新的数据加入目前的对话框
    var newHistoryList = this.data.historyList.concat(historyList)
    this.setData({
      historyList: newHistoryList,
    })


    //重新设置scroll
    let _this = this
    this.getScrollHeight().then((res) => {
      var scroll = res - _this.data.scrollHeight
      _this.setData({
        scrollTop: 100000000,
        scrollHeight: res,
      })
    })
  },
  createMsg: function() {
    var msgType;
  
    if (this.data.historyList.length>1) {
      msgType = 1
    } else {
      msgType = 3
    }

    var data = JSON.stringify({
      chatId: this.data.id,
      receiverId: this.data.otherSide.openId,
      senderId: wx.getStorageSync('userInfo').openId,
      goodsId: this.data.goods.id,
      messageType: msgType,
      messageBody: this.data.input
    })
    return data
  },
  buy:function(){
    util.showErrorToast('功能开发中')
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
    websocket.listenBadge()
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


})