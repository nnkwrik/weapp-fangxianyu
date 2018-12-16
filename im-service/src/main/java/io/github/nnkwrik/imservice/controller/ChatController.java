package io.github.nnkwrik.imservice.controller;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.token.injection.JWT;
import io.github.nnkwrik.imservice.model.vo.ChatForm;
import io.github.nnkwrik.imservice.model.vo.ChatIndex;
import io.github.nnkwrik.imservice.redis.RedisClient;
import io.github.nnkwrik.imservice.service.FormService;
import io.github.nnkwrik.imservice.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author nnkwrik
 * @date 18/12/07 13:31
 */
@RestController
@Slf4j
@RequestMapping("/chat")
public class ChatController {


    @Autowired
    private IndexService indexService;

    @Autowired
    private FormService formService;


    //打开消息一览时
    @GetMapping("/index")
    public Response<List<ChatIndex>> getChatIndex(@JWT JWTUser user,
                                                  @RequestParam(value = "offsetTime", required = false)
                                                  @DateTimeFormat(pattern = StdDateFormat.DATE_FORMAT_STR_ISO8601) Date offsetTime,
                                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        if (user == null) {
            return Response.ok(0);
        }
        if (offsetTime == null) {
            offsetTime = new Date();
        }
        List<ChatIndex> voList = indexService.showIndex(user.getOpenId(), size, offsetTime);
        log.info("展示消息一览,展示{} 条信息.用户id = {},用户昵称 = {}", voList.size(), user.getOpenId(), user.getNickName());

        return Response.ok(voList);
    }

    //打开聊天框时
    @GetMapping("/form/{chatId}")
    public Response<ChatForm> getChatForm(@PathVariable("chatId") int chatId,
                                          @JWT(required = true) JWTUser user,
                                          @RequestParam(value = "offsetTime", required = false)
                                          @DateTimeFormat(pattern = StdDateFormat.DATE_FORMAT_STR_ISO8601) Date offsetTime,
                                          @RequestParam(value = "size", defaultValue = "10") int size) {
        if (offsetTime == null) {
            offsetTime = new Date();
        }
        //offset,在聊天框的时候收到的消息个数
        ChatForm vo = formService.showForm(chatId, user.getOpenId(), size, offsetTime);
        log.info("用户openId={}获取与用户openId={}的聊天记录,展示 {} 条记录", user.getOpenId(), vo.getOtherSide().getOpenId(), vo.getHistoryList().size());

        return Response.ok(vo);
    }

    //把所有未读设为已读, 通过ws实时阅读到消息时
    @PostMapping("/flushUnread/{chatId}")
    public Response flushUnread(@PathVariable("chatId") int chatId,
                                @JWT(required = true) JWTUser user) {
        formService.flushUnread(chatId ,user.getOpenId());
        log.info("用户openId={}chatId={}的所有未读消息设为已读", user.getOpenId(), chatId);
        return Response.ok();
    }

}
