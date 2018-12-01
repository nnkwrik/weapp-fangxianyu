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
    // categoryFilter: false,
    // currentSortType: 'default',
    // currentSortOrder: '',
    // filterCategory: [],
    defaultKeyword: '输入关键字',
    hotKeyword: [],
    page: 1,
    size: 10,
    // currentSortType: 'id',
    // currentSortOrder: 'desc',
    // categoryId: 0
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
  getHelpKeyword: function() {
    let that = this;
    // 'https://suggest.taobao.com/sug?code=utf-8&q=a'
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
          // page: res.data.currentPage,
          // size: res.data.numsPerPage
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
  // openSortFilter: function (event) {
  //   let currentId = event.currentTarget.id;
  //   switch (currentId) {
  //     case 'categoryFilter':
  //       this.setData({
  //         'categoryFilter': !this.data.categoryFilter,
  //         'currentSortOrder': 'asc'
  //       });
  //       break;
  //     case 'priceSort':
  //       let tmpSortOrder = 'asc';
  //       if (this.data.currentSortOrder == 'asc') {
  //         tmpSortOrder = 'desc';
  //       }
  //       this.setData({
  //         'currentSortType': 'price',
  //         'currentSortOrder': tmpSortOrder,
  //         'categoryFilter': false
  //       });

  //       this.getGoodsList();
  //       break;
  //     default:
  //       //综合排序
  //       this.setData({
  //         'currentSortType': 'default',
  //         'currentSortOrder': 'desc',
  //         'categoryFilter': false
  //       });
  //       this.getGoodsList();
  //   }
  // },
  // selectCategory: function (event) {
  //   let currentIndex = event.target.dataset.categoryIndex;
  //   let filterCategory = this.data.filterCategory;
  //   let currentCategory = null;
  //   for (let key in filterCategory) {
  //     if (key == currentIndex) {
  //       filterCategory[key].selected = true;  //checked?
  //       currentCategory = filterCategory[key];
  //     } else {
  //       filterCategory[key].selected = false;
  //     }
  //   }
  //   this.setData({
  //     'filterCategory': filterCategory,
  //     'categoryFilter': false,
  //     categoryId: currentCategory.id,
  //     page: 1,
  //     goodsList: []
  //   });
  //   this.getGoodsList();
  // },
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