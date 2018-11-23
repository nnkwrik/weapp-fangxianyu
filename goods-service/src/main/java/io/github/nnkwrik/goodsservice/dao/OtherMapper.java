package io.github.nnkwrik.goodsservice.dao;

import io.github.nnkwrik.goodsservice.model.po.Ad;
import io.github.nnkwrik.goodsservice.model.po.Channel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/16 19:14
 */
@Mapper
public interface OtherMapper {

    @Select("select id,name,url,icon_url from channel order by sort_order asc")
    List<Channel> findChannel();

    @Select("select id, image_url, link\n" +
            "from ad\n" +
            "where enable = true\n" +
            "order by sort_order asc, `create` desc\n" +
            "limit 5")
    List<Ad> findAd();

    @Select("SELECT EXISTS(SELECT 1 FROM user_preference WHERE type = 1 and user_id = #{user_id} and goods_id = #{goods_id})")
    Boolean userHasCollect(@Param("user_id") String userId, @Param("goods_id") int goodsId);


    @Select("SELECT EXISTS(SELECT 1 FROM user_preference WHERE type = 2 and user_id = #{user_id} and goods_id = #{goods_id})")
    Boolean userHasWant(@Param("user_id") String userId, @Param("goods_id") int goodsId);

}
