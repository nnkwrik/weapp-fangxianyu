package io.github.nnkwrik.common.dto;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/12/07 15:58
 */
@Data
public class SimpleGoods {
    private Integer id;
    private String name;
    private String primaryPicUrl;
    private Double price;

    public static SimpleGoods unknownGoods() {
        SimpleGoods unknownGoods = new SimpleGoods();
        unknownGoods.setName("商品不存在");
        return unknownGoods;
    }
}
