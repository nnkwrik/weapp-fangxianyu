package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.po.GoodsGallery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 商品详情页
 *
 * @author nnkwrik
 * @date 18/11/17 21:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDetailPageVo {

    /*商品详情*/
    private Goods info;
    /*商品图片*/
    private List<GoodsGallery> gallery;

    /*卖家信息*/
    private SimpleUser seller;
    /*卖家出售过的商品数*/
    private Integer sellerHistory;

    /*评论*/
    private List<CommentVo> comment;
    /*用户是否收藏*/
    private Boolean userHasCollect;
}
