package io.github.nnkwrik.goodsservice.service;

import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.po.Region;
import io.github.nnkwrik.goodsservice.model.vo.CatalogVo;
import io.github.nnkwrik.goodsservice.model.vo.IndexVO;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/16 15:22
 */
public interface IndexService {

    IndexVO getIndex();

    CatalogVo getCatalogIndex();

    CatalogVo getCatalogById(int id);


    void addComment(int goodsId, String userId, int replyCommentId, String replyUserId, String content);

    List<Region> getRegionList(int regionId);

    List<Category> getPostCateList(int cateId);
}
