package io.github.nnkwrik.goodsservice.controller;

import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.token.injection.JWT;
import io.github.nnkwrik.goodsservice.model.vo.inner.GoodsSimpleVo;
import io.github.nnkwrik.goodsservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/collect/addordelete/{goodsId}/{userHasCollect}")
    public Response collectAddOrDelete(@PathVariable("goodsId") int goodsId,
                                       @PathVariable("userHasCollect") boolean hasCollect,
                                       @JWT(required = true) JWTUser user) {
        userService.collectAddOrDelete(goodsId, user.getOpenId(), hasCollect);
        log.info("用户【{}】添加或删除收藏商品，商品id={}，是否是添加?{}", user.getNickName(), goodsId, !hasCollect);
        return Response.ok();

    }


    @GetMapping("/collect/list")
    public Response getCollectList(@JWT(required = true) JWTUser user) {
        List<GoodsSimpleVo> vo = userService.getUserCollectList(user.getOpenId());
        log.info("用户【{}】查询收藏的商品,总数:{}", user.getNickName(), vo.size());
        return Response.ok(vo);
    }

    @GetMapping("goods/bought")
    public Response getUserBought(@JWT(required = true) JWTUser user) {
        List<GoodsSimpleVo> vo = userService.getUserBought(user.getOpenId());
        log.info("用户【{}】查询买过的商品,总数:{}", user.getNickName(), vo.size());
        return Response.ok(vo);
    }

    @GetMapping("goods/sold")
    public Response getUserSold(@JWT(required = true) JWTUser user) {
        List<GoodsSimpleVo> vo = userService.getUserSold(user.getOpenId());
        log.info("用户【{}】查询卖出的商品,总数:{}", user.getNickName(), vo.size());
        return Response.ok(vo);
    }

    @GetMapping("goods/posted")
    public Response getUserPosted(@JWT(required = true) JWTUser user) {
        List<GoodsSimpleVo> vo = userService.getUserPosted(user.getOpenId());
        log.info("用户【{}】查询发布的商品,总数:{}", user.getNickName(), vo.size());
        return Response.ok(vo);
    }
}
