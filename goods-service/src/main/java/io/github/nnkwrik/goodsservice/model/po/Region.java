package io.github.nnkwrik.goodsservice.model.po;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/26 21:17
 */
@Data
public class Region {
    private Integer id;
    private Integer parentId;
    private String name;
    private Integer type;
    private Integer agencyId;
}
