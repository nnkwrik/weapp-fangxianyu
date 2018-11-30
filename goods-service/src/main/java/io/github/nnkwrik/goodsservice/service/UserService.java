package io.github.nnkwrik.goodsservice.service;

import io.github.nnkwrik.goodsservice.model.po.Goods;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/27 20:37
 */
public interface UserService {

    Boolean userHasCollect(String userId, int goodsId);

    void collectAddOrDelete(int goodsId, String userId, boolean hasCollect);

    List<Goods> getUserCollectList(String userId);

    List<Goods> getUserBought(String buyerId);

    List<Goods> getUserSold(String sellerId);

    List<Goods> getUserPosted(String userId);
}
