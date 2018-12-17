package io.github.nnkwrik.goodsservice.service.impl;

import com.github.pagehelper.PageHelper;
import fangxianyu.innerApi.user.UserClientHandler;
import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.common.util.BeanListUtils;
import io.github.nnkwrik.goodsservice.dao.CategoryMapper;
import io.github.nnkwrik.goodsservice.dao.GoodsMapper;
import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.po.GoodsComment;
import io.github.nnkwrik.goodsservice.model.po.GoodsGallery;
import io.github.nnkwrik.goodsservice.model.vo.CategoryPageVo;
import io.github.nnkwrik.goodsservice.model.vo.CommentVo;
import io.github.nnkwrik.goodsservice.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author nnkwrik
 * @date 18/11/17 21:15
 */
@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private UserClientHandler userClientHandler;

    @Override
    public CategoryPageVo getGoodsAndBrotherCateById(int id, int page, int size) {

        List<Category> brotherCategory = categoryMapper.findBrotherCategory(id);
        List<Goods> goodsList = getGoodsByCateId(id, page, size);
        CategoryPageVo vo = new CategoryPageVo(brotherCategory, goodsList);

        return vo;
    }

    @Override
    public List<Goods> getGoodsByCateId(int id, int page, int size) {
        //紧跟在这个方法后的第一个MyBatis 查询方法会被进行分页
        PageHelper.startPage(page, size);
        List<Goods> goodsList = goodsMapper.findSimpleGoodsByCateId(id);
        return goodsList;
    }

    @Override
    public Goods getGoodsDetail(int goodsId) {
        return goodsMapper.findDetailGoodsByGoodsId(goodsId);
    }


    @Override
    public int getSellerHistory(String sellerId) {
        return goodsMapper.getSellerHistory(sellerId);
    }


    @Override
    public List<GoodsGallery> getGoodsGallery(int goodsId) {

        return goodsMapper.findGalleryByGoodsId(goodsId);
    }

    /**
     * 获取与goodsId相关的商品
     * @param goodsId
     * @param page
     * @param size
     * @return
     */
    @Override
    public List<Goods> getGoodsRelated(int goodsId, int page, int size) {
        //获取同一子分类下
        PageHelper.startPage(page, size);
        List<Goods> simpleGoods = goodsMapper.findSimpleGoodsInSameCate(goodsId);

        //统一子分类下的数量少于10, 查找同一父分类下
        if (simpleGoods.size() < 10) {
            PageHelper.startPage(page, size);
            simpleGoods = goodsMapper.findSimpleGoodsInSameParentCate(goodsId);
        }

        return simpleGoods;
    }

    /**
     * 获取goodsId商品的评论(2级)
     * @param goodsId
     * @return
     */
    @Override
    public List<CommentVo> getGoodsComment(int goodsId) {
        List<GoodsComment> baseCommentPo = goodsMapper.findBaseComment(goodsId);
        if (baseCommentPo == null || baseCommentPo.size() <= 0) return null;

        List<CommentVo> baseComment = BeanListUtils.copyListProperties(baseCommentPo, CommentVo.class);
        Set<String> userIdSet = new HashSet<>();

        //把回复评论的评论加入baseComment
        baseComment.stream()
                .forEach(base -> {
                    userIdSet.add(base.getUserId());
                    //查找回复评论的评论
                    List<GoodsComment> replyListPo = goodsMapper.findReplyComment(base.getId());
                    List<CommentVo> replyList = BeanListUtils.copyListProperties(replyListPo, CommentVo.class);
                    replyList.stream().forEach(reply -> userIdSet.add(reply.getUserId()));
                    base.setReplyList(replyList);
                });

        //去用户服务查找评论用户的信息
        Map<String, SimpleUser> simpleUserMap
                = userClientHandler.getSimpleUserList(new ArrayList<>(userIdSet));

        //把用户信息加到评论中
        baseComment.stream().map(base -> setUser4Comment(simpleUserMap, base).getReplyList())
                .flatMap(List::stream)
                .forEach(reply -> setUser4Comment(simpleUserMap, reply));

        return baseComment;
    }

    @Override
    public void addComment(int goodsId, String userId, int replyCommentId, String replyUserId, String content) {
        goodsMapper.addComment(goodsId, userId, replyCommentId, replyUserId, content);
    }


    private CommentVo setUser4Comment(Map<String, SimpleUser> simpleUserMap, CommentVo comment) {
        SimpleUser userDTO = simpleUserMap.get(comment.getUserId());
        if (userDTO == null) {
            comment.setSimpleUser(SimpleUser.unknownUser());
        } else {
            comment.setSimpleUser(userDTO);
        }
        SimpleUser replyUserDTO = simpleUserMap.get(comment.getReplyUserId());
        if (replyUserDTO == null) {
            comment.setReplyUserName("用户不存在");
        } else {
            comment.setReplyUserName(replyUserDTO.getNickName());
        }
        return comment;
    }


}
