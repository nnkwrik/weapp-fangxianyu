package io.github.nnkwrik.goodsservice.model.po;

import lombok.Data;

import java.util.Date;

/**
 * @author nnkwrik
 * @date 18/12/02 14:18
 */
@Data
public class GoodsExample extends Goods {
    private Date time;
}
