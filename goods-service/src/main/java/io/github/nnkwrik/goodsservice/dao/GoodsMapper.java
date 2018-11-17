package io.github.nnkwrik.goodsservice.dao;

import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.po.GoodsGallery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/16 19:15
 */
@Mapper
public interface GoodsMapper {

    @Select("select id, `name`, primary_pic_url, price\n" +
            "from goods\n" +
            "where is_on_sale = 1 and is_delete = 0\n" +
            "order by browse_count desc, last_edit desc")
    List<Goods> findSimpleGoods();

    @Select("select id, `name`, primary_pic_url, price\n" +
            "from goods\n" +
            "where category_id = #{cateId}\n" +
            "and is_on_sale = 1 and is_delete = 0\n" +
            "order by browse_count desc, last_edit desc")
    List<Goods> findSimpleGoodsByCateId(@Param("cateId") int cateId);

    @Select("select id,\n" +
            "       seller_id,\n" +
            "       `name`,\n" +
            "       price,\n" +
            "       market_price,\n" +
            "       primary_pic_url,\n" +
            "       `desc`,\n" +
            "       want_count,\n" +
            "       browse_count,\n" +
            "       last_edit\n" +
            "from goods\n" +
            "where id = #{goodsId}")
    Goods findDetailGoodsByGoodsId(@Param("goodsId") int goodId);

    @Select("select id, img_url\n" +
            "from goods_gallery\n" +
            "where goods_id = #{goodsId}")
    List<GoodsGallery> findGalleryByGoodsId(@Param("goodsId") int goodId);

}
