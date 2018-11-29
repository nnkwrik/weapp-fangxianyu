package io.github.nnkwrik.goodsservice.service.impl;

import io.github.nnkwrik.goodsservice.dao.UserMapper;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.vo.inner.GoodsSimpleVo;
import io.github.nnkwrik.goodsservice.service.UserService;
import io.github.nnkwrik.goodsservice.util.PO2VO;
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
    public List<GoodsSimpleVo> getUserCollectList(String userId) {
        List<Goods> goodsList = userMapper.getUserCollect(userId);

        return PO2VO.convertList(PO2VO.goodsSimple,goodsList);
    }

    @Override
    public List<GoodsSimpleVo> getUserBought(String buyerId) {
        List<Goods> goodsList = userMapper.getUserBought(buyerId);
        return PO2VO.convertList(PO2VO.goodsSimple,goodsList);
    }

    @Override
    public List<GoodsSimpleVo> getUserSold(String sellerId) {
        List<Goods> goodsList = userMapper.getUserSold(sellerId);
        return PO2VO.convertList(PO2VO.goodsSimple,goodsList);

    }


}
