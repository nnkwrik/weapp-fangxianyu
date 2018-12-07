package io.github.nnkwrik.imservice.controller;

import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.token.injection.JWT;
import io.github.nnkwrik.imservice.model.po.LastChat;
import io.github.nnkwrik.imservice.model.vo.ChatIndex;
import io.github.nnkwrik.imservice.redis.RedisClient;
import io.github.nnkwrik.imservice.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    //用于打开小程序时
    @GetMapping("/chat/unreadCount")
    public Response<Integer> getUnreadCount(@JWT JWTUser user) {

        if (user == null) {
            return Response.ok(0);
        }
        List<LastChat> unreadMessage = redisClient.hvals(user.getOpenId());
        int unreadCount = unreadMessage.stream().mapToInt(LastChat::getUnreadCount).sum();
        log.info("已登录用户查询未读消息个数, {} 条未读信息.用户id = {},用户昵称 = {}", unreadCount, user.getOpenId(), user.getNickName());

        return Response.ok(unreadCount);
    }

    //打开消息一览时
    @GetMapping("/chat/index")
    public Response<List<ChatIndex>> getChatIndex(@JWT JWTUser user,
                                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        if (user == null) {
            return Response.ok(0);
        }
        List<ChatIndex> voList = indexService.showIndex(user.getOpenId(), page, size);
        log.info("展示消息一览,展示{} 条信息.用户id = {},用户昵称 = {}", voList.size(), user.getOpenId(), user.getNickName());

        return Response.ok(voList);
    }

}
