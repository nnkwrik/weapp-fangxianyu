package io.github.nnkwrik.goodsservice.service.impl;

import com.github.pagehelper.PageHelper;
import io.github.nnkwrik.goodsservice.dao.CategoryMapper;
import io.github.nnkwrik.goodsservice.dao.GoodsMapper;
import io.github.nnkwrik.goodsservice.dao.OtherMapper;
import io.github.nnkwrik.goodsservice.model.po.*;
import io.github.nnkwrik.goodsservice.model.vo.CatalogPageVo;
import io.github.nnkwrik.goodsservice.model.vo.IndexPageVo;
import io.github.nnkwrik.goodsservice.service.IndexService;
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
    public IndexPageVo getIndex(int page, int size) {
        //广告
        List<Ad> adList = otherMapper.findAd();

        //分类
        List<Channel> channelList = otherMapper.findChannel();

        //推荐商品
        List<Goods> goodsList = getIndexMore(page,size);

        return new IndexPageVo(goodsList, adList, channelList);
    }

    @Override
    public List<Goods> getIndexMore(int page, int size) {
        PageHelper.startPage(page, size);
        return goodsMapper.findSimpleGoods();
    }

    @Override
    public CatalogPageVo getCatalogIndex() {
        List<Category> allCategory = categoryMapper.findMainCategory();
        Category topCategory = allCategory.get(0);
        List<Category> subCategory = categoryMapper.findSubCategory(topCategory.getId());

        return new CatalogPageVo(allCategory,subCategory);
    }

    @Override
    public List<Category> getSubCatalogById(int id) {
        return categoryMapper.findSubCategory(id);
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
