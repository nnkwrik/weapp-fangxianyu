package io.github.nnkwrik.goodsservice.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/18 21:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchIndexPageVo {
    private List<String> historyKeywordList;
    private List<String> hotKeywordList;
}
