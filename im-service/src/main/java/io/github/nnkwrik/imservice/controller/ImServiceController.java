package io.github.nnkwrik.imservice.controller;

import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.imservice.dao.ChatMapper;
import io.github.nnkwrik.imservice.model.po.Chat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nnkwrik
 * @date 18/12/08 18:51
 */
@RestController
@Slf4j
@RequestMapping("/chat-service")
public class ImServiceController {

    @Autowired
    private ChatMapper chatMapper;

    @PostMapping("/createChat/{goodsId}/{senderId}/{receiverId}")
    public Response<Integer> createChat(@PathVariable("goodsId") int goodsId,
                                        @PathVariable("senderId") String senderId,
                                        @PathVariable("receiverId") String receiverId) {
        //u1 < u2
        Chat chat = new Chat();
        chat.setGoodsId(goodsId);
        if (senderId.compareTo(receiverId) < 0) {
            chat.setU1(senderId);
            chat.setU2(receiverId);
            chat.setShowToU1(true);
            chat.setShowToU1(false);
        } else {
            chat.setU1(receiverId);
            chat.setU2(senderId);
            chat.setShowToU1(false);
            chat.setShowToU1(true);
        }
        chatMapper.addChat(chat);

        log.info("创建聊天chatId={},发起人id={},接收方id={}", chat.getId(), senderId, receiverId);
        return Response.ok(chat.getId());

    }
}
