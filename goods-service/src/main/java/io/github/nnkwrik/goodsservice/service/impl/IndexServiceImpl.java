package io.github.nnkwrik.goodsservice.service.impl;

import com.github.pagehelper.PageHelper;
import io.github.nnkwrik.goodsservice.dao.CategoryMapper;
import io.github.nnkwrik.goodsservice.dao.GoodsMapper;
import io.github.nnkwrik.goodsservice.dao.OtherMapper;
import io.github.nnkwrik.goodsservice.model.po.*;
import io.github.nnkwrik.goodsservice.model.vo.CatalogVo;
import io.github.nnkwrik.goodsservice.model.vo.IndexVO;
import io.github.nnkwrik.goodsservice.model.vo.inner.BannerVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.CategoryVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.ChannelVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.GoodsSimpleVo;
import io.github.nnkwrik.goodsservice.service.IndexService;
import io.github.nnkwrik.goodsservice.util.PO2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/16 15:23
 */
@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private OtherMapper otherMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public IndexVO getIndex() {
        //广告
        List<Ad> adList = otherMapper.findAd();
        List<BannerVo> bannerVoList = PO2VO.convertList(adList, BannerVo.class);

        //分类
        List<Channel> channelList = otherMapper.findChannel();
        List<ChannelVo> channelVoList = PO2VO.convertList(channelList, ChannelVo.class);

        //推荐商品
        int pageNum = 1;
        int pageSize = 10;
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goodsList = goodsMapper.findSimpleGoods();
        List<GoodsSimpleVo> goodsSimpleVOList = PO2VO.convertList(goodsList, GoodsSimpleVo.class);

        return new IndexVO(goodsSimpleVOList, bannerVoList, channelVoList);
    }

    @Override
    public CatalogVo getCatalogIndex() {
        List<Category> mainCategory = categoryMapper.findMainCategory();
        Category currentCate = mainCategory.get(0);
        List<Category> subCategory = categoryMapper.findSubCategory(currentCate.getId());


        List<CategoryVo> mainCategoryVo = PO2VO.convertList(mainCategory, CategoryVo.class);
        List<CategoryVo> subCategoryVo = PO2VO.convertList(subCategory, CategoryVo.class);
        CategoryVo currentCategoryVo =
                new CategoryVo(currentCate.getId(), currentCate.getName(), currentCate.getIconUrl(), subCategoryVo);

        return new CatalogVo(mainCategoryVo, currentCategoryVo);
    }

    @Override
    public CatalogVo getCatalogById(int id) {
        List<Category> subCategory = categoryMapper.findSubCategory(id);
        List<CategoryVo> subCategoryVo = PO2VO.convertList(subCategory, CategoryVo.class);
        Category currentCategory = categoryMapper.findCategoryById(id);
        CategoryVo currentCategoryVo = PO2VO.convert(currentCategory, CategoryVo.class);
        currentCategoryVo.setSubCategoryList(subCategoryVo);

        return new CatalogVo(null, currentCategoryVo);
    }

    @Override
    public void addComment(int goodsId, String userId, int replyCommentId, String replyUserId, String content) {
        otherMapper.addComment(goodsId, userId, replyCommentId, replyUserId, content);
    }

    @Override
    public List<Region> getRegionList(int regionId) {
        return otherMapper.getRegionByParentId(regionId);
    }

    @Override
    public List<Category> getPostCateList(int cateId) {
        return otherMapper.getCateByParentId(cateId);
    }


}
