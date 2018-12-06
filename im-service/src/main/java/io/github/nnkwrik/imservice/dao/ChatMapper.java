package io.github.nnkwrik.imservice.dao;

import io.github.nnkwrik.imservice.model.po.Chat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

/**
 * @author nnkwrik
 * @date 18/12/05 21:54
 */
@Mapper
public interface ChatMapper {
//    @Select("select id, u1, u2, unread_count\n" +
//            "from chat_user\n" +
//            "where ((u1 = #{open_id} and u1_to_u2 = false) or (u2 = #{open_id} and u1_to_u2 = true))\n" +
//            "  and unread_count > 0")
//    Chat getUserUnreadChat(@Param("open_id") String openId);

//    @Select("select unread_count from chat_user where u1=#{u1} and u2=#{u2}")
//    Integer getUnreadCountByChat(@Param("u1") String u1Id, @Param("u2") String u2Id);


//    Integer getUnreadCount(@Param("u1") String u1Id, @Param("u2") String u2Id);

    @Select("select id\n" +
            "from chat\n" +
            "where u1 = #{u1}\n" +
            "  and u2 = #{u2}\n" +
            "  and goods_id = #{goods_id}")
    Integer getChatId(@Param("u1") String u1, @Param("u2") String u2, @Param("goods_id") int goodsId);


    @Select("insert into chat (u1, u2, goods_id)\n" +
            "values (#{u1}, #{u2}, #{goodsId})")
    @SelectKey(resultType = Integer.class, before = false, keyProperty = "id", statement = "SELECT LAST_INSERT_ID()")
    void addChat(Chat chat);
}
