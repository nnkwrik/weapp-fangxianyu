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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author nnkwrik
 * @date 18/12/07 13:31
 */
@RestController
@Slf4j
public class ChatController {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private IndexService indexService;

    @Autowired
    private FormService formService;

    //用于打开小程序时
    @GetMapping("/chat/unreadCount")
    public Response<Integer> getUnreadCount(@JWT JWTUser user) {

        if (user == null) {
            return Response.ok(0);
        }
        int unreadCount = indexService.getUnreadCount(user.getOpenId());
        log.info("已登录用户查询未读消息个数, {} 条未读信息.用户id = {},用户昵称 = {}", unreadCount, user.getOpenId(), user.getNickName());

        return Response.ok(unreadCount);
    }

    //打开消息一览时
    @GetMapping("/chat/index")
    public Response<List<ChatIndex>> getChatIndex(@JWT JWTUser user,
                                                  @RequestParam(value = "offsetTime", required = false)
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date offsetTime,
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
    @GetMapping("/chat/form/{chatId}")
    public Response<ChatForm> getChatForm(@PathVariable("chatId") int chatId,
                                          @JWT(required = true) JWTUser user,
                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "size", defaultValue = "10") int size,
                                          @RequestParam(value = "offset", defaultValue = "0") int offset) {
        //offset,在聊天框的时候收到的消息个数
        ChatForm vo = formService.showForm(chatId, user.getOpenId(), page, size, offset);
        log.info("用户openId={}获取与用户openId={}的聊天记录,展示 {} 条记录", user.getOpenId(), vo.getOtherSide().getOpenId(), vo.getHistoryList().size());

        return Response.ok(vo);
    }

    //把所有未读设为已读, 在退出聊天框时使用
//    @PostMapping("/chat/flushUnread/{chatId}")
//    public Response flushUnread(@PathVariable("chatId") int chatId,
//                                @JWT(required = true) JWTUser user) {
//        formService.flushUnread(user.getOpenId(), chatId );
//        log.info("用户openId={}chatId={}的所有未读消息设为已读", user.getOpenId(), chatId);
//        return Response.ok();
//    }

}
