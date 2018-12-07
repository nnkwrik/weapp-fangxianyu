package io.github.nnkwrik.imservice.service.impl;

import com.github.pagehelper.PageHelper;
import io.github.nnkwrik.imservice.dao.HistoryMapper;
import io.github.nnkwrik.imservice.model.po.History;
import io.github.nnkwrik.imservice.model.po.HistoryExample;
import io.github.nnkwrik.imservice.model.po.LastChat;
import io.github.nnkwrik.imservice.model.vo.ChatIndex;
import io.github.nnkwrik.imservice.redis.RedisClient;
import io.github.nnkwrik.imservice.service.IndexService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nnkwrik
 * @date 18/12/07 16:32
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private HistoryMapper historyMapper;

//    @Autowired
//    private UserClient userClient;


    @Override
    public List<ChatIndex> showIndex(String userId, int page, int size) {
        List<LastChat> unreadMessage = redisClient.hvals(userId);

        List<ChatIndex> resultVoList = new ArrayList<>();    //最终要返回的值
        List<Integer> goodsIds = new ArrayList<>();         //需要去商品服务查的id
        List<String> userIds = new ArrayList<>();           //需要去用户服务查的id


        int alreadyShow = size * (page - 1);
        int needShow = page * size;
        if (unreadMessage.size() >= needShow) {
            //需要展示的内容全在redis中

            List<LastChat> unread = getDisplayUnread(unreadMessage, alreadyShow, size);

            dealUnread(unread, resultVoList, goodsIds, userIds);
        } else if (unreadMessage.size() > alreadyShow) {
            //需要的内容的一部分在redis(未读),一部分在sql(已读)

            //未读消息
            List<LastChat> unread = getDisplayUnread(unreadMessage, alreadyShow, size);
            dealUnread(unread, resultVoList, goodsIds, userIds);

            List<Integer> unreadChatIds = unread.stream()
                    .map(chat -> chat.getLastMsg().getChatId())
                    .collect(Collectors.toList());

            //已读消息
            int remain = needShow - unreadMessage.size();
            PageHelper.offsetPage(0, remain);
            List<HistoryExample> read = historyMapper.getLastReadChat(unreadChatIds);
            dealRead(read,userId, resultVoList, goodsIds, userIds);


        } else {
            //需要的内容全在sql

            int offset = alreadyShow - unreadMessage.size();
            PageHelper.offsetPage(offset, size);
            List<HistoryExample> read = historyMapper.getLastChat();
            dealRead(read,userId, resultVoList, goodsIds, userIds);
        }

        //去用户服务查用户名字头像

        //去商品服务查商品图片

        //遍历赋值


        return resultVoList;
    }


    private List<LastChat> getDisplayUnread(List<LastChat> unreadMessage, int offset, int size) {
        List<LastChat> displayUnread = unreadMessage.stream()
                .sorted((a, b) -> b.getLastMsg().getSenderId().compareTo(a.getLastMsg().getSenderId()))
                .skip(offset)
                .limit(size)
                .collect(Collectors.toList());
        return displayUnread;
    }

    private void dealUnread(List<LastChat> unread,
                            List<ChatIndex> resultVoList,
                            List<Integer> goodsIds, List<String> userIds) {

        unread.stream().forEach(po -> {
            //稍后去其他服务查询
            goodsIds.add(po.getLastMsg().getGoodsId());
            userIds.add(po.getLastMsg().getSenderId());

            //设置未读数
            ChatIndex vo = new ChatIndex();
            vo.setUnreadCount(po.getUnreadCount());

            //设置最后一条信息
            History lastChat = new History();
            BeanUtils.copyProperties(po.getLastMsg(), lastChat);
            vo.setLastChat(lastChat);

            resultVoList.add(vo);
        });

    }

    private void dealRead(List<HistoryExample> read, String userId,
                          List<ChatIndex> resultVoList,
                          List<Integer> goodsIds, List<String> userIds) {

        read.stream().forEach(po -> {

            goodsIds.add(po.getGoodsId());
            if (userId.equals(po.getU1())) {
                userIds.add(po.getU2());
            } else {
                userIds.add(po.getU1());
            }

            //设置未读数
            ChatIndex vo = new ChatIndex();
            vo.setUnreadCount(0);

            //设置最后一条信息
            History lastChat = new History();
            BeanUtils.copyProperties(po, lastChat);
            vo.setLastChat(lastChat);

            resultVoList.add(vo);
        });

    }
}
