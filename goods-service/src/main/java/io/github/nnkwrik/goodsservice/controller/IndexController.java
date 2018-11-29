package io.github.nnkwrik.goodsservice.controller;

import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.token.injection.JWT;
import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.po.Region;
import io.github.nnkwrik.goodsservice.model.vo.CatalogVo;
import io.github.nnkwrik.goodsservice.model.vo.IndexVO;
import io.github.nnkwrik.goodsservice.model.vo.inner.CommentVo;
import io.github.nnkwrik.goodsservice.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/comment/post/{goodsId}")
    public Response postComment(@PathVariable("goodsId") int goodsId,
                                @RequestBody CommentVo comment,
                                @JWT(required = true) JWTUser user) {
        if (StringUtils.isEmpty(user.getOpenId()) ||
                StringUtils.isEmpty(comment.getReplyUserId()) ||
                StringUtils.isEmpty(comment.getContent()) ||
                comment.getReplyCommentId() == null) {
            String msg = "用户发表评论失败，信息不完整";
            log.info(msg);
            return Response.fail(Response.COMMENT_INFO_INCOMPLETE,msg);
        }

        indexService.addComment(goodsId, user.getOpenId(), comment.getReplyCommentId(), comment.getReplyUserId(), comment.getContent());

        log.info("用户添加评论：用户id=【{}】，回复评论id=【{}】，回复内容=【{}】", user.getOpenId(),comment.getReplyCommentId(),comment.getReplyUserName());
        return Response.ok();

    }
    @GetMapping("/region/list/{regionId}")
    public Response getRegionList(@PathVariable("regionId") int regionId){
        List<Region> regionList = indexService.getRegionList(regionId);
        log.debug("通过地区id=【{}】，搜索地区子列表。搜索结果为：{}",regionId,regionList);
        return Response.ok(regionList);

    }

    @GetMapping("/category/post/{cateId}")
    public Response getPostCateList(@PathVariable("cateId")int cateId){
        List<Category> cateList = indexService.getPostCateList(cateId);
        log.debug("通过分类id=【{}】，搜索分类子列表。搜索结果为：{}",cateId,cateList);
        return Response.ok(cateList);
    }



}
