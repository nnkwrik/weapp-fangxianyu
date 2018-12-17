package io.github.nnkwrik.goodsservice.controller;

import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.vo.CatalogPageVo;
import io.github.nnkwrik.goodsservice.model.vo.IndexPageVo;
import io.github.nnkwrik.goodsservice.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页和分类页api
 *
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
    public Response<IndexPageVo> index(@RequestParam(value = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "size", defaultValue = "10") int size) {

        IndexPageVo vo = indexService.getIndex(page, size);
        log.info("浏览首页 : 展示{}个广告, {}个分类, {}个商品", vo.getBanner().size(), vo.getChannel().size(), vo.getIndexGoodsList().size());

        return Response.ok(vo);
    }

    /**
     * 首页展示更多推荐商品
     *
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


}
