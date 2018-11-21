package io.github.nnkwrik.goodsservice.controller;

import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.goodsservice.cache.SearchCache;
import io.github.nnkwrik.goodsservice.model.vo.SearchIndexPageVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.GoodsSimpleVo;
import io.github.nnkwrik.goodsservice.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * search相关的借口依赖openId,
 *
 * @author nnkwrik
 * @date 18/11/18 21:17
 */
@Slf4j
@RestController
public class SearchController {


    @Autowired
    private SearchCache searchCache;


    @Autowired
    private SearchService searchService;


    @PostMapping("/search/index")
    public Response<SearchIndexPageVo> searchIndex(@RequestBody Map<String, String> map) {
        String openId = map.get("openId");
        List<String> historyKeyword = null;
        if (!StringUtils.isEmpty(openId)) {
            historyKeyword = searchService.getUserHistory(openId);
        }

        List<String> hotKeyword = searchCache.getHot(10);
        SearchIndexPageVo vo = new SearchIndexPageVo(historyKeyword, hotKeyword);
        log.info("用户openId= 【{}】获取搜索历史和热门关键词，搜索历史 = 【{}】，热门关键词 = 【{}】", openId, historyKeyword, hotKeyword);

        return Response.ok(vo);
    }

    @PostMapping("/search/clearhistory")
    public Response clearHistory(@RequestBody Map<String, String> map) {
        String openId = map.get("openId");
        if (StringUtils.isEmpty(openId)) return Response.fail(Response.OPEN_ID_IS_EMPTY, "用户id为空,请登陆后再尝试");
        searchService.clearUserHistory(openId);
        log.info("用户openId= 【{}】清空搜索历史", openId);
        return Response.ok();
    }


    //TODO 后期提供排序和地区和卖家信用的筛选功能
    @PostMapping("search/result/{keyword}")
    public Response<List<GoodsSimpleVo>> searchGoods(@PathVariable("keyword") String keyword,
                                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                                     @RequestParam(value = "limit", defaultValue = "10") int size,
                                                     @RequestBody Map<String, String> map) {
        String openId = map.get("openId");
        List<GoodsSimpleVo> goodsListVo = searchService.searchByKeyword(keyword, page, size);

        //数据库改openid的搜索历史
        if (!StringUtils.isEmpty(openId)) {
            searchService.updateUserHistory(openId, keyword);
        }
        //加入热门搜索缓存
        searchCache.add(keyword.toLowerCase());
        log.debug("用户 openid=【{}】，通过关键字【{}】搜索商品，搜索结果:{}", openId, keyword, goodsListVo);
        return Response.ok(goodsListVo);
    }


}
