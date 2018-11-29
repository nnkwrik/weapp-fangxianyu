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
    private String primaryPicUrl;
    private Double price;
    private Boolean isSelling;
    private String soldTime;
    private String lastEdit;
}
