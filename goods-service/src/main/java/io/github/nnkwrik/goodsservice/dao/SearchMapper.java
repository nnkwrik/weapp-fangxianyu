package io.github.nnkwrik.goodsservice.dao;

import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.po.SearchHistory;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/20 20:18
 */
@Mapper
public interface SearchMapper {

    /**
     * 热度算法
     * Score = (1*click + 10 * want) /e^ (day/10)
     * = (1*click + 10 * want) / e^((T(now) - T *  10^-7 )
     */
    String popular_score = "(1 * browse_count + 10 * want_count) / exp((now() - last_edit) * POW(10, -7))";


    @Select("select keyword\n" +
            "from search_history\n" +
            "where user_id = #{user_id}\n" +
            "order by search_time desc")
    List<SearchHistory> findSearchHistory(@Param("user_id") String userId);


    @Select("<script>\n" +
            "select id, `name`, primary_pic_url, price\n" +
            "from goods\n" +
            "where\n" +
            "    <foreach item='item' collection='keywords' open='(' separator='or' close=')'>\n" +
            "    name like concat(concat('%', #{item}, '%'))\n" +
            "    </foreach>\n" +
            "  and is_selling = 1\n" +
            "  and is_delete = 0\n" +
            "order by " + popular_score + " desc\n" +
            "</script>")
    List<Goods> findGoodsByKeywords(@Param("keywords") List<String> keywords);


    @Delete("delete from search_history where user_id = #{user_id}")
    void clearHistory(@Param("user_id") String userId);

    @Delete("delete\n" +
            "from search_history\n" +
            "where id in (select id from (select id from search_history where user_id = #{user_id} order by search_time asc limit #{limit}) as tmp)")
    void deleteOldHistory(@Param("user_id") String userId, @Param("limit") int limit);

    @Select("SELECT EXISTS(SELECT 1 FROM search_history WHERE user_id = #{user_id}\n" +
            "                                             and keyword = #{keyword})")
    Boolean isExistedHistory(@Param("user_id") String userId, @Param("keyword") String keyword);

    @Insert("insert into search_history (user_id, keyword) VALUES (#{user_id}, #{keyword})")
    void insertHistory(@Param("user_id") String userId, @Param("keyword") String keyword);


    @Update("update search_history\n" +
            "set search_time = now()\n" +
            "where user_id = #{user_id} and keyword = #{keyword}")
    void updateSearchTime(@Param("user_id") String userId, @Param("keyword") String keyword);


}
