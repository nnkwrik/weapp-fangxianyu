package io.github.nnkwrik.imservice.controller;

import io.github.nnkwrik.imservice.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nnkwrik
 * @date 18/12/05 12:08
 */
@RestController
public class TestController {
    @Autowired
    private WebSocket webSocket;

    @RequestMapping("/testsend/{openId}")
    public void testsend(@PathVariable("openId") String openId){
        webSocket.sendMessage(openId,"success to send");
    }
}
