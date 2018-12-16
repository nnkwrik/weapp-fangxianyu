var util = require('./utils/util.js');
var api = require('./config/api.js');
var user = require('./services/user.js');
var websocket = require('./services/websocket.js');

var SocketTask

App({
  onLaunch: function() {
    //!!生产环境专用测试数据
    wx.setStorageSync('userInfo', this.testData.userInfo);
    wx.setStorageSync('token', this.testData.token);

    // wx.setStorageSync('userInfo', null);
    // wx.setStorageSync('token', null);

    //获取用户的登录信息
    user.checkLogin().then(res => {
      console.log('app login')
      this.globalData.userInfo = wx.getStorageSync('userInfo');
      this.globalData.token = wx.getStorageSync('token');

      

    }).catch(() => {

    });


  },
  onShow:function(){
    websocket.wsConnect()
  },
  onHide: function() {
    websocket.wsClose()
  },


  globalData: {
    userInfo: {
      openId: '',
      nickName: 'Hi,游客',
      avatarUrl: 'https://i.postimg.cc/RVbDV5fN/anonymous.png'
    },
    token: '',
    websocket: {
      socketOpen: false,
      changeBadge: true,
    }


  },
  testData: {
    userInfo: {
      openId: '1',
      nickName: '测试用户1',
      avatarUrl: 'https://4.bp.blogspot.com/-gKPdnJWscyI/VCIkF3Po4DI/AAAAAAAAmjo/fAKkTMyf8hM/s170/monster01.png'
    },
    token: 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdmF0YXJVcmwiOiJodHRwczovLzQuYnAuYmxvZ3Nwb3QuY29tLy1nS1BkbkpXc2N5SS9WQ0lrRjNQbzRESS9BQUFBQUFBQW1qby9mQUtrVE15ZjhoTS9zMTcwL21vbnN0ZXIwMS5wbmciLCJvcGVuSWQiOiIxIiwibmlja05hbWUiOiLmtYvor5XnlKjmiLcxIn0.NH3ISj2Fkircr8oB_w8-lZmf3QPt2tEOPx6Xrc-Bt8HAFu1oZIYOBYaevl8PS1xoaKkf4-8TBL2Jfx5E_uSbbkYj6WD5whHoWPy264AC3qP6ddIYFPDt3w5Ya8-FEZ26he6_mTSr0ceX-rMoFl_yiBSqoU0_H4XNAewsrTK8x3ow9qBI26eQlLDxHsZE-R3pA5sUm1IQEuV-pWGFgw6STNedoWJwX9Vq_SS4LnjOjmUZxI_xH3kPT38UAb-tvL-cM1_9XioP6H0G_9v4EhfDvnKZpmVXF4_qVzPy1VL_2VbTQr2AMoIqzP_FBSsCq2l6keP_BF6cnICJmGkqY3sLqw',
  },
  post: {
    cate: {
      id: 0,
      name: ''
    },
    region: {
      id: 0,
      name: ''
    }
  }


})