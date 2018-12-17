var app = getApp();
const api = require('../config/api.js');
const util = require('../utils/util.js');

var SocketTask
var socketOpen = false
var badge = 0


/**
 * 创建websocket连接
 */
function wsConnect() {

  let that = this
  let userInfo = wx.getStorageSync('userInfo')
  // 创建Socket
  return new Promise(function(resolve, reject) {
    if (!userInfo){
      reject("未登录")
    }
    var openId = userInfo.openId
    SocketTask = wx.connectSocket({
      url: api.ChatWs + '/' + openId,
      header: {
        'content-type': 'application/json',
        'Authorization': wx.getStorageSync('token')
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

function wsClose() {
  wx.closeSocket()
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
    // that.wsConnect()
  })
  SocketTask.onError(onError => {
    console.log('监听 WebSocket 错误。错误信息', onError)
    socketOpen = false
  })
  SocketTask.onMessage(onMessage => {
    console.log('监听WebSocket接受到服务器的消息事件。服务器返回的消息', JSON.parse(onMessage.data))
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
 * 监听websocket,接收到消息时改变badge
 */
function listenBadge() {
  wx.onSocketMessage(onMessage => {
    var res = JSON.parse(onMessage.data)
    if (res.errno === 0) {
      if (res.data.messageType == 4 && res.data.messageBody != 0) {
        badge = res.data.messageBody
        wx.setTabBarBadge({
          index: 3,
          text: badge + ""
        })

        console.log("初始化未读数badge : " + badge)
      } else if (res.data.messageType == 1) {
        badge++
        wx.setTabBarBadge({
          index: 3,
          text: badge + ""
        })
        console.log("接收到新消息,更新badge : " + badge)
      }
    } else if (res.errno == 3002 || res.errno == 3003 || res.errno == 3004 || res.errno == 3005) {
      console.log(res.errmsg)
      //TOKEN_IS_EMPTY
      //需要登录后才可以操作
      wx.getSetting({
        success: res => {
          if (res.authSetting['scope.userInfo']) {
            // // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
            util.getUserInfo().then((res) => {
              util.backendLogin(res).then((res) => {
                wsConnect()
                console.log('再次请求')
              })
            })

          }
        }
      })
    }else {
      console.log(res)
      reject(res)
    }
  })
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
          util.request(api.ChatFlushUnread + '/' + chatId, {}, "POST").then((res) => {
            if (res.errno == 0) {
              console.log("把实时获取的消息设为已读")

            } else {
              console.log(res)
            }
          })
          resolve(res.data)
        }
      }  else {
        console.log(res)
        reject(res)
      }
    })
  })
}

/**
 * 消息列表页监听消息
 */
function listenChatIndex() {
  return new Promise(function(resolve, reject) {
    wx.onSocketMessage(onMessage => {
      var res = JSON.parse(onMessage.data)
      if (res.errno === 0) {
          console.log("消息列表监听到新消息 : " + res.data.messageBody)
          badge++
          wx.setTabBarBadge({
            index: 3,
            text: badge + ""
          })
          resolve(res.data)
      } else {
        console.log(res)
        reject(res)
      }
    })
  })
}


function lessBadge(less) {
  badge = badge - less
  if (badge <= 0) {
    badge = 0
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
  wsClose,
};