package io.github.nnkwrik.goodsservice.controller;

import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.token.injection.JWT;
import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.po.GoodsComment;
import io.github.nnkwrik.goodsservice.model.po.Region;
import io.github.nnkwrik.goodsservice.model.vo.CatalogPageVo;
import io.github.nnkwrik.goodsservice.model.vo.IndexPageVO;
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

    /**
     * 展示首页,包括:广告,首页分类,首页商品
     *
     * @return
     */
    @GetMapping("/index/index")
    public Response<IndexPageVO> index(@RequestParam(value = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "size", defaultValue = "10") int size) {

        IndexPageVO vo = indexService.getIndex(page, size);
        log.info("浏览首页 : 展示{}个广告, {}个分类, {}个商品", vo.getBanner().size(), vo.getChannel().size(), vo.getIndexGoodsList().size());

        return Response.ok(vo);
    }

    /**
     * 首页展示更多推荐商品
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/index/more")
    public Response<List<Goods>> indexMore(@RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        List<Goods> goodsList = indexService.getIndexMore(page, size);
        log.info("首页获取更多推荐商品 : 返回{}个商品", goodsList.size());
        return Response.ok(goodsList);
    }

    /**
     * 分类页,展示:所有主分类,排名第一的主分类包含的子分类
     *
     * @return
     */
    @GetMapping("/catalog/index")
    public Response<CatalogPageVo> catalog() {

        CatalogPageVo vo = indexService.getCatalogIndex();
        log.info("浏览分类页 : 展示{}个主分类, 展示{}个子分类", vo.getAllCategory().size(), vo.getSubCategory().size());

        return Response.ok(vo);
    }

    /**
     * 分类页, 获取选定主分类下的子分类
     *
     * @param id
     * @return
     */
    @GetMapping("/catalog/{id}")
    public Response<List<Category>> subCatalog(@PathVariable("id") int id) {

        List<Category> subCatalog = indexService.getSubCatalogById(id);
        log.info("浏览分类页,筛选分类 : 展示{}个子分类", subCatalog.size());

        return Response.ok(subCatalog);
    }

    /**
     * 发表评论
     *
     * @param goodsId
     * @param comment
     * @param user
     * @return
     */
    @PostMapping("/comment/post/{goodsId}")
    public Response postComment(@PathVariable("goodsId") int goodsId,
                                @RequestBody GoodsComment comment,
                                @JWT(required = true) JWTUser user) {
        if (StringUtils.isEmpty(user.getOpenId()) ||
                StringUtils.isEmpty(comment.getReplyUserId()) ||
                StringUtils.isEmpty(comment.getContent()) ||
                comment.getReplyCommentId() == null) {
            String msg = "用户发表评论失败，信息不完整";
            log.info(msg);
            return Response.fail(Response.COMMENT_INFO_INCOMPLETE, msg);
        }

        indexService.addComment(goodsId, user.getOpenId(), comment.getReplyCommentId(), comment.getReplyUserId(), comment.getContent());

        log.info("用户添加评论：用户昵称=【{}】，回复评论id=【{}】，回复内容=【{}】", user.getNickName(), comment.getReplyCommentId(), comment.getContent());
        return Response.ok();

    }

    /**
     * 获取发布商品时需要填选的发货地区
     *
     * @param regionId
     * @return
     */
    @GetMapping("/region/list/{regionId}")
    public Response getRegionList(@PathVariable("regionId") int regionId) {
        List<Region> regionList = indexService.getRegionList(regionId);
        log.info("通过地区id=【{}】，搜索地区子列表。搜索到{}个结果", regionId, regionList.size());
        return Response.ok(regionList);

    }

    /**
     * 获取发布商品时需要填选的分类
     *
     * @param cateId
     * @return
     */
    @GetMapping("/category/post/{cateId}")
    public Response getPostCateList(@PathVariable("cateId") int cateId) {
        List<Category> cateList = indexService.getPostCateList(cateId);
        log.info("通过分类id=【{}】，搜索分类子列表。搜索到{}个结果", cateId, cateList.size());
        return Response.ok(cateList);
    }


}
