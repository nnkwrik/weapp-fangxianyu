package io.github.nnkwrik.goodsservice.model.vo.inner;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/16 19:05
 */
@Data
public class GoodsSimpleVo {
    private Integer id;
    private String name;
    private String list_pic_url;
    private Double price;
    private Boolean is_selling;
    private String sold_time;
}
