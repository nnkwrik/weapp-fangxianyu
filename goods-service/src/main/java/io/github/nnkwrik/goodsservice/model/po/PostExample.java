package io.github.nnkwrik.goodsservice.model.po;

import lombok.Data;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/12/03 17:03
 */
@Data
public class PostExample extends Goods{
    private List<String> images;
}
