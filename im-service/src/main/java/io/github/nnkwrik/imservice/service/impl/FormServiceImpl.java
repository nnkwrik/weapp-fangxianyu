package io.github.nnkwrik.imservice.service.impl;

import com.github.pagehelper.PageHelper;
import fangxianyu.innerApi.goods.GoodsClientHandler;
import fangxianyu.innerApi.user.UserClientHandler;
import io.github.nnkwrik.imservice.dao.ChatMapper;
import io.github.nnkwrik.imservice.dao.HistoryMapper;
import io.github.nnkwrik.imservice.model.po.Chat;
import io.github.nnkwrik.imservice.model.po.History;
import io.github.nnkwrik.imservice.model.vo.ChatForm;
import io.github.nnkwrik.imservice.service.FormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/12/07 22:39
 */
@Service
@Slf4j
public class FormServiceImpl implements FormService {


    @Autowired
    private HistoryMapper historyMapper;

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private UserClientHandler userClientHandler;

    @Autowired
    private GoodsClientHandler goodsClientHandler;


    @Override
    public ChatForm showForm(int chatId, String userId, int page, int size, int offset) {
        ChatForm vo = new ChatForm();

        Chat chat = chatMapper.getChatById(chatId);

        //TODO 异步调用拿future
        if (chat.getU1().equals(userId)) {
            vo.setOtherSide(userClientHandler.getSimpleUser(chat.getU2()));
            vo.setIsU1(true);
        } else {
            vo.setOtherSide(userClientHandler.getSimpleUser(chat.getU1()));
            vo.setIsU1(false);
        }
        vo.setGoods(goodsClientHandler.getSimpleGoods(chat.getGoodsId()));

        int pageOffset = (page - 1) * size + offset;
        PageHelper.offsetPage(pageOffset, size);
        List<History> chatHistory = historyMapper.getChatHistory(chatId);

        vo.setHistoryList(chatHistory);

        return vo;
    }


}
