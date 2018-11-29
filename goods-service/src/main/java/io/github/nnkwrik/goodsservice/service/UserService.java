package io.github.nnkwrik.goodsservice.service;

import io.github.nnkwrik.goodsservice.model.vo.inner.GoodsSimpleVo;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/27 20:37
 */
public interface UserService {

    Boolean userHasCollect(String userId, int goodsId);

    void collectAddOrDelete(int goodsId, String userId, boolean hasCollect);

    List<GoodsSimpleVo> getUserCollectList(String userId);

    List<GoodsSimpleVo> getUserBought(String buyerId);

    List<GoodsSimpleVo> getUserSold(String sellerId);
}
