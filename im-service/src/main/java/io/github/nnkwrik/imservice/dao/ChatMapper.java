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

    @Select("insert into chat (u1, u2, goods_id)\n" +
            "values (#{u1}, #{u2}, #{goodsId})")
    @SelectKey(resultType = Integer.class, before = false, keyProperty = "id", statement = "SELECT LAST_INSERT_ID()")
    void addChat(Chat chat);

    @Select("select u1,u2,goods_id from chat where id = #{id}")
    Chat getChatById(@Param("id") int id);

}
