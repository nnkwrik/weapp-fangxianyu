package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author nnkwrik
 * @date 18/12/02 15:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageVo {
    private SimpleUser user;
    private LinkedHashMap<String, List<Goods>> userHistory;
    private Integer soldCount;
}
