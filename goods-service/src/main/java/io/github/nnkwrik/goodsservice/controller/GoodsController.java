package io.github.nnkwrik.goodsservice.controller;

import io.github.nnkwrik.goodsservice.dao.CategoryMapper;
import io.github.nnkwrik.goodsservice.model.vo.CatalogVo;
import io.github.nnkwrik.goodsservice.model.vo.IndexVO;
import io.github.nnkwrik.goodsservice.model.vo.ResponseVO;
import io.github.nnkwrik.goodsservice.service.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nnkwrik
 * @date 18/11/14 18:42
 */
@Slf4j
@RestController
public class GoodsController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping("/index/index")
    public ResponseVO<IndexVO> index() {

        IndexVO vo = catalogService.getIndex();
        log.debug("浏览首页 : 广告 = {},分类 = {}, 商品 = {}", vo.getBanner(), vo.getChannel(), vo.getIndexGoodsList());

        return ResponseVO.ok(vo);
    }

    @GetMapping("/catalog/index")
    public ResponseVO<CatalogVo> catalog() {

        CatalogVo vo = catalogService.getCatalogIndex();
        log.info("浏览分类页 : 主分类 = {}, 展示子分类 = {}", vo.getCategoryList(), vo.getCurrentCategory());

        return ResponseVO.ok(vo);
    }

    @GetMapping("/catalog/{id}")
    public ResponseVO<CatalogVo> subCatalog(@PathVariable("id") int id) {

        CatalogVo vo = catalogService.getCatalogById(id);
        log.debug("筛选分类 : 获取子分类 = {}", vo.getCurrentCategory());

        return ResponseVO.ok(vo);
    }



}
