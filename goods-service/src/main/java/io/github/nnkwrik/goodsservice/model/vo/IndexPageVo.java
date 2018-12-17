package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.goodsservice.model.po.Ad;
import io.github.nnkwrik.goodsservice.model.po.Channel;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 首页
 *
 * @author nnkwrik
 * @date 18/11/14 20:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexPageVo {

    /*首页推荐商品*/
    private List<Goods> indexGoodsList;

    /*广告banner*/
    private List<Ad> banner;

    /*首页展示分类*/
    private List<Channel> channel;
}