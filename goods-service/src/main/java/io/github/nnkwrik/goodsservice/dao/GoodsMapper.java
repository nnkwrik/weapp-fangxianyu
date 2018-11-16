package io.github.nnkwrik.goodsservice.dao;

import io.github.nnkwrik.goodsservice.model.po.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/16 19:15
 */
@Mapper
public interface GoodsMapper {

    @Select("select id, `name`, primary_pic_url, price\n" +
            "from goods\n" +
            "order by browse_count desc, last_edit desc\n" +
            "limit 10;")
    List<Goods> findSimpleGoods();


}
