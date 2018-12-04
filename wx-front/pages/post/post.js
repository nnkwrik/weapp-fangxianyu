var app = getApp();
var util = require('../../utils/util.js');
var api = require('../../config/api.js');
var user = require('../../services/user.js');
Page({
  data: {
    desc: '',
    title: '',
    regionId: 0,
    region: '',
    price: '',
    marketPrice: '',
    isPostageFree: false,
    postage: null,
    ableSelfTake: false,
    ableMeet: false,
    ableExpress: false,
    cateId: 0,
    cateName: '',
    imgList: [],
    tmpImgList:[],
  },
  onLoad: function(options) {
    var that = this;
    user.checkLoginAndNav()

  },
  onClose() {
    wx.navigateBack({
      delta: 1
    });
  },
  addImage() {
    let that = this;
    let remain = 10 - this.data.imgList.length;
    console.log('上传图片')
    wx.chooseImage({
      count: remain,
      success(res) {
        let length = res.tempFiles.length

        // tempFilePath可以作为img标签的src属性显示图片        
        let tempFilePaths = res.tempFilePaths
        let tempFiles = res.tempFiles
        that.setData({
          tmpImgList: that.data.tmpImgList.concat(tempFilePaths)
        })
        for (var i = 0; i < length; i++) {
          that.data.imgList.push('false');
          var index = that.data.imgList.length-1
          that.setData({
            imgList: that.data.imgList
          })
          
          if (tempFiles[i].size > 4500000) {
            console.log("图片太大")
            that.compressImg(tempFilePaths[i], index)
          } else {
            console.log("上传到图床")
            that.uploadFile(tempFilePaths[i], index)
          }

          // console.log(tempFilePaths[i]);

        }
        console.log(that.data.imgList);

      },
      fail(res) {
        console.log(res);
      },





    })
  },
  uploadFile(url, i) {
    let that = this;
    wx.uploadFile({
      url: '	https://sm.ms/api/upload',
      filePath: url,
      name: 'smfile',
      success(res) {
        const data = JSON.parse(res.data);

        console.log(data)
        if (data.code == 'success') {
          console.log("图片上传成功, " + data.data.url)
          that.data.imgList[i] = data.data.url
          that.setData({
            imgList: that.data.imgList
          })
          // that.onLoad();

        } else if (data.code == 'error' && data.msg == 'File is too large.') {
          console.log("上传失败,图片太大")
          that.compressImg(url, i)
        }
      }
    })

    //模拟上传
    // setTimeout(function goback() {
    //   console.log("图片上传成功, " + url)
    //   that.data.imgList[i] = url
    //   that.setData({
    //     imgList: that.data.imgList
    //   })
    // }, 2000)

  },
  compressImg(url, i) {
    let that = this
    wx.compressImage({
      src: url, // 图片路径
      quality: 50, // 压缩质量
      success() {
        console.log("压缩后重新上传")
        that.uploadFile(url, i)
      },
      fail(res) {
        console.log(res)
        console.log("压缩失败 ")
      }
    })
  },
  removeImg(event){
    console.log("删除元素")
    let index = event.currentTarget.dataset.index
    let that = this
    that.data.imgList.splice(index, 1)
    that.data.tmpImgList.splice(index, 1)
    this.setData({
      imgList: that.data.imgList ,
      tmpImgList: that.data.tmpImgList,
    })

  },
  preview(event){
    let url = event.currentTarget.dataset.url
    let urls = [];
    let imgList = this.data.imgList
    for (var index in imgList){
      if (imgList[index]!='false'){
        urls.push(imgList[index])
      }
    }
    wx.previewImage({
      current: url,
      urls: urls // 需要预览的图片http链接列表
    })
  },
  onPost() {
    
    if (this.data.title.trim() == '') {
      util.showErrorToast('必须填写商品名')
      return;
    }

    if (this.data.desc.trim() == '') {
      util.showErrorToast('必须填写介绍')
      return;
    }
    if (this.data.region.trim() == '') {
      util.showErrorToast('请选择发货地点')
      return;
    }
    if (this.data.imgList.length < 1) {
      util.showErrorToast('请上传图片')
      return;
    }
    if (this.data.cateName.trim() == '') {
      util.showErrorToast('请选择分类')
      return;
    }

    let reg1 = /^[0-9]+(.[0-9]{1,})?$/;
    let reg2 = /^[0-9]+(.[0-9]{1,2})?$/;

    if (this.data.price == '') {
      util.showErrorToast('必须填写价格')
      return;
    }

    let postage = this.data.postage == null ? '0.00' : this.data.postage
    this.setData({
      marketPrice: this.data.marketPrice == '' ? '0' : this.data.marketPrice
    })

    if (!reg1.test(this.data.price) || !reg1.test(this.data.marketPrice) || !reg1.test(postage)) {
      util.showErrorToast('价格必须是数字')
      return;
    }
    if (!reg2.test(this.data.price) || !reg2.test(this.data.marketPrice) || !reg2.test(postage)) {
      util.showErrorToast('小数必须是最大2位')
      return;
    }
    if (parseFloat(this.data.price) >= 100000000 || parseFloat(this.data.marketPrice) >= 100000000) {
      util.showErrorToast("必须在0到1亿元之间")
      return;
    }
    if (parseFloat(postage) > 1000) {
      util.showErrorToast("邮费最大1千元")
      return;
    }
    if (!this.data.ableSelfTake && !this.data.ableMeet && !this.data.ableExpress) {
      util.showErrorToast("请选择交易方式")
      return;
    }

    let imgList = this.data.imgList
    for (var index in imgList) {
      if (imgList[index] == 'false') {
        util.showErrorToast('图片上传中')
        return;
      }
    }

    let that = this
    user.checkLoginAndNav().then(() => {
      util.request(api.GoodsPost, {
        name: this.data.title,
        desc: this.data.desc,
        regionId: this.data.regionId,
        region: this.data.region,
        categoryId: this.data.cateId,
        price: this.data.price,
        marketPrice: this.data.marketPrice,
        postage: postage,
        ableSelfTake: this.data.ableSelfTake,
        ableMeet: this.data.ableMeet,
        ableExpress: this.data.ableExpress,
        images: this.data.imgList,
      }, 'POST').then(function(res) {
        if (res.errno === 0) {

          setTimeout(function goback() {
            wx.reLaunch({
              url: '/pages/index/index'
            })
          }, 1000)

          wx.showToast({
            title: '发布成功'
          })
        }

        console.log(res)
      });
    })

  },
  bindInputDesc(event) {
    this.setData({
      desc: event.detail.value,
    })
    console.log(event.detail)
  },
  bindInputTitle(event) {
    this.setData({
      title: event.detail.value,
    })
    console.log(event.detail)
  },
  bindInputPrice(event) {
    this.setData({
      price: event.detail.value,
    })
    console.log(event.detail)

  },
  bindInputMarketPrice(event) {
    this.setData({
      marketPrice: event.detail.value,
    })
  },
  bindInputPostage(event) {
    this.setData({
      postage: event.detail.value,
    })
  },
  postageFree(event) {
    if (event.detail.value[0]) {
      this.setData({
        isPostageFree: true,
        postage: null
      })
    } else {
      this.setData({
        isPostageFree: false,

      })
    }
    console.log(event.detail.value[0])
  },
  trade(event) {
    this.setData({
      ableSelfTake: false,
      ableMeet: false,
      ableExpress: false,
    })
    for (let i in event.detail.value) {
      console.log(event.detail.value[i])
      if (event.detail.value[i] == 'ableSelfTake') {
        this.setData({
          ableSelfTake: true,
        })
      } else if (event.detail.value[i] == 'ableMeet') {
        this.setData({
          ableMeet: true,
        })
      } else if (event.detail.value[i] == 'ableExpress') {
        this.setData({
          ableExpress: true,
        })
      }
    };

    console.log(event.detail)
  },
  onReady: function() {

  },
  onShow: function() {
    // 页面显示
    if (app.post.region.id) {
      this.setData({
        regionId: app.post.region.id,
        region: app.post.region.name
      });
      app.post.region.id = 0;
      app.post.region.name = ''
    }

    if (app.post.cate.id) {
      this.setData({
        cateId: app.post.cate.id,
        cateName: app.post.cate.name
      });
      app.post.cate.id = 0;
      app.post.cate.name = ''
    }
  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭
    //重启
    wx.reLaunch({
      url: '/pages/index/index'
    })


  }
})