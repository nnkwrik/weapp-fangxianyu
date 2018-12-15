var app = getApp();
const api = require('../config/api.js');
const util = require('../utils/util.js');

var SocketTask
var socketOpen = false
var badge = 0
var newMsgList  = []


/**
 * 创建websocket连接
 */
function wsConnect() {

  let that = this
  var openId = wx.getStorageSync('userInfo').openId
  // 创建Socket
  return new Promise(function(resolve, reject) {
    SocketTask = wx.connectSocket({
      url: api.ChatWs + '/' + openId,
      data: 'data',
      header: {
        'content-type': 'application/json'
      },
      method: 'post',
      success: function(res) {
        console.log('WebSocket连接创建', res)
        listenBadge()
      },
      fail: function(err) {
        wx.showToast({
          title: '网络异常！',
        })
        console.log("err")
        reject("网络异常！")
      },
    })
    console.log(SocketTask)
    socketTask(SocketTask)

    resolve(SocketTask)
  })
}

/**
 * 监听websocket状态
 */
function socketTask(SocketTask) {
  let that = this

  SocketTask.onOpen(res => {
    socketOpen = true;
    console.log('监听 WebSocket 连接打开事件。', res)
  })
  SocketTask.onClose(onClose => {
    console.log('监听 WebSocket 连接关闭事件。', onClose)
    socketOpen = false;
    that.wsConnect()
  })
  SocketTask.onError(onError => {
    console.log('监听 WebSocket 错误。错误信息', onError)
    socketOpen = false
  })
  SocketTask.onMessage(onMessage => {
    // console.log('监听WebSocket接受到服务器的消息事件。服务器返回的消息', JSON.parse(onMessage.data))
  })
}


function sendMessage(data) {
  var that = this;
  return new Promise(function(resolve, reject) {
    if (socketOpen) {
      wx.sendSocketMessage({
        data: data,
        success: res => {
          resolve(res);
        },
        fail: res => {
          reject(res);
        }
      })
    } else {
      reject("未建立websocket连接");
    }

  })
}

/**
 * 初始化badge未读数
 */
function initBadge() {
  return new Promise(function(resolve, reject) {
    wx.onSocketMessage(onMessage => {
      var res = JSON.parse(onMessage.data)
      if (res.errno === 0) {
        if (res.data.messageType == 3 && res.data.messageBody != 0) {
          badge = res.data.messageBody
          wx.setTabBarBadge({
            index: 3,
            text: badge
          })

          resolve("初始化未读数badge : " + badge)
        }
      } else {
        console.log(res)
        reject(res)
      }
    })
  })
}
function listenBadge() {
  // return new Promise(function (resolve, reject) {
    wx.onSocketMessage(onMessage => {
      var res = JSON.parse(onMessage.data)
      if (res.errno === 0) {
        if (res.data.messageType == 3 && res.data.messageBody != 0) {
          badge = res.data.messageBody
          wx.setTabBarBadge({
            index: 3,
            text: badge+""
          })

          console.log("初始化未读数badge : " + badge)
        } else if (res.data.messageType == 1){
          badge++
          wx.setTabBarBadge({
            index: 3,
            text: badge + ""
          })
          console.log("接收到新消息,更新badge : " + badge)
        }
      } else {
        console.log(res)
        reject(res)
      }
    })
  // })
}

/**
 * 消息form页监听消息
 */
function listenChatForm(chatId) {
  return new Promise(function(resolve, reject) {
    wx.onSocketMessage(onMessage => {
      var res = JSON.parse(onMessage.data)
      if (res.errno === 0) {
        if (res.data.messageType == 1 && res.data.chatId == chatId) {
          console.log("消息Form监听到新消息 : " + res.data.messageBody)
          var newMsg = res.data
          newMsgList.push(newMsg)
          console.log(newMsgList)
          resolve(newMsg)
        }
      } else {
        console.log(res)
        reject(res)
      }
    })
  })
}

/**
 * 关闭对form页的监听,其实就是把期间收到的消息刷入数据库
 */
function stopListenForm(chatId){
  if (newMsgList.length > 0){
    newMsgList = []
    util.request(api.ChatFlushUnread + '/' + chatId, {},"POST").then((res) => {
      if(res.errno == 0){
        console.log("把unread刷入数据库成功")
        
      }else{
        console.log(res)
      }
    })
  }
}

/**
 * 消息列表页监听消息
 */
function listenChatIndex() {
  return new Promise(function(resolve, reject) {
    wx.onSocketMessage(onMessage => {
      var res = JSON.parse(onMessage.data)
      if (res.errno === 0) {
        if (res.data.messageType == 1) {
          console.log("消息列表监听到新消息 : " + res.data.messageBody)
          badge++
          wx.setTabBarBadge({
            index: 3,
            text: badge + ""
          })
          resolve(res.data)
        }
      } else {
        console.log(res)
        reject(res)
      }
    })
  })
}


function lessBadge(less) {
  badge = badge - less
  if (badge == 0) {
    wx.removeTabBarBadge({
      index: 3,
    })
  } else {
    wx.setTabBarBadge({
      index: 3,
      text: badge + ""
    })
  }

}

module.exports = {
  wsConnect,
  socketTask,
  sendMessage,
  listenChatForm,
  listenChatIndex,
  lessBadge,
  listenBadge,
  stopListenForm,
};