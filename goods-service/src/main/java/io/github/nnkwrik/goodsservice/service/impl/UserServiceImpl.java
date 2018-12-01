package io.github.nnkwrik.goodsservice.service.impl;

import com.github.pagehelper.PageHelper;
import io.github.nnkwrik.goodsservice.dao.UserMapper;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/27 20:37
 */
@Service
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


}
