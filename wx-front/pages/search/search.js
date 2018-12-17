var util = require('../../utils/util.js');
var api = require('../../config/api.js');

var app = getApp()
Page({
  data: {
    keywrod: '',
    searchStatus: false,
    goodsList: [],
    helpKeyword: [],
    historyKeyword: [],
    defaultKeyword: '输入关键字',
    hotKeyword: [],
    page: 1,
    size: 10,
  },
  //事件处理函数
  closeSearch: function() {
    wx.navigateBack()
  },
  clearKeyword: function() {
    this.setData({
      keyword: '',
      searchStatus: false
    });
  },
  onLoad: function() {
    this.getSearchKeyword();
  },

  getSearchKeyword() {
    let that = this;
    util.request(api.SearchIndex).then(function(res) {
      if (res.errno === 0) {
        that.setData({
          historyKeyword: res.data.historyKeywordList,
          hotKeyword: res.data.hotKeywordList
        });
      }
    });
  },

  inputChange: function(e) {

    this.setData({
      keyword: e.detail.value,
      searchStatus: false
    });
    this.getHelpKeyword();
  },
  //借用淘宝的输入辅助api
  getHelpKeyword: function() {
    let that = this;
    util.request('https://suggest.taobao.com/sug', {
      code: 'utf-8',
      q: that.data.keyword
    }).then(function(res) {

      that.setData({
        helpKeyword: res.result
      });

    });
  },
  inputFocus: function() {
    this.setData({
      searchStatus: false,
      goodsList: []
    });

    if (this.data.keyword) {
      this.getHelpKeyword();
    }
  },
  clearHistory: function() {
    let that = this;
    this.setData({
      historyKeyword: []
    })

    util.request(api.SearchClearHistory)
      .then(function(res) {
        console.log('清除成功');
      });
  },
  getGoodsList: function() {
    let that = this;
    util.request(api.SearchResult + '/' + that.data.keyword, {
      page: this.data.page,
      size: this.data.size
    }).then(function(res) {
      if (res.errno === 0) {
        that.setData({
          searchStatus: true,
          categoryFilter: false,
          goodsList: that.data.goodsList.concat(res.data),
        });
      }

      //重新获取关键词
      that.getSearchKeyword();
    });
  },
  onKeywordTap: function(event) {

    this.getSearchResult(event.target.dataset.keyword);

  },
  getSearchResult(keyword) {
    this.setData({
      keyword: keyword,
      page: 1,
      categoryId: 0,
      goodsList: []
    });

    this.getGoodsList();
  },
  onKeywordConfirm(event) {
    this.getSearchResult(event.detail.value);
  },
  onPullDownRefresh: function() {
    console.log("上拉刷新")
    this.onLoad()
    setTimeout(function callback() {
      wx.stopPullDownRefresh()
    }, 500)


  },
  onReachBottom: function() {
    console.log("拉到底")
    this.setData({
      page: this.data.page + 1
    })
    this.getGoodsList()
  },
})