package io.github.nnkwrik.goodsservice.service;

import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.po.Region;
import io.github.nnkwrik.goodsservice.model.vo.CatalogPageVo;
import io.github.nnkwrik.goodsservice.model.vo.IndexPageVO;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/16 15:22
 */
public interface IndexService {

    IndexPageVO getIndex(int page, int size);

    List<Goods> getIndexMore(int page, int size);

    CatalogPageVo getCatalogIndex();

    List<Category> getSubCatalogById(int id);


    void addComment(int goodsId, String userId, int replyCommentId, String replyUserId, String content);

    List<Region> getRegionList(int regionId);

    List<Category> getPostCateList(int cateId);

}
