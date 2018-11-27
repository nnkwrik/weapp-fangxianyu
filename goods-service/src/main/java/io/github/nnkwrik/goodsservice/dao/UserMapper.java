package io.github.nnkwrik.goodsservice.dao;

import io.github.nnkwrik.goodsservice.model.po.Goods;
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
            "  and type = 1;")
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
            "  and type = 2;")
    void deleteUserWant(@Param("user_id") String userId, @Param("goods_id") int goodsId);


    @Select("select goods.id, name, primary_pic_url, price, is_on_sale\n" +
            "from goods\n" +
            "       inner join user_preference as u \n" +
            "         on goods.id = u.goods_id and u.user_id = #{user_id} and u.type = 1 and is_delete = false\n" +
            "order by u.create_time desc;")
    List<Goods> getUserCollect(@Param("user_id")String userId);
}
