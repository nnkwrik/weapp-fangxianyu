package io.github.nnkwrik.userservice.dao;

import io.github.nnkwrik.userservice.model.User;
import org.apache.ibatis.annotations.*;

/**
 * @author nnkwrik
 * @date 18/11/11 16:57
 */
@Mapper
public interface UserMapper {

    @Insert("insert into user (openId,nickName,avatarUrl,gender,language,city,province,country) " +
            "values (#{openId},#{nickName},#{avatarUrl},#{gender}," +
            "#{language},#{city},#{province},#{country})")
//    @SelectKey(resultType = Integer.class, before = false, keyProperty = "id", statement = "SELECT LAST_INSERT_ID()")
    void register(User user);
}
