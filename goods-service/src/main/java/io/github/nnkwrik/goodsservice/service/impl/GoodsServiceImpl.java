package io.github.nnkwrik.goodsservice.service.impl;

import com.github.pagehelper.PageHelper;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.goodsservice.client.UserClient;
import io.github.nnkwrik.goodsservice.dao.CategoryMapper;
import io.github.nnkwrik.goodsservice.dao.GoodsMapper;
import io.github.nnkwrik.goodsservice.dao.OtherMapper;
import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.po.GoodsComment;
import io.github.nnkwrik.goodsservice.model.po.GoodsGallery;
import io.github.nnkwrik.goodsservice.model.vo.CategoryPageVo;
import io.github.nnkwrik.goodsservice.model.vo.GoodsRelatedVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.*;
import io.github.nnkwrik.goodsservice.service.GoodsService;
import io.github.nnkwrik.goodsservice.util.PO2VO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.midi.Soundbank;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private OtherMapper otherMapper;


    @Override
    public CategoryPageVo getGoodsAndBrotherCateById(int id, int page, int size) {

        List<Category> brotherCategory = categoryMapper.findBrotherCategory(id);
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

        Response<SimpleUser> response = userClient.getSimpleUser(detailGoods.getSellerId());
        if (response.getErrno() == Response.USER_IS_NOT_EXIST) {
            log.info("没有搜索到商品卖家的相关信息");
            return null;
        }
        goodsDetailVo.setSeller(response.getData());

        return goodsDetailVo;
    }

    @Override
    public List<GalleryVo> getGoodsGallery(int goodsId) {
        List<GoodsGallery> gallery = goodsMapper.findGalleryByGoodsId(goodsId);
        List<GalleryVo> galleryVo = PO2VO.convertList(PO2VO.gallery, gallery);

        return galleryVo;
    }

    @Override
    public GoodsRelatedVo getGoodsRelated(int goodsId) {
        //获取同一子分类下
        int pageNum = 1;
        int pageSize = 10;
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> simpleGoods = goodsMapper.findSimpleGoodsInSameCate(goodsId);

        //统一子分类下的数量少于10, 查找同一父分类下
        if (simpleGoods.size() < 10) {
            PageHelper.startPage(pageNum, pageSize);
            simpleGoods = goodsMapper.findSimpleGoodsInSameParentCate(goodsId);
        }

        List<GoodsSimpleVo> goodsSimpleVo = PO2VO.convertList(PO2VO.goodsSimple, simpleGoods);
        return new GoodsRelatedVo(goodsSimpleVo);
    }

    @Override
    public Boolean userHasCollect(String userId, int goodsId) {
        return otherMapper.userHasCollect(userId, goodsId);
    }

    @Override
    public List<CommentVo> getGoodsComment(int goodsId) {
        List<GoodsComment> baseComment = goodsMapper.findBaseComment(goodsId);
        if (baseComment == null || baseComment.size() <= 0) return null;
        Set<String> userIdSet = new HashSet<>();
        List<CommentVo> voList = baseComment.stream()
                .map(base -> {
                    CommentVo baseVo = PO2VO.convert(PO2VO.comment, base);
                    userIdSet.add(baseVo.getUser_id());
                    return baseVo;
                }).map(baseVo -> {
                    //查找回复评论的评论
                    List<GoodsComment> replyComment = goodsMapper.findReplyComment(baseVo.getId());
                    List<CommentVo> replyCommentVo = PO2VO.convertList(PO2VO.comment, replyComment);
                    replyCommentVo.stream()
                            .forEach(reply -> userIdSet.add(reply.getUser_id()));
                    baseVo.setReplyList(replyCommentVo);
                    return baseVo;
                })
                .collect(Collectors.toList());

        Map<String, SimpleUser> simpleUserMap
                = getSimpleUserList(userIdSet.stream().collect(Collectors.toList()));

        voList.stream().map(base -> setUser4Comment(simpleUserMap, base).getReplyList())
                .flatMap(List::stream)
                .forEach(reply -> setUser4Comment(simpleUserMap, reply));

        return voList;
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
        SimpleUser userDTO = simpleUserMap.get(comment.getUser_id());
        if (userDTO == null) {
            comment.setSimpleUser(unknownUser());
        } else {
            comment.setSimpleUser(userDTO);
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
