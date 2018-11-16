package io.github.nnkwrik.goodsservice.model.po;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/16 20:01
 */
@Data
public class Ad {
    private Integer id;
    private String name;
    private String imageUrl;
    private String link;
    private Integer sortOrder;
    private Boolean enable;
    private Data data;
}
