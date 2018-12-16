var app = getApp();
var WxParse = require('../../lib/wxParse/wxParse.js');
var util = require('../../utils/util.js');
var api = require('../../config/api.js');
var user = require('../../services/user.js');

Page({
  data: {
    id: 0,
    goods: {},
    gallery: [],
    comment: [],
    isSeller: false,
    seller: {},
    sellerDates: 0,
    sellerHistory: 0,
    relatedGoods: [],
    cartGoodsCount: 0,
    userHasCollect: 0,

    openComment: false,
    openDelete: false,
    showInput: false,
    replyId: '',
    replyUserId: '',
    replyUserName: '',
    page: 1,
    size: 10,
    commentContent: '',
    onLoadOption: {},
    noCollectImage: "/static/images/detail_star.png",
    hasCollectImage: "/static/images/detail_star_checked.png",
    collectBackImage: "/static/images/detail_star.png"
  },
  getGoodsInfo: function() {
    let that = this;
    util.request(api.GoodsDetail + '/' + that.data.id).then(function(res) {
      if (res.errno === 0) {
        console.log(res.data)
        if (res.data.info.isDelete) {
          util.showErrorToast('商品不存在')
          setTimeout(function callback() {

            wx.navigateBack({
              delta: 1
            })
          }, 1000)
          return
        }
        
        //计算卖家来平台第几天
        let registerTime = res.data.seller.registerTime
        registerTime = registerTime.replace('T', ' ').replace(/-/g, '/').split(".")[0];
        let duration = new Date().getTime() - new Date(registerTime).getTime();
        let dates = parseInt(Math.floor(duration) / (1000 * 60 * 60 * 24));
        console.log("dates " + dates)
        that.setData({
          goods: res.data.info,
          gallery: res.data.gallery,
          seller: res.data.seller,
          sellerHistory: res.data.sellerHistory,
          sellerDates: dates,
          comment: res.data.comment,
          userHasCollect: res.data.userHasCollect
        });

        if (res.data.userHasCollect == 1) {
          that.setData({
            'collectBackImage': that.data.hasCollectImage
          });
        } else {
          that.setData({
            'collectBackImage': that.data.noCollectImage
          });
        }

        if (that.data.seller.openId == wx.getStorageSync('userInfo').openId) {
          console.log("当前用户是卖家")
          that.setData({
            isSeller: true
          });
        }

        WxParse.wxParse('goodsDetail', 'html', res.data.info.goods_desc, that);

        that.getGoodsRelated();
      }
    });

  },
  getGoodsRelated: function() {
    let that = this;
    util.request(api.GoodsRelated + '/' + that.data.id, {
      page: this.data.page,
      size: this.data.size
    }).then(function(res) {
      if (res.errno === 0) {
        that.setData({
          relatedGoods: that.data.relatedGoods.concat(res.data)
        });
      }
    });

  },
  deleteGoods: function() {
    let that = this;
    util.request(api.GoodsDelete + '/' + that.data.id, {}, 'DELETE').then(function(res) {
      if (res.errno === 0) {

        setTimeout(function goback() {
          wx.reLaunch({
            url: '/pages/index/index'
          })
        }, 1000)

        wx.showToast({
          title: '删除成功'
        })
      } else {
        console.log(res.errmsg)
      }
    });

  },

  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.setData({
      onLoadOption: options,
      id: parseInt(options.id),
      commentContent: ''

      // id: 1181000
    });

    var that = this;
    this.getGoodsInfo();

    // util.request(api.CartGoodsCount).then(function (res) {
    //   if (res.errno === 0) {
    //     that.setData({
    //       cartGoodsCount: res.data.cartTotal.goodsCount
    //     });

    //   }
    // });
  },
  onReady: function() {
    // 页面渲染完成

  },
  onShow: function() {
    // 页面显示

  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭

  },

  switchCommentPop: function(event) {
    if (event.currentTarget.dataset.disable) {
      console.log("disable")
      return
    }
    let that = this

    this.setData({
      replyId: event.currentTarget.dataset.replyId,
      replyUserId: event.currentTarget.dataset.replyUserId,
      replyUserName: event.currentTarget.dataset.replyUserName

    })

    user.checkLoginAndNav().then(() => {
      if (this.data.openComment == false) {
        this.setData({
          openComment: !this.data.openComment
        });
        let that = this
        setTimeout(function callback() {
          that.setData({
            showInput: true
          })
        }, 100)
      }
    })



  },

  switchDeletetPop: function(event) {
    let that = this
    user.checkLoginAndNav().then(() => {
      if (this.data.openDelete == false) {
        this.setData({
          openDelete: !this.data.openDelete
        });
      }
    })

  },


  closeComment: function() {
    this.setData({

      openComment: false,
    });
  },

  closeDelete: function() {
    this.setData({

      openDelete: false,
    });
  },

  postComment: function(event) {
    let that = this
    if (event.detail.value.trim() == '') {
      util.showErrorToast('请填写内容')
      return false;
    }
    util.request(api.CommentPost + '/' + this.data.id, {
      replyCommentId: this.data.replyId,
      replyUserId: this.data.replyUserId,
      content: event.detail.value
    }, "POST").then(function(res) {
      if (res.errno === 0) {
        that.setData({
          commentContent: ''
        })

        wx.showToast({
          title: '留言成功'
        })
        //刷新
        that.onLoad(that.data.onLoadOption);


      }
      console.log(res)
    });

  },

  addCannelCollect: function() {
    let that = this;
    user.checkLoginAndNav().then(() => {


      //添加或是取消收藏
      util.request(api.CollectAddOrDelete + '/' + this.data.id + '/' + this.data.userHasCollect, {}, "POST")
        .then(function(res) {
          let _res = res;
          let collectState = !that.data.userHasCollect;
          if (_res.errno == 0) {
            that.setData({
              userHasCollect: collectState
            });

            if (that.data.userHasCollect) {
              that.setData({
                'collectBackImage': that.data.hasCollectImage
              });
            } else {
              that.setData({
                'collectBackImage': that.data.noCollectImage
              });
            }

          } else {
            wx.showToast({
              image: '/static/images/icon_error.png',
              title: _res.errmsg,
              mask: true
            });
          }
        });
    })
  },
  preview: function(event) {
    let url = event.currentTarget.dataset.url
    let urls = []
    for (var i in this.data.gallery) {
      urls.push(this.data.gallery[i].imgUrl)
    }

    wx.previewImage({
      current: url,
      urls: urls // 需要预览的图片http链接列表
    })
    console.log(url)
  },
  want: function() {
    util.request(api.GoodsWant + '/' + this.data.id + '/' + this.data.seller.openId, {}, "POST")
    .then(function(res) {
      if (res.errno == 0) {
        wx.navigateTo({
          url: '/pages/chat/chatForm/chatForm?id=' + res.data,
        })
      }else{
        console.log(res)
      }
    })
  },

  onReachBottom: function() {
    console.log("拉到底")
    this.setData({
      page: this.data.page + 1
    })
    this.getGoodsRelated()
  },
})