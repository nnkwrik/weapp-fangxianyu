package io.github.nnkwrik.goodsservice.controller;

import io.github.nnkwrik.goodsservice.model.vo.CatalogVo;
import io.github.nnkwrik.goodsservice.model.vo.IndexVO;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.goodsservice.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nnkwrik
 * @date 18/11/17 19:49
 */
@Slf4j
@RestController
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping("/index/index")
    public Response<IndexVO> index() {

        IndexVO vo = indexService.getIndex();
        log.debug("浏览首页 : 广告 = {},分类 = {}, 商品 = {}", vo.getBanner(), vo.getChannel(), vo.getIndexGoodsList());

        return Response.ok(vo);
    }

    @GetMapping("/catalog/index")
    public Response<CatalogVo> catalog() {

        CatalogVo vo = indexService.getCatalogIndex();
        log.debug("浏览分类页 : 主分类 = {}, 展示子分类 = {}", vo.getCategoryList(), vo.getCurrentCategory());

        return Response.ok(vo);
    }

    @GetMapping("/catalog/{id}")
    public Response<CatalogVo> subCatalog(@PathVariable("id") int id) {

        CatalogVo vo = indexService.getCatalogById(id);
        log.debug("筛选分类 : 获取子分类 = {}", vo.getCurrentCategory());

        return Response.ok(vo);
    }
}
