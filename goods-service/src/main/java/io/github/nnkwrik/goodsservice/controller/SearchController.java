package io.github.nnkwrik.goodsservice.controller;

import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.token.injection.JWT;
import io.github.nnkwrik.goodsservice.cache.SearchCache;
import io.github.nnkwrik.goodsservice.model.vo.SearchIndexPageVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.GoodsSimpleVo;
import io.github.nnkwrik.goodsservice.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * search相关的借口依赖openId,
 *
 * @author nnkwrik
 * @date 18/11/18 21:17
 */
@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {


    @Autowired
    private SearchCache searchCache;


    @Autowired
    private SearchService searchService;


    @GetMapping("/index")
    public Response<SearchIndexPageVo> searchIndex(@JWT JWTUser jwtUser) {
        List<String> historyKeyword = null;
        if (jwtUser != null) {
            historyKeyword = searchService.getUserHistory(jwtUser.getOpenId());
        }

        List<String> hotKeyword = searchCache.getHot(10);
        SearchIndexPageVo vo = new SearchIndexPageVo(historyKeyword, hotKeyword);
        log.info("用户openId= 【{}】获取搜索历史和热门关键词，搜索历史 = 【{}】，热门关键词 = 【{}】", jwtUser.getOpenId(), historyKeyword, hotKeyword);

        return Response.ok(vo);
    }

    @GetMapping("/clearhistory")
    public Response clearHistory(@JWT JWTUser jwtUser) {
        if (jwtUser == null) return Response.fail(Response.OPEN_ID_IS_EMPTY, "用户id为空,请登陆后再尝试");
        searchService.clearUserHistory(jwtUser.getOpenId());
        log.info("用户openId= 【{}】清空搜索历史", jwtUser.getOpenId());
        return Response.ok();
    }

    @GetMapping("/result/{keyword}")
    public Response<List<GoodsSimpleVo>> searchGoods(@PathVariable("keyword") String keyword,
                                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                                     @RequestParam(value = "limit", defaultValue = "10") int size,
                                                     @JWT JWTUser jwtUser) {
        List<GoodsSimpleVo> goodsListVo = searchService.searchByKeyword(keyword, page, size);

        //数据库改openid的搜索历史
        String openId = null;
        if (jwtUser != null) {
            openId = jwtUser.getOpenId();
            searchService.updateUserHistory(openId, keyword);
        }
        //加入热门搜索缓存
        searchCache.add(keyword.toLowerCase());
        log.debug("用户 openid=【{}】，通过关键字【{}】搜索商品，搜索结果:{}", openId, keyword, goodsListVo);
        return Response.ok(goodsListVo);
    }


}
