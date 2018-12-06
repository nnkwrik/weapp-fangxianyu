package io.github.nnkwrik.imservice.controller;

import io.github.nnkwrik.imservice.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    @RequestMapping("/testsend/{openId}")
//    public void testsend(@PathVariable("openId") String openId) {
//        webSocket.sendMessage(openId, Response.ok("success to send"));
//    }

    @GetMapping("/testNull")
    public String testNull() {
        return "webSocketService == null?true:false " + (webSocketService == null ? true : false);
    }


}
