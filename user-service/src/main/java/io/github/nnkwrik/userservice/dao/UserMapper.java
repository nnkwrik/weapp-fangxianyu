package io.github.nnkwrik.userservice.dao;

import io.github.nnkwrik.userservice.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/11 16:57
 */
@Mapper
public interface UserMapper {

    @Insert("insert into user (open_id,nick_name,avatar_url,gender,language,city,province,country) " +
            "values (#{openId},#{nickName},#{avatarUrl},#{gender}," +
            "#{language},#{city},#{province},#{country})")
//    @SelectKey(resultType = Integer.class, before = false, keyProperty = "id", statement = "SELECT LAST_INSERT_ID()")
    void register(User user);


    @Select("select open_id, nick_name, avatar_url, register_time\n" +
            "from user\n" +
            "where open_id = #{openId}")
    User getSimpleUser(@Param("openId") String openId);

    @Select("<script>\n"+
            "select open_id, nick_name, avatar_url, register_time\n" +
            "from user\n" +
            "where open_id in" +
            "    <foreach item='item' collection='openIdList' open='(' separator=',' close=')'>\n" +
            "    #{item}\n" +
            "    </foreach>\n"+
            "</script>")
    List<User> getSimpleUserList(@Param("openIdList") List<String> openIdList);
}
