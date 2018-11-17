package io.github.nnkwrik.goodsservice.model.po;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/17 21:52
 */
@Data
public class GoodsGallery {
    private Integer id;
    private Integer goodsId;
    private String imgUrl;
}
