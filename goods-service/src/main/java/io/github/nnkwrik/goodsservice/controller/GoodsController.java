package io.github.nnkwrik.goodsservice.controller;

import io.github.nnkwrik.goodsservice.model.vo.CategoryPageVo;
import io.github.nnkwrik.goodsservice.model.vo.GoodsDetailPageVo;
import io.github.nnkwrik.goodsservice.model.vo.ResponseVO;
import io.github.nnkwrik.goodsservice.model.vo.inner.CategoryVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.GalleryVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.GoodsDetailVo;
import io.github.nnkwrik.goodsservice.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/14 18:42
 */
@Slf4j
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 获取选定的子目录下的商品列表和同一个父目录下的兄弟目录
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/goods/category/{categoryId}")

    public ResponseVO<CategoryVo> getCategoryPage(@PathVariable("categoryId") int categoryId,
                                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                                  @RequestParam(value = "limit", defaultValue = "10") int size) {


        CategoryPageVo vo = goodsService.getGoodsAndBrotherCateById(categoryId, page, size);
        log.debug("通过分类浏览商品 : 商品={}", vo.getGoodsList());

        return ResponseVO.ok(vo);
    }

    @GetMapping("/goods/list/{categoryId}")
    public ResponseVO<CategoryVo> getGoodsByCategory(@PathVariable("categoryId") int categoryId,
                                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                                     @RequestParam(value = "limit", defaultValue = "10") int size) {
        CategoryPageVo vo = goodsService.getGoodsByCateId(categoryId, page, size);
        log.debug("通过分类浏览商品 : 商品={}", vo.getGoodsList());
        return ResponseVO.ok(vo);

    }

    @GetMapping("/goods/detail/{goodsId}")
    public ResponseVO<GoodsDetailPageVo> getGoodsDetail(@PathVariable("goodsId") int goodsId) {
        GoodsDetailVo goodsDetail = goodsService.getGoodsDetail(goodsId);
        List<GalleryVo> goodsGallery = goodsService.getGoodsGallery(goodsId);
        //TODO comment

        GoodsDetailPageVo vo = new GoodsDetailPageVo(goodsDetail, goodsGallery);
        log.debug("浏览商品详情 : {}", vo);

        return ResponseVO.ok(vo);
    }


}
