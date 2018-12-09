package io.github.nnkwrik.imservice.dao;

import io.github.nnkwrik.imservice.model.po.Chat;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/12/05 21:54
 */
@Mapper
public interface ChatMapper {

    @Select("insert into chat (u1, u2, goods_id, show_to_u1, show_to_u2)\n" +
            "values (#{u1}, #{u2}, #{goodsId}, #{showToU1}, #{showToU2})")
    @SelectKey(resultType = Integer.class, before = false, keyProperty = "id", statement = "SELECT LAST_INSERT_ID()")
    void addChat(Chat chat);

    @Update("update chat\n" +
            "set show_to_u1 = true , show_to_u2 = true\n" +
            "where id = #{chat_id}")
    void showToBoth(@Param("chat_id") int chatId);

    @Select("select u1,u2,goods_id from chat where id = #{id}")
    Chat getChatById(@Param("id") int id);

    @Select("select id\n" +
            "from chat where (u1 = #{user_id} and show_to_u1 = true) or (u2 = #{user_id} and show_to_u2 = true)")
    List<Integer> getChatIdsByUser(@Param("user_id") String userId);

}
