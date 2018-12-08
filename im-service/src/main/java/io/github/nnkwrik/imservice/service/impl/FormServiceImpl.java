package io.github.nnkwrik.imservice.service.impl;

import com.github.pagehelper.PageHelper;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.dto.SimpleGoods;
import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.imservice.client.GoodsClient;
import io.github.nnkwrik.imservice.client.UserClient;
import io.github.nnkwrik.imservice.dao.ChatMapper;
import io.github.nnkwrik.imservice.dao.HistoryMapper;
import io.github.nnkwrik.imservice.model.po.Chat;
import io.github.nnkwrik.imservice.model.po.History;
import io.github.nnkwrik.imservice.model.vo.ChatForm;
import io.github.nnkwrik.imservice.redis.RedisClient;
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
    private RedisClient redisClient;

    @Autowired
    private HistoryMapper historyMapper;

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private GoodsClient goodsClient;

    @Override
    public ChatForm showForm(int chatId, String userId, int page, int size, int offset) {
        ChatForm vo = new ChatForm();

        Chat chat = chatMapper.getChatById(chatId);

        //TODO 异步调用拿future
        if (chat.getU1().equals(userId)) {
            vo.setOtherSide(getSimpleUser(chat.getU2()));
            vo.setIsU1(true);
        } else {
            vo.setOtherSide(getSimpleUser(chat.getU1()));
            vo.setIsU1(false);
        }
        vo.setGoods(getSimpleGoods(chat.getGoodsId()));

        int pageOffset = (page - 1) * size + offset;
        PageHelper.offsetPage(pageOffset, size);
        List<History> chatHistory = historyMapper.getChatHistory(chatId);

        vo.setHistoryList(chatHistory);

        return vo;
    }

    //TODO 重复
    private SimpleGoods getSimpleGoods(Integer goodsId) {
        log.info("从商品服务查询商品的简单信息");
        Response<SimpleGoods> response = goodsClient.getSimpleGoods(goodsId);
        if (response.getErrno() != 0) {
            log.info("从商品服务获取商品信息列表失败,errno={},原因={}", response.getErrno(), response.getErrmsg());
            return unknownGoods();
        }
        return response.getData();
    }

    private SimpleUser getSimpleUser(String openId) {
        log.info("从用户服务查询用户的简单信息");
        Response<SimpleUser> response = userClient.getSimpleUser(openId);
        if (response.getErrno() != 0) {
            log.info("从用户服务获取用户信息列表失败,errno={},原因={}", response.getErrno(), response.getErrmsg());
            return unknownUser();
        }
        return response.getData();
    }

    private SimpleUser unknownUser() {
        SimpleUser unknownUser = new SimpleUser();
        unknownUser.setNickName("用户不存在");
        unknownUser.setAvatarUrl("https://i.postimg.cc/RVbDV5fN/anonymous.png");
        return unknownUser;
    }

    private SimpleGoods unknownGoods() {
        SimpleGoods unknownGoods = new SimpleGoods();
        unknownGoods.setName("商品不存在");
        return unknownGoods;
    }
}
