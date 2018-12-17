package io.github.nnkwrik.goodsservice.dao;

import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.po.GoodsExample;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/27 20:38
 */
@Mapper
public interface UserMapper {

    @Select("SELECT EXISTS(SELECT 1 FROM user_preference WHERE type = 1 and user_id = #{user_id} and goods_id = #{goods_id})")
    Boolean userHasCollect(@Param("user_id") String userId, @Param("goods_id") int goodsId);


    @Insert("insert into user_preference (goods_id, user_id, type)\n" +
            "values (#{goods_id}, #{user_id}, 1);\n")
    void setUserCollect(@Param("user_id") String userId, @Param("goods_id") int goodsId);

    @Delete("delete\n" +
            "from user_preference\n" +
            "where goods_id = #{goods_id}\n" +
            "  and user_id = #{user_id}\n" +
            "  and type = 1")
    void deleteUserCollect(@Param("user_id") String userId, @Param("goods_id") int goodsId);


    @Select("SELECT EXISTS(SELECT 1 FROM user_preference WHERE type = 2 and user_id = #{user_id} and goods_id = #{goods_id})")
    Boolean userHasWant(@Param("user_id") String userId, @Param("goods_id") int goodsId);

    @Insert("insert into user_preference (goods_id, user_id, type)\n" +
            "values (#{goods_id}, #{user_id}, 2);\n")
    void setUserWant(@Param("user_id") String userId, @Param("goods_id") int goodsId);

    @Delete("delete\n" +
            "from user_preference\n" +
            "where goods_id = #{goods_id}\n" +
            "  and user_id = #{user_id}\n" +
            "  and type = 2")
    void deleteUserWant(@Param("user_id") String userId, @Param("goods_id") int goodsId);


    @Select("select goods.id, name, primary_pic_url, price, is_selling\n" +
            "from goods\n" +
            "       inner join user_preference as u \n" +
            "         on goods.id = u.goods_id and u.user_id = #{user_id} and u.type = 1 and is_delete = false\n" +
            "order by u.create_time desc")
    List<Goods> getUserCollect(@Param("user_id") String userId);

    @Select("select id, name, primary_pic_url, price, sold_time\n" +
            "from goods\n" +
            "where buyer_id = #{buyer_id}\n" +
            "  and is_selling = 0\n" +
            "order by sold_time desc")
    List<Goods> getUserBought(@Param("buyer_id") String buyerId);

    @Select("select id, name, primary_pic_url, price, sold_time\n" +
            "from goods\n" +
            "where seller_id = #{seller_id}\n" +
            "  and is_selling = 0\n" +
            "order by sold_time desc")
    List<Goods> getUserSold(@Param("seller_id") String sellerId);

    @Select("select id, name, primary_pic_url, price, last_edit\n" +
            "from goods\n" +
            "where seller_id = #{seller_id}\n" +
            "  and is_selling = 1\n" +
            "  and is_delete = 0\n" +
            "order by last_edit desc")
    List<Goods> getUserPosted(@Param("seller_id") String sellerId);


    /**
     * 搜索用户对商品的操作历史,需要分别搜索发布和卖出
     * @param userId
     * @return
     */
    @Select("select goods.id,\n" +
            "       name,\n" +
            "       primary_pic_url,\n" +
            "       price,\n" +
            "       is_selling,\n" +
            "       post_time,\n" +
            "       sold_time,\n" +
            "       time\n" +
            "from goods\n" +
            "       inner join (select id, post_time as time from goods\n" +
            "                   UNION\n" +
            "                   select id, sold_time as time from goods where sold_time is not null) as ids\n" +
            "         on goods.id = ids.id and seller_id = #{user_id}\n" +
            "order by time desc")
    List<GoodsExample> getUserHistoryList(@Param("user_id") String userId);
}
