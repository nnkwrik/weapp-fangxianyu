package io.github.nnkwrik.goodsservice.model.vo.inner;

import io.github.nnkwrik.common.dto.SimpleUser;
import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/17 21:45
 */
@Data
public class GoodsDetailVo extends GoodsSimpleVo {

    private String desc;
    private Double marketPrice;
    private Integer wantCount;
    private Integer browseCount;

    private Double postage;
    private String region;

    private Boolean ableExpress;
    private Boolean ableMeet;
    private Boolean ableSelfTake;


    private SimpleUser seller;
    private Integer sellerHistory;
}
