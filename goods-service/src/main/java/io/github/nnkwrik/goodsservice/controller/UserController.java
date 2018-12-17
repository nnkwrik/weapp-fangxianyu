package io.github.nnkwrik.goodsservice.controller;

import fangxianyu.innerApi.im.ImClientHandler;
import fangxianyu.innerApi.user.UserClientHandler;
import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.common.token.injection.JWT;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.vo.UserPageVo;
import io.github.nnkwrik.goodsservice.service.GoodsService;
import io.github.nnkwrik.goodsservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 用户信息相关api
 *
 * @author nnkwrik
 * @date 18/11/27 20:33
 */
@RestController
@Slf4j
@RequestMapping("/goodsUser")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UserClientHandler userClientHandler;

    @Autowired
    private ImClientHandler imClientHandler;

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
    @GetMapping("/collect")
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
    @GetMapping("/bought")
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
    @GetMapping("/sold")
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
    @GetMapping("/posted")
    public Response getUserPosted(@JWT(required = true) JWTUser user,
                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        List<Goods> vo = userService.getUserPosted(user.getOpenId(), page, size);
        log.info("用户【{}】查询发布的商品,总数:{}", user.getNickName(), vo.size());
        return Response.ok(vo);
    }

    /**
     * 用户主页
     *
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/user/{userId}")
    public Response getUserPage(@PathVariable("userId") String userId,
                                @RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size) {
        SimpleUser user = userClientHandler.getSimpleUser(userId);
        if (user == null) {
            log.info("搜索goodsId = 【{}】的详情时出错", userId);
            return Response.fail(Response.USER_IS_NOT_EXIST, "无法搜索到商品卖家的信息");
        }

        Integer soldCount = goodsService.getSellerHistory(userId);
        LinkedHashMap<String, List<Goods>> userHistory = userService.getUserHistoryList(userId, page, size);
        UserPageVo vo = new UserPageVo(user, userHistory, soldCount);
        log.info("浏览用户id=[{}],昵称=[{}]的首页,搜索到{}天的记录", user.getOpenId(), user.getNickName(), userHistory == null ? 0 : userHistory.size());

        return Response.ok(vo);
    }

    /**
     * 用户主页,获取更多
     *
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/user/more/{userId}")
    public Response<LinkedHashMap<String, List<Goods>>> getUserPageMore(@PathVariable("userId") String userId,
                                                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                                                        @RequestParam(value = "size", defaultValue = "10") int size) {

        LinkedHashMap<String, List<Goods>> userHistory = userService.getUserHistoryList(userId, page, size);
        log.info("浏览用户id=[{}]的首页,搜索到{}天的记录", userId, userHistory.size());

        return Response.ok(userHistory);
    }

    /**
     * 标记为想要,创建chat
     *
     * @param user
     * @param goodsId
     * @param sellerId
     * @return
     */
    @PostMapping("/want/{goodsId}/{sellerId}")
    public Response<Integer> want(@JWT(required = true) JWTUser user,
                                  @PathVariable("goodsId") int goodsId,
                                  @PathVariable("sellerId") String sellerId) {

        userService.addWant(goodsId, user.getOpenId());
        //创建对话
        Integer chatId = imClientHandler.createChat(goodsId, user.getOpenId(), sellerId);
        log.info("用户id=[{}],将商品id=[{}]标记为想要.并创建对话,chatId=[{}]", user.getOpenId(), goodsId, chatId);

        return Response.ok(chatId);
    }


}
