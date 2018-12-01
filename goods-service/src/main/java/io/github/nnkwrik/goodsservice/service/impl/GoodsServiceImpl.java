package io.github.nnkwrik.goodsservice.service.impl;

import com.github.pagehelper.PageHelper;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.goodsservice.client.UserClient;
import io.github.nnkwrik.goodsservice.dao.CategoryMapper;
import io.github.nnkwrik.goodsservice.dao.GoodsMapper;
import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.po.GoodsComment;
import io.github.nnkwrik.goodsservice.model.po.GoodsGallery;
import io.github.nnkwrik.goodsservice.model.vo.CategoryPageVo;
import io.github.nnkwrik.goodsservice.model.vo.CommentVo;
import io.github.nnkwrik.goodsservice.service.GoodsService;
import io.github.nnkwrik.goodsservice.util.BeanListUtils;
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
    private UserClient userClient;


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
    public SimpleUser getSellerInfo(String sellerId) {
        Response<SimpleUser> response = userClient.getSimpleUser(sellerId);
        if (response.getErrno() == Response.USER_IS_NOT_EXIST) {
            log.info("没有搜索到商品卖家的相关信息");
            return null;
        }
        return response.getData();
    }

    @Override
    public int getSellerHistory(String sellerId) {
        return goodsMapper.getSellerHistory(sellerId);
    }


    @Override
    public List<GoodsGallery> getGoodsGallery(int goodsId) {

        return goodsMapper.findGalleryByGoodsId(goodsId);
    }

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

    @Override
    public void postGoods(Goods goods) {
        goodsMapper.addGoods(goods);
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
    public List<CommentVo> getGoodsComment(int goodsId) {
        List<GoodsComment> baseCommentPo = goodsMapper.findBaseComment(goodsId);
        if (baseCommentPo == null || baseCommentPo.size() <= 0) return null;

//        List<CommentVo> baseComment = createCommentVoList(baseCommentPo);
        List<CommentVo> baseComment = BeanListUtils.copyListProperties(baseCommentPo, CommentVo.class);
        Set<String> userIdSet = new HashSet<>();


        baseComment.stream()
                .map(base -> {
                    //待会通过用户服务查找评论的用户信息
                    userIdSet.add(base.getUserId());
                    //查找回复评论的评论
                    List<GoodsComment> replyListPo = goodsMapper.findReplyComment(base.getId());
                    List<CommentVo> replyList = BeanListUtils.copyListProperties(replyListPo, CommentVo.class);
                    base.setReplyList(replyList);
                    return replyList;
                })
                .flatMap(reply -> reply.stream())
                //待会通过用户服务查找回复评论的用户信息
                .forEach(reply -> userIdSet.add(reply.getUserId()));


        Map<String, SimpleUser> simpleUserMap
                = getSimpleUserList(new ArrayList<>(userIdSet));

        //加入评论中的用户信息
        baseComment.stream().map(base -> setUser4Comment(simpleUserMap, base).getReplyList())
                .flatMap(List::stream)
                .forEach(reply -> setUser4Comment(simpleUserMap, reply));

        return baseComment;
    }


    private Map<String, SimpleUser> getSimpleUserList(List<String> openIdList) {
        log.info("从用户服务查询用户的简单信息");
        Response<HashMap<String, SimpleUser>> response = userClient.getSimpleUserList(openIdList);
        if (response.getErrno() == Response.USER_IS_NOT_EXIST) {
            log.info("没有查到匹配openId的用户");
            return new HashMap<>();
        }
        return response.getData();
    }

    private CommentVo setUser4Comment(Map<String, SimpleUser> simpleUserMap, CommentVo comment) {
        SimpleUser userDTO = simpleUserMap.get(comment.getUserId());
        if (userDTO == null) {
            comment.setSimpleUser(unknownUser());
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

    private SimpleUser unknownUser() {
        SimpleUser unknownUser = new SimpleUser();
        unknownUser.setNickName("用户不存在");
        unknownUser.setAvatarUrl("https://i.postimg.cc/RVbDV5fN/anonymous.png");
        return unknownUser;
    }

}
