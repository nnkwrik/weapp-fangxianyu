package io.github.nnkwrik.goodsservice.controller;

import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.token.injection.JWT;
import io.github.nnkwrik.goodsservice.model.vo.CatalogVo;
import io.github.nnkwrik.goodsservice.model.vo.IndexVO;
import io.github.nnkwrik.goodsservice.model.vo.inner.CommentVo;
import io.github.nnkwrik.goodsservice.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author nnkwrik
 * @date 18/11/17 19:49
 */
@Slf4j
@RestController
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping("/index/index")
    public Response<IndexVO> index() {

        IndexVO vo = indexService.getIndex();
        log.debug("浏览首页 : 广告 = {},分类 = {}, 商品 = {}", vo.getBanner(), vo.getChannel(), vo.getIndexGoodsList());

        return Response.ok(vo);
    }

    @GetMapping("/catalog/index")
    public Response<CatalogVo> catalog() {

        CatalogVo vo = indexService.getCatalogIndex();
        log.debug("浏览分类页 : 主分类 = {}, 展示子分类 = {}", vo.getCategoryList(), vo.getCurrentCategory());

        return Response.ok(vo);
    }

    @GetMapping("/catalog/{id}")
    public Response<CatalogVo> subCatalog(@PathVariable("id") int id) {

        CatalogVo vo = indexService.getCatalogById(id);
        log.debug("筛选分类 : 获取子分类 = {}", vo.getCurrentCategory());

        return Response.ok(vo);
    }

    @PostMapping("/collect/addordelete/{goodsId}/{userHasCollect}")
    public Response collectAddOrDelete(@PathVariable("goodsId") int goodsId,
                                       @PathVariable("userHasCollect") boolean hasCollect,
                                       @JWT(required = true) JWTUser user) {
        indexService.collectAddOrDelete(goodsId, user.getOpenId(), hasCollect);
        log.info("用户【{}】添加或删除收藏商品，商品id={}，是否是添加?{}", user.getNickName(), goodsId, !hasCollect);
        return Response.ok();

    }

    @PostMapping("/comment/post/{goodsId}")
    public Response postComment(@PathVariable("goodsId") int goodsId,
                                @RequestBody CommentVo comment,
                                @JWT(required = true) JWTUser user) {
        if (StringUtils.isEmpty(user.getOpenId()) ||
                StringUtils.isEmpty(comment.getReply_user_id()) ||
                StringUtils.isEmpty(comment.getContent()) ||
                comment.getReply_comment_id() == null) {
            String msg = "用户发表评论失败，信息不完整";
            log.info(msg);
            return Response.fail(Response.COMMENT_INFO_INCOMPLETE,msg);
        }

        indexService.addComment(goodsId, user.getOpenId(), comment.getReply_comment_id(), comment.getReply_user_id(), comment.getContent());

        log.info("用户添加评论：用户id=【{}】，回复评论id=【{}】，回复内容=【{}】", user.getOpenId(),comment.getReply_comment_id(),comment.getReply_user_name());
        return Response.ok();

    }
}
