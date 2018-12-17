package io.github.nnkwrik.goodsservice.service.impl;

import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.goodsservice.dao.GoodsMapper;
import io.github.nnkwrik.goodsservice.dao.PostMapper;
import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.po.GoodsGallery;
import io.github.nnkwrik.goodsservice.model.po.PostExample;
import io.github.nnkwrik.goodsservice.model.po.Region;
import io.github.nnkwrik.goodsservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nnkwrik
 * @date 18/12/16 21:37
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private PostMapper postMapper;

    @Override
    @Transactional
    public void postGoods(PostExample post) {
        List<String> images = post.getImages();
        List<GoodsGallery> galleryList = new ArrayList<>();
        post.setPrimaryPicUrl(images.get(0)); //TODO 对PrimaryImage进行压缩

        //添加到goods表并获取id
        goodsMapper.addGoods(post);
        int goodsId = post.getId();

        //把照片添加到Gallery表
        images.stream()
                .forEach(url -> {
                    GoodsGallery gallery = new GoodsGallery();
                    gallery.setGoodsId(goodsId);
                    gallery.setImgUrl(url);
                    galleryList.add(gallery);
                });
        goodsMapper.addGalleryList(galleryList);
    }

    @Override
    public void deleteGoods(int goodsId, String userId) throws Exception {
        if (goodsMapper.validateSeller(goodsId, userId)) {
            goodsMapper.deleteGoods(goodsId);
        } else {
            throw new Exception(Response.SELLER_AND_GOODS_IS_NOT_MATCH + "");
        }
    }

    @Override
    public List<Region> getRegionList(int regionId) {
        return postMapper.getRegionByParentId(regionId);
    }

    @Override
    public List<Category> getCateList(int cateId) {
        return postMapper.getCateByParentId(cateId);
    }
}
