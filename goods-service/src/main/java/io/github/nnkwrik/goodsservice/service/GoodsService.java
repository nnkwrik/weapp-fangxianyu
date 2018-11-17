package io.github.nnkwrik.goodsservice.service;

import io.github.nnkwrik.goodsservice.model.vo.CategoryPageVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.GalleryVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.GoodsDetailVo;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/17 21:14
 */
public interface GoodsService {

    CategoryPageVo getGoodsAndBrotherCateById(int id, int page, int size);

    CategoryPageVo getGoodsByCateId(int id, int page, int size);

    GoodsDetailVo getGoodsDetail(int id);

    List<GalleryVo> getGoodsGallery(int goodsId);
}
