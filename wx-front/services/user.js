/**
 * 用户相关服务
 */

const util = require('../utils/util.js');
const api = require('../config/api.js');
var app = getApp();


/**
 * 调用微信登录
 */
function loginByWeixin() {

  let code = null;
  return new Promise(function(resolve, reject) {
    return util.login().then((res) => {
      code = res.code;
      return util.getUserInfo(); //接口改了,用不了
    }).then((userInfo) => {
      //登录远程服务器
      util.request(api.AuthLoginByWeixin, {
        code: code,
        userInfo: userInfo
      }, 'POST').then(res => {
        if (res.errno === 0) {
          //存储用户信息
          wx.setStorageSync('userInfo', res.data.userInfo);
          wx.setStorageSync('token', res.data.token);

          resolve(res);
        } else {
          reject(res);
        }
      }).catch((err) => {
        reject(err);
      });
    }).catch((err) => {
      reject(err);
    })
  });
}


/**
 * 判断用户是否登录
 */
function checkLogin() {
  return new Promise(function(resolve, reject) {
    if (wx.getStorageSync('userInfo') && wx.getStorageSync('token')) {
      resolve(true);

      // util.checkSession().then(() => {
      //   resolve(true);
      // }).catch(() => {
      //   reject(false); //session过期
      // });

    } else {
      reject(false); //没有登陆过
    }
  });
}

/**
 * 判断用户是否已授权获取userInfo
 */
function checkUserAuth() {
  return new Promise(function(resolve, reject) {
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          resolve(true);
        }else{
          reject(false);
        }
      }
    })
  })
}


module.exports = {
  loginByWeixin,
  checkLogin,
  checkUserAuth,
};