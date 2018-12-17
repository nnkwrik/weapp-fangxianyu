package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 用户主页
 * @author nnkwrik
 * @date 18/12/02 15:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageVo {

    /*用户基本信息*/
    private SimpleUser user;

    /*用户历史*/
    private LinkedHashMap<String, List<Goods>> userHistory;

    /*出售过几件商品*/
    private Integer soldCount;
}
