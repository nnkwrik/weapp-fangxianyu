package io.github.nnkwrik.goodsservice.service.impl;

import com.github.pagehelper.PageHelper;
import io.github.nnkwrik.goodsservice.dao.CategoryMapper;
import io.github.nnkwrik.goodsservice.dao.GoodsMapper;
import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.po.GoodsGallery;
import io.github.nnkwrik.goodsservice.model.vo.CategoryPageVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.CategoryVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.GalleryVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.GoodsDetailVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.GoodsSimpleVo;
import io.github.nnkwrik.goodsservice.service.GoodsService;
import io.github.nnkwrik.goodsservice.util.PO2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/17 21:15
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private GoodsMapper goodsMapper;


    @Override
    public CategoryPageVo getGoodsAndBrotherCateById(int id, int page, int size) {

        List<Category> brotherCategory = categoryMapper.findBrotherCategry(id);
        List<CategoryVo> brotherCategoryVo = PO2VO.convertList(PO2VO.category, brotherCategory);
        CategoryPageVo vo = getGoodsByCateId(id, page, size);
        vo.setBrotherCategory(brotherCategoryVo);

        return vo;
    }

    @Override
    public CategoryPageVo getGoodsByCateId(int id, int page, int size) {
        //紧跟在这个方法后的第一个MyBatis 查询方法会被进行分页
        PageHelper.startPage(page, size);
        List<Goods> simpleGoods = goodsMapper.findSimpleGoodsByCateId(id);
        List<GoodsSimpleVo> goodsSimpleVo = PO2VO.convertList(PO2VO.goodsSimple, simpleGoods);

        return new CategoryPageVo(null, goodsSimpleVo);
    }

    @Override
    public GoodsDetailVo getGoodsDetail(int id) {
        Goods detailGoods = goodsMapper.findDetailGoodsByGoodsId(id);
        GoodsDetailVo goodsDetailVo = PO2VO.convert(PO2VO.goodsDetail, detailGoods);
        return goodsDetailVo;
    }

    @Override
    public List<GalleryVo> getGoodsGallery(int goodsId) {
        List<GoodsGallery> gallery = goodsMapper.findGalleryByGoodsId(goodsId);
        List<GalleryVo> galleryVo = PO2VO.convertList(PO2VO.gallery, gallery);

        return galleryVo;
    }


}
