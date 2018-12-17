package io.github.nnkwrik.userservice.dao;

import io.github.nnkwrik.userservice.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/11 16:57
 */
@Mapper
public interface UserMapper {


    /**
     * 注册用户：存在相同的openId时update，不存在则insert
     *
     * @param user
     */
    @Insert("insert into user (open_id, nick_name, avatar_url, gender, language, city, province, country)\n" +
            "values (#{openId},#{nickName},#{avatarUrl},#{gender},#{language},#{city},#{province},#{country})\n" +
            "on duplicate key update nick_name = #{nickName} , " +
            "                                  avatar_url = #{avatarUrl} , gender = #{gender} , language = #{language} ," +
            "                                  city = #{city} , province = #{province} , country = #{country}")
    void register(User user);


    @Select("select open_id, nick_name, avatar_url, register_time\n" +
            "from user\n" +
            "where open_id = #{openId}")
    User getSimpleUser(@Param("openId") String openId);


    @Select("<script>\n" +
            "select open_id, nick_name, avatar_url, register_time\n" +
            "from user\n" +
            "where open_id in" +
            "    <foreach item='item' collection='openIdList' open='(' separator=',' close=')'>\n" +
            "    #{item}\n" +
            "    </foreach>\n" +
            "</script>")
    List<User> getSimpleUserList(@Param("openIdList") List<String> openIdList);
}
