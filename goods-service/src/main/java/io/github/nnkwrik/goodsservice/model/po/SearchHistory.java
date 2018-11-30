package io.github.nnkwrik.goodsservice.model.po;

import lombok.Data;

import java.util.Date;

/**
 * @author nnkwrik
 * @date 18/11/20 18:58
 */
@Data
public class SearchHistory {
    private Integer id;
    private String userId;
    private String keyword;
    private Date searchTime;
}
