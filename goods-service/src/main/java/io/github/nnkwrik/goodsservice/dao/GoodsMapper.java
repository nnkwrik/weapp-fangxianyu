package io.github.nnkwrik.goodsservice.dao;

import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.po.GoodsGallery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/16 19:15
 */
@Mapper
public interface GoodsMapper {

    /**
     * 热度算法
     * Score = (1*click + 10 * want) /e^ (day/10)
     * = (1*click + 10 * want) / e^((T(now) - T *  10^-7 )
     */
    String popular_score = "(1 * browse_count + 10 * want_count) / exp((now() - last_edit) * POW(10, -7))";


    /**
     * 列出所有简单商品信息
     *
     * @return
     */
    @Select("select id, `name`, primary_pic_url, price\n" +
            "from goods\n" +
            "where is_on_sale = 1 and is_delete = 0\n" +
            "order by " + popular_score + " desc")
    List<Goods> findSimpleGoods();

    /**
     * 根据分类id列出分类下的所有简单商品信息
     *
     * @return
     */
    @Select("select id, `name`, primary_pic_url, price\n" +
            "from goods\n" +
            "where category_id = #{cateId}\n" +
            "and is_on_sale = 1 and is_delete = 0\n" +
            "order by " + popular_score + " desc")
    List<Goods> findSimpleGoodsByCateId(@Param("cateId") int cateId);

    /**
     * 通过商品id查找商品的详细信息
     *
     * @param goodsId
     * @return
     */
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
    Goods findDetailGoodsByGoodsId(@Param("goodsId") int goodsId);

    /**
     * 通过商品id查找该商品关联的图片
     *
     * @param goodsId
     * @return
     */
    @Select("select id, img_url\n" +
            "from goods_gallery\n" +
            "where goods_id = #{goodsId}")
    List<GoodsGallery> findGalleryByGoodsId(@Param("goodsId") int goodsId);


    /**
     * 查找与该商品位于同一个子分类的简单商品信息
     *
     * @param goodsId
     * @return
     */
    @Select("select id, `name`, primary_pic_url, price\n" +
            "from goods\n" +
            "where category_id = (select category_id from goods where id = #{goodsId})\n" +
            "  and id != #{goodsId}\n" +
            "  and is_on_sale = 1\n" +
            "  and is_delete = 0\n" +
            "order by " + popular_score + " desc")
    List<Goods> findSimpleGoodsInSameCate(@Param("goodsId") int goodsId);


    /**
     * 查找与该商品位于同一个父分类的简单商品信息
     *
     * @param goodsId
     * @return
     */
    @Select("select id, `name`, primary_pic_url, price\n" +
            "from goods\n" +
            "where category_id in (select bar.id\n" +
            "                      from goods\n" +
            "                             inner join category as foo on foo.id = goods.category_id\n" +
            "                             inner join category as bar on foo.parent_id = bar.parent_id\n" +
            "                      where goods.id = #{goodsId})\n" +
            "  and id != #{goodsId}\n" +
            "  and is_on_sale = 1\n" +
            "  and is_delete = 0\n" +
            "order by " + popular_score + " desc")
    List<Goods> findSimpleGoodsInSameParentCate(@Param("goodsId") int goodsId);

    @Update("update goods set browse_count = browse_count + #{add} where id = #{goodsId}")
    void addBrowseCount(@Param("goodsId") int id, @Param("add") int add);


}
