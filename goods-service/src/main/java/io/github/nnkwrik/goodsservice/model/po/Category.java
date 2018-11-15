package io.github.nnkwrik.goodsservice.model.po;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/15 16:27
 */
@Data
public class Category {
    private Integer id;
    private String name;
    private Integer parentId;
    private String iconUrl;
    private Integer sortOrder;
}
