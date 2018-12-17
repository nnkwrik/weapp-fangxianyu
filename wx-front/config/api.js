// const ApiRootUrl = 'https://01bcd133.ngrok.io/';
// const WebSocktUrl = 'ws://f5763704.ngrok.io/'
const ApiRootUrl = 'http://127.0.0.1:8080/';
const WebSocktUrl = 'ws://127.0.0.1:8805/'

module.exports = {
  //首页
  IndexUrl: ApiRootUrl + 'index/index', //首页数据接口
  IndexMore: ApiRootUrl + 'index/more', //首页展示更多推荐商品

  //分类页
  CatalogList: ApiRootUrl + 'catalog/index', //分类目录全部分类数据接口
  CatalogCurrent: ApiRootUrl + 'catalog', //分类目录当前分类数据接口
  
  //分类浏览商品
  GoodsCategory: ApiRootUrl + 'goods/category/index', //获得分类数据
  GoodsList: ApiRootUrl + 'goods/category', //获得分类下的商品列表

  //浏览商品详情
  GoodsDetail: ApiRootUrl + 'goods/detail', //获得商品的详情
  GoodsRelated: ApiRootUrl + 'goods/related', //商品详情页的关联商品（大家都在看）
  CommentPost: ApiRootUrl + 'goods/comment/post', //发表评论

  //搜索
  SearchIndex: ApiRootUrl + 'search/index', //搜索页面数据
  SearchResult: ApiRootUrl + 'search/result', //搜索结果
  SearchClearHistory: ApiRootUrl + 'search/clearhistory', //清空搜索历史

  //商品发布
  GoodsPost: ApiRootUrl + 'post/post', //发布商品
  GoodsDelete: ApiRootUrl + 'post/delete', //删除自己发布的商品
  RegionList: ApiRootUrl + 'post/region', //获取区域列表
  PostCateList: ApiRootUrl + 'post/category', //发布商品时选择分类

  //用户相关
  CollectList: ApiRootUrl + 'goodsUser/collect', //收藏列表
  CollectAddOrDelete: ApiRootUrl + 'goodsUser/collect/addordelete', //添加或取消收藏
  PostedList: ApiRootUrl + 'goodsUser/posted', //发布的商品
  BoughtList: ApiRootUrl + 'goodsUser/bought', //买过的商品
  SoldList: ApiRootUrl + 'goodsUser/sold', //卖出的商品
  UserPage: ApiRootUrl + 'goodsUser/user', //用户主页
  UserPageMore: ApiRootUrl + 'goodsUser/user/more', //用户主页更多
  GoodsWant: ApiRootUrl + 'goodsUser/want', //把商品标记为想要

  //认证
  AuthLoginByWeixin: ApiRootUrl + 'auth/loginByWeixin', //微信登录

  //消息
  ChatIndex: ApiRootUrl + 'chat/index', //消息一览
  ChatForm: ApiRootUrl + 'chat/form', //消息框
  ChatFlushUnread: ApiRootUrl + 'chat/flushUnread', //把所有未读设为已读
  ChatWs: WebSocktUrl + 'ws', //消息WebSocket连接
};