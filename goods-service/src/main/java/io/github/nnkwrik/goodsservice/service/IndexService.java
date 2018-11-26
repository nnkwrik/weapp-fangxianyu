package io.github.nnkwrik.goodsservice.service;

import io.github.nnkwrik.goodsservice.model.vo.CatalogVo;
import io.github.nnkwrik.goodsservice.model.vo.IndexVO;

/**
 * @author nnkwrik
 * @date 18/11/16 15:22
 */
public interface IndexService {

    IndexVO getIndex();

    CatalogVo getCatalogIndex();

    CatalogVo getCatalogById(int id);

    void collectAddOrDelete(int goodsId, String userId, boolean hasCollect);

    void addComment(int goodsId, String userId, int replyCommentId, String replyUserId, String content);

}
