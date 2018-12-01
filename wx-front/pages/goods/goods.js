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
    // attribute: [],
    // issueList: [],
    comment: [],
    // brand: {},
    // specificationList: [],
    // productList: [],
    seller: {},
    sellerDates: 0,
    sellerHistory: 0,
    relatedGoods: [],
    cartGoodsCount: 0,
    userHasCollect: 0,
    number: 1,
    checkedSpecText: '请选择规格数量',
    openAttr: false,
    openComment: false,
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

        //计算卖家来平台第几天
        let registerTime = res.data.seller.registerTime
        let duration = new Date().getTime() - new Date(registerTime).getTime();
        let dates = parseInt(Math.floor(duration) / (1000 * 60 * 60 * 24));
        that.setData({
          goods: res.data.info,
          gallery: res.data.gallery,
          seller: res.data.seller,
          sellerHistory: res.data.sellerHistory,
          sellerDates: dates,
          // attribute: res.data.attribute,
          // issueList: res.data.issue,
          comment: res.data.comment,
          // brand: res.data.brand,
          // specificationList: res.data.specificationList,
          // productList: res.data.productList,
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
  // clickSkuValue: function (event) {
  //   let that = this;
  //   let specNameId = event.currentTarget.dataset.nameId;
  //   let specValueId = event.currentTarget.dataset.valueId;

  //   //判断是否可以点击

  //   //TODO 性能优化，可在wx:for中添加index，可以直接获取点击的属性名和属性值，不用循环
  //   let _specificationList = this.data.specificationList;
  //   for (let i = 0; i < _specificationList.length; i++) {
  //     if (_specificationList[i].specification_id == specNameId) {
  //       for (let j = 0; j < _specificationList[i].valueList.length; j++) {
  //         if (_specificationList[i].valueList[j].id == specValueId) {
  //           //如果已经选中，则反选
  //           if (_specificationList[i].valueList[j].checked) {
  //             _specificationList[i].valueList[j].checked = false;
  //           } else {
  //             _specificationList[i].valueList[j].checked = true;
  //           }
  //         } else {
  //           _specificationList[i].valueList[j].checked = false;
  //         }
  //       }
  //     }
  //   }
  //   this.setData({
  //     'specificationList': _specificationList
  //   });
  //   //重新计算spec改变后的信息
  //   this.changeSpecInfo();

  //   //重新计算哪些值不可以点击
  // },

  //获取选中的规格信息
  // getCheckedSpecValue: function () {
  //   let checkedValues = [];
  //   let _specificationList = this.data.specificationList;
  //   for (let i = 0; i < _specificationList.length; i++) {
  //     let _checkedObj = {
  //       nameId: _specificationList[i].specification_id,
  //       valueId: 0,
  //       valueText: ''
  //     };
  //     for (let j = 0; j < _specificationList[i].valueList.length; j++) {
  //       if (_specificationList[i].valueList[j].checked) {
  //         _checkedObj.valueId = _specificationList[i].valueList[j].id;
  //         _checkedObj.valueText = _specificationList[i].valueList[j].value;
  //       }
  //     }
  //     checkedValues.push(_checkedObj);
  //   }

  //   return checkedValues;

  // },
  // //根据已选的值，计算其它值的状态
  // setSpecValueStatus: function () {

  // },
  // //判断规格是否选择完整
  // isCheckedAllSpec: function () {
  //   return !this.getCheckedSpecValue().some(function (v) {
  //     if (v.valueId == 0) {
  //       return true;
  //     }
  //   });
  // },
  // getCheckedSpecKey: function () {
  //   let checkedValue = this.getCheckedSpecValue().map(function (v) {
  //     return v.valueId;
  //   });

  //   return checkedValue.join('_');
  // },
  // changeSpecInfo: function () {
  //   let checkedNameValue = this.getCheckedSpecValue();

  //   //设置选择的信息
  //   let checkedValue = checkedNameValue.filter(function (v) {
  //     if (v.valueId != 0) {
  //       return true;
  //     } else {
  //       return false;
  //     }
  //   }).map(function (v) {
  //     return v.valueText;
  //   });
  //   if (checkedValue.length > 0) {
  //     this.setData({
  //       'checkedSpecText': checkedValue.join('　')
  //     });
  //   } else {
  //     this.setData({
  //       'checkedSpecText': '请选择规格数量'
  //     });
  //   }

  // },
  // getCheckedProductItem: function (key) {
  //   return this.data.productList.filter(function (v) {
  //     if (v.goods_specification_ids == key) {
  //       return true;
  //     } else {
  //       return false;
  //     }
  //   });
  // },
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
      }
    })

  },


  closeComment: function() {
    this.setData({

      openComment: false,
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
  // openCartPage: function () {
  //   wx.switchTab({
  //     url: '/pages/cart/cart',
  //   });
  // },
  // addToCart: function () {
  //   var that = this;
  //   if (this.data.openAttr === false) {
  //     //打开规格选择窗口
  //     this.setData({
  //       openAttr: !this.data.openAttr
  //     });
  //   } else {

  //     //提示选择完整规格
  //     if (!this.isCheckedAllSpec()) {
  //       wx.showToast({
  //         image: '/static/images/icon_error.png',
  //         title: '请选择规格',
  //         mask: true
  //       });
  //       return false;
  //     }

  //     //根据选中的规格，判断是否有对应的sku信息
  //     let checkedProduct = this.getCheckedProductItem(this.getCheckedSpecKey());
  //     if (!checkedProduct || checkedProduct.length <= 0) {
  //       //找不到对应的product信息，提示没有库存
  //       wx.showToast({
  //         image: '/static/images/icon_error.png',
  //         title: '库存不足',
  //         mask: true
  //       });
  //       return false;
  //     }

  //     //验证库存
  //     if (checkedProduct.goods_number < this.data.number) {
  //       //找不到对应的product信息，提示没有库存
  //       wx.showToast({
  //         image: '/static/images/icon_error.png',
  //         title: '库存不足',
  //         mask: true
  //       });
  //       return false;
  //     }

  //     //添加到购物车
  //     util.request(api.CartAdd, { goodsId: this.data.goods.id, number: this.data.number, productId: checkedProduct[0].id }, "POST")
  //       .then(function (res) {
  //         let _res = res;
  //         if (_res.errno == 0) {
  //           wx.showToast({
  //             title: '添加成功'
  //           });
  //           that.setData({
  //             openAttr: !that.data.openAttr,
  //             cartGoodsCount: _res.data.cartTotal.goodsCount
  //           });
  //         } else {
  //           wx.showToast({
  //             image: '/static/images/icon_error.png',
  //             title: _res.errmsg,
  //             mask: true
  //           });
  //         }

  //       });
  //   }

  // },
  // cutNumber: function () {
  //   this.setData({
  //     number: (this.data.number - 1 > 1) ? this.data.number - 1 : 1
  //   });
  // },
  // addNumber: function () {
  //   this.setData({
  //     number: this.data.number + 1
  //   });
  // }
  onReachBottom: function() {
    console.log("拉到底")
    this.setData({
      page: this.data.page + 1
    })
    this.getGoodsRelated()
  },
})