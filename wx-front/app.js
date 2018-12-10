var util = require('./utils/util.js');
var api = require('./config/api.js');
var user = require('./services/user.js');

App({
  onLaunch: function () {
    //!!生产环境专用测试数据
    wx.setStorageSync('userInfo', this.testData.userInfo);
    wx.setStorageSync('token', this.testData.token);

    // wx.setStorageSync('userInfo', null);
    // wx.setStorageSync('token', null);

    //获取用户的登录信息
    // user.checkLogin().then(res => {
    //   console.log('app login')
    //   this.globalData.userInfo = wx.getStorageSync('userInfo');
    //   this.globalData.token = wx.getStorageSync('token');
    // }).catch(() => {
      
    // });
  },

  
  
  globalData: {
    userInfo: {
      openId: '',
      nickName: 'Hi,游客',
      avatarUrl: 'https://i.postimg.cc/RVbDV5fN/anonymous.png'
    },
    token: '',
  },
  testData: {
    userInfo: {
      openId: '1',
      nickName: '测试用户1',
      avatarUrl: 'https://4.bp.blogspot.com/-gKPdnJWscyI/VCIkF3Po4DI/AAAAAAAAmjo/fAKkTMyf8hM/s170/monster01.png'
    },
    token: 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdmF0YXJVcmwiOiJodHRwczovLzQuYnAuYmxvZ3Nwb3QuY29tLy1nS1BkbkpXc2N5SS9WQ0lrRjNQbzRESS9BQUFBQUFBQW1qby9mQUtrVE15ZjhoTS9zMTcwL21vbnN0ZXIwMS5wbmciLCJvcGVuSWQiOiIxIiwibmlja05hbWUiOiLmtYvor5XnlKjmiLcxIiwiZXhwIjoxNTQ0NDI1MzY1fQ.ZsuZLiaShOEFNZEeVYFtF7u568-ykyX3jXuznBgfnxo_G70TlQjALtHdkIZo8c-L6iSYGu45CN7Qwhs8_76l_C6dkwh8p-ncT1fDdVavsiTnqMY2oj_HqHR8hA2jMnB8QteH066F5IQihqcpAcFj4l_nZeBZ-zRAQNE9t3ufnD1qVCrOw9nRO7ywMunMUEqf0AGeRl3k2SWiShIRxuZsy2x5Iacu3CsLL1W9K1caA-tzydC4wrfwPwOdp1dlBNb0Fmo8jO3yR8rKojrwC61kOTnTB-YCC56cb_fUM6LiPrh6zFq6F3HSvYfXSha1s0JJom3lX4ycM5OwDcFBBh4EHQ',
  },
  post:{
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