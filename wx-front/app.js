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
      this.globalData.userInfo = wx.getStorageSync('userInfo');
      this.globalData.token = wx.getStorageSync('token');
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
      avatarUrl: 'https://avatars2.githubusercontent.com/u/29662114?s=460&v=4'
    },
    token: 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdmF0YXJVcmwiOiJodHRwczovL2F2YXRhcnMyLmdpdGh1YnVzZXJjb250ZW50LmNvbS91LzI5NjYyMTE0P3M9NDYwJnY9NCIsIm9wZW5JZCI6IjEiLCJuaWNrTmFtZSI6Iua1i-ivleeUqOaItyIsImV4cCI6MTU0MzEyNzQ4OH0.ObcfDrCIgtY6AuqKqwZd_r8HnmjzHH_GsLA7161nBzUJeg2gvJ9xhMNniOKu0BmMI4dXn8TYyBLkee9y1xphLHCyQZwqgsLYKZFTfbcmSbpDAXjknA8qIsVAGpp5srChB6LoM1GDWiIRetJ-kBn0WopypMNck8kLFe6gdw7NyoafP0Wzrryf7DWAL56TlSOZ_IcubavcF58wX2oOMdS8_Q9NSh9Jj5lWJKkqAm395wdbnurFjOF3PtM5OlrJt4hqKcv3Sd9C_MOFiA3svlmmYjNfhUl8E9HEJpm1b5DmwxgkzMLty1VtZliL9hn0Ius4YOXJlk7IuLgHgBfDA2OyXQ',
  }
})