package io.github.nnkwrik.imservice.controller;

import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.imservice.client.GoodsClient;
import io.github.nnkwrik.imservice.client.UserClient;
import io.github.nnkwrik.imservice.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author nnkwrik
 * @date 18/12/05 12:08
 */
@RestController
public class TestController {
//    @Autowired
//    private WebSocket webSocket;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private UserClient userClient;

//    @RequestMapping("/testsend/{openId}")
//    public void testsend(@PathVariable("openId") String openId) {
//        webSocket.sendMessage(openId, Response.ok("success to send"));
//    }

    @GetMapping("/testNull")
    public String testNull() {
        return "webSocketService == null?true:false " + (webSocketService == null ? true : false);
    }

    @GetMapping("/testGoods")
    public Response testGoods(){
        return goodsClient.getSimpleGoods(12);
    }

    @GetMapping("/testGoodsList")
    public Response testGoodsList(){
        return goodsClient.getSimpleGoodsList(Arrays.asList(13,14,16));

    }

    @GetMapping("/testUser")
    public Response testUser(){
        return userClient.getSimpleUser(1+"");
    }

    @GetMapping("/testUserList")
    public Response testUserList(){
        return userClient.getSimpleUserList(Arrays.asList("1","2"));

    }


}
