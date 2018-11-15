package io.github.nnkwrik.goodsservice.model.po;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/14 22:20
 */
@Data
public class Channel{
    private Integer id;
    private String name;
    private String url;
    private String iconUrl;
    private Integer sortOrder;
}
