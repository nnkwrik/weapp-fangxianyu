var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var websocket = require('../../../services/websocket.js');

var app = getApp();

Page({
  data: {
    chatList: [],
    offsetTime: null,
    size: 10,
  },
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数

  },
  openListen: function() {
    let that = this
    websocket.listenChatIndex().then(res => {

      //存在与目前list中
      let chatList = this.data.chatList
      for (var i in chatList) {
        if (chatList[i].lastChat.chatId == res.chatId) {
          var target = chatList[i]
          var newChatList = []


          target.unreadCount++;
          target.u1ToU2 = res.senderId < res.receiverId ? true : false
          target.lastChat.messageType = res.messageType
          target.lastChat.messageBody = res.messageBody
          target.lastChat.sendTime = res.sendTime

          chatList.splice(i, 1);
          console.log("splice")
          console.log(chatList)

          newChatList.push(target)
          newChatList = newChatList.concat(chatList)

          that.setData({
            chatList: newChatList
          })
          that.openListen()
          return
        }
      }
      //不存在, 后端可以专门写个api
      that.onShow()
    })
  },
  onReady: function() {
    // 页面渲染完成

  },
  onShow: function() {
    // 页面显示
    let now = new Date();
    this.setData({
      offsetTime: now.toISOString(),
      chatList: []
    })

    this.getChatList();
    this.openListen();
  },
  onHide: function() {
    // 页面隐藏
    websocket.listenBadge()

  },
  onUnload: function() {
    // 页面关闭

  },
  getChatList: function() {
    let that = this;
    util.request(api.ChatIndex, {
      size: this.data.size,
      offsetTime: this.data.offsetTime
    }).then(function(res) {
      if (res.errno === 0) {
        console.log(res.data);
        that.setData({
          chatList: that.data.chatList.concat(res.data),
        });
      } else {
        console.log(res)
      }
    })
  },
  navForm: function(e) {
    var chatId = e.currentTarget.dataset.id
    var index = e.currentTarget.dataset.index
    var chatList = this.data.chatList

    //减少tapbar的badge
    var lessBadge = chatList[index].unreadCount
    websocket.lessBadge(lessBadge)

    //减少列表用户的badge
    chatList[index].unreadCount = 0
    this.setData({
      chatList: chatList
    })

    wx.navigateTo({
      url: '/pages/chat/chatForm/chatForm?id=' + chatId,
    })

  },
  onPullDownRefresh: function() {
    console.log("上拉刷新")
    this.setData({
      chatList: [],
      offsetTime: null,
      size: 10,
    })
    this.onShow()
    setTimeout(function callback() {
      wx.stopPullDownRefresh()
    }, 500)


  },
  onReachBottom: function() {
    console.log("拉到底")
    let chatList = this.data.chatList;
    let offsetTime = chatList[chatList.length - 1].offsetTime;
    this.setData({
      offsetTime: offsetTime,
    })

    this.getChatList()
  },

})