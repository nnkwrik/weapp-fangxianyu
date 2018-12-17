package io.github.nnkwrik.goodsservice.service.impl;

import com.github.pagehelper.PageHelper;
import io.github.nnkwrik.goodsservice.dao.CategoryMapper;
import io.github.nnkwrik.goodsservice.dao.GoodsMapper;
import io.github.nnkwrik.goodsservice.dao.IndexMapper;
import io.github.nnkwrik.goodsservice.model.po.Ad;
import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.po.Channel;
import io.github.nnkwrik.goodsservice.model.po.Goods;
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
    private IndexMapper indexMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public IndexPageVo getIndex(int page, int size) {
        //广告
        List<Ad> adList = indexMapper.findAd();

        //分类
        List<Channel> channelList = indexMapper.findChannel();

        //推荐商品
        List<Goods> goodsList = getIndexMore(page, size);

        return new IndexPageVo(goodsList, adList, channelList);
    }

    @Override
    public List<Goods> getIndexMore(int page, int size) {
        PageHelper.startPage(page, size);
        return goodsMapper.findSimpleGoods();
    }

    @Override
    public CatalogPageVo getCatalogIndex() {
        //所有主分类
        List<Category> allCategory = categoryMapper.findMainCategory();
        Category topCategory = allCategory.get(0);
        //第一个主分类下的所有子分类
        List<Category> subCategory = categoryMapper.findSubCategory(topCategory.getId());

        return new CatalogPageVo(allCategory, subCategory);
    }

    @Override
    public List<Category> getSubCatalogById(int id) {
        //主分类=id下的所有子分类
        return categoryMapper.findSubCategory(id);
    }


}
