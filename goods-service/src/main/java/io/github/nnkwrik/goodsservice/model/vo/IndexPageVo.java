package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.goodsservice.model.po.Ad;
import io.github.nnkwrik.goodsservice.model.po.Channel;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/14 20:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexPageVo {
    private List<Goods> indexGoodsList;
    private List<Ad> banner;
    private List<Channel> channel;
}