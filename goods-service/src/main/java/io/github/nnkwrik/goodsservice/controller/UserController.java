package io.github.nnkwrik.goodsservice.controller;

import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.token.injection.JWT;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/27 20:33
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 收藏或取消收藏某个商品
     *
     * @param goodsId
     * @param hasCollect
     * @param user
     * @return
     */
    @PostMapping("/collect/addordelete/{goodsId}/{userHasCollect}")
    public Response collectAddOrDelete(@PathVariable("goodsId") int goodsId,
                                       @PathVariable("userHasCollect") boolean hasCollect,
                                       @JWT(required = true) JWTUser user) {
        userService.collectAddOrDelete(goodsId, user.getOpenId(), hasCollect);
        log.info("用户【{}】添加或删除收藏商品，商品id={}，是否是添加?{}", user.getNickName(), goodsId, !hasCollect);
        return Response.ok();

    }


    /**
     * 获取用户收藏的商品列表
     *
     * @param user
     * @return
     */
    @GetMapping("/collect/list")
    public Response<List<Goods>> getCollectList(@JWT(required = true) JWTUser user,
                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        List<Goods> vo = userService.getUserCollectList(user.getOpenId(), page, size);
        log.info("用户【{}】查询收藏的商品,总数:{}", user.getNickName(), vo.size());
        return Response.ok(vo);
    }

    /**
     * 获取用户买过的商品列表
     *
     * @param user
     * @return
     */
    @GetMapping("goods/bought")
    public Response<List<Goods>> getUserBought(@JWT(required = true) JWTUser user,
                                               @RequestParam(value = "page", defaultValue = "1") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        List<Goods> vo = userService.getUserBought(user.getOpenId(), page, size);
        log.info("用户【{}】查询买过的商品,总数:{}", user.getNickName(), vo.size());
        return Response.ok(vo);
    }

    /**
     * 获取用户卖出的商品列表
     *
     * @param user
     * @return
     */
    @GetMapping("goods/sold")
    public Response<List<Goods>> getUserSold(@JWT(required = true) JWTUser user,
                                             @RequestParam(value = "page", defaultValue = "1") int page,
                                             @RequestParam(value = "size", defaultValue = "10") int size) {
        List<Goods> vo = userService.getUserSold(user.getOpenId(), page, size);
        log.info("用户【{}】查询卖出的商品,总数:{}", user.getNickName(), vo.size());
        return Response.ok(vo);
    }

    /**
     * 获取用户发布但还没卖出的商品列表
     *
     * @param user
     * @return
     */
    @GetMapping("goods/posted")
    public Response getUserPosted(@JWT(required = true) JWTUser user,
                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        List<Goods> vo = userService.getUserPosted(user.getOpenId(), page, size);
        log.info("用户【{}】查询发布的商品,总数:{}", user.getNickName(), vo.size());
        return Response.ok(vo);
    }
}
