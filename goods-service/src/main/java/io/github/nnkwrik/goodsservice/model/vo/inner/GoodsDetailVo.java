package io.github.nnkwrik.goodsservice.model.vo.inner;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/17 21:45
 */
@Data
public class GoodsDetailVo extends GoodsSimpleVo {

    private String goods_brief;
    private Double market_price;
    private Integer want_count;
    private Integer browse_count;
    private String last_edit;
    //TODO seller
}
