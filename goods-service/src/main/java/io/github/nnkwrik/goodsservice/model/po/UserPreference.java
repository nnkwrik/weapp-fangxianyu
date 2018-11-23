package io.github.nnkwrik.goodsservice.model.po;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/23 21:32
 */
@Data
public class UserPreference {
    private Integer id;
    private String userId;
    private Integer type;   //1:收藏，2：想要
    private Data createTime;
}
