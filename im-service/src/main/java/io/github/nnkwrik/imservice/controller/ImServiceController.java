package io.github.nnkwrik.imservice.controller;

import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.imservice.constant.MessageType;
import io.github.nnkwrik.imservice.dao.ChatMapper;
import io.github.nnkwrik.imservice.dao.HistoryMapper;
import io.github.nnkwrik.imservice.model.po.Chat;
import io.github.nnkwrik.imservice.model.po.History;
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

    @Autowired
    private HistoryMapper historyMapper;

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
            chat.setShowToU2(false);
        } else {
            chat.setU1(receiverId);
            chat.setU2(senderId);
            chat.setShowToU1(false);
            chat.setShowToU2(true);
        }
        Integer chatId = chatMapper.getChatIdByChat(chat);
        if (chatId == null){
            chatMapper.addChat(chat);
            chatId = chat.getId();

            History history = new History();
            history.setChatId(chatId);
            history.setU1ToU2(senderId.compareTo(receiverId) < 0?true:false);
            history.setMessageType(MessageType.ESTABLISH_CHAT);
            historyMapper.addHistory(history);
        }


        log.info("创建聊天chatId={},发起人id={},接收方id={}", chatId, senderId, receiverId);
        return Response.ok(chatId);

    }
}
