package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.po.GoodsGallery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/17 21:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDetailPageVo {
    private Goods info;
    private List<GoodsGallery> gallery;

    private SimpleUser seller;
    private Integer sellerHistory;

    private List<CommentVo> comment;
    private Boolean userHasCollect;
}
