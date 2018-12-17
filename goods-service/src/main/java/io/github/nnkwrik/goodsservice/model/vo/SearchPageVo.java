package io.github.nnkwrik.goodsservice.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 搜索主页
 *
 * @author nnkwrik
 * @date 18/11/18 21:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchPageVo {

    /*搜索历史*/
    private List<String> historyKeywordList;

    /*热门关键字*/
    private List<String> hotKeywordList;
}
