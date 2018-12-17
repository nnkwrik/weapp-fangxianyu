/**
 * 用户相关服务
 */

const util = require('../utils/util.js');
const api = require('../config/api.js');
var app = getApp();



/**
 * 判断用户是否登录
 */
function checkLogin() {
  return new Promise(function(resolve, reject) {
    if (wx.getStorageSync('userInfo') && wx.getStorageSync('token')) {
      resolve(true);


    } else {
      reject(false); //没有登陆过
    }
  });
}

function checkLoginAndNav() {
  return new Promise(function(resolve, reject) {
    if (wx.getStorageSync('userInfo') && wx.getStorageSync('token')) {
      resolve(true);
    } else {
      wx.navigateTo({
        url: '/pages/auth/auth'
      })
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
        } else {
          reject(false);
        }
      }
    })
  })
}


module.exports = {
  checkLogin,
  checkUserAuth,
  checkLoginAndNav,
};