package io.github.nnkwrik.goodsservice.service.impl;

import com.github.pagehelper.PageHelper;
import io.github.nnkwrik.goodsservice.dao.UserMapper;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.po.GoodsExample;
import io.github.nnkwrik.goodsservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/27 20:37
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean userHasCollect(String userId, int goodsId) {
        return userMapper.userHasCollect(userId, goodsId);
    }

    @Override
    public void collectAddOrDelete(int goodsId, String userId, boolean hasCollect) {
        if (hasCollect) {
            userMapper.deleteUserCollect(userId, goodsId);
        } else {
            userMapper.setUserCollect(userId, goodsId);
        }
    }

    @Override
    public List<Goods> getUserCollectList(String userId, int page, int size) {
        PageHelper.startPage(page, size);
        return userMapper.getUserCollect(userId);
    }

    @Override
    public List<Goods> getUserBought(String buyerId, int page, int size) {
        PageHelper.startPage(page, size);
        return userMapper.getUserBought(buyerId);
    }

    @Override
    public List<Goods> getUserSold(String sellerId, int page, int size) {
        PageHelper.startPage(page, size);
        return userMapper.getUserSold(sellerId);

    }

    @Override
    public List<Goods> getUserPosted(String userId, int page, int size) {
        PageHelper.startPage(page, size);
        return userMapper.getUserPosted(userId);
    }


    @Override
    public LinkedHashMap<String, List<Goods>> getUserHistoryList(String userId, int page, int size) {
        PageHelper.startPage(page, size);
        List<GoodsExample> userHistoryList = userMapper.getUserHistoryList(userId);
        if (userHistoryList.size() < 1) return null;

        LinkedHashMap<String, List<Goods>> result = new LinkedHashMap<>();

        LocalDate lastDay = getDay(userHistoryList.get(0).getTime());
        List<Goods> lastValue = new ArrayList<>();

        for (GoodsExample goods : userHistoryList) {
            if (lastDay.equals(getDay(goods.getTime()))) {
                lastValue.add(goods);
            } else {
                String key = dateFormat(lastDay);
                result.put(key, lastValue);
                lastDay = getDay(goods.getTime());
                lastValue = new ArrayList<>();

                lastValue.add(goods);
            }
        }
        result.put(dateFormat(lastDay), lastValue);

        return result;
    }

    private LocalDate getDay(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private String dateFormat(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = localDate.format(formatter);
        return formattedString;
    }


}
