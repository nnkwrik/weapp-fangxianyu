package io.github.nnkwrik.goodsservice.dao;

import io.github.nnkwrik.goodsservice.model.po.Ad;
import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.po.Channel;
import io.github.nnkwrik.goodsservice.model.po.Region;
import org.apache.ibatis.annotations.*;

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


    @Insert("insert into goods_comment (goods_id, user_id, reply_comment_id, reply_user_id, content)\n" +
            "values (#{goods_id}, #{user_id}, #{reply_comment_id}, #{reply_user_id}, #{content})")
    void addComment(@Param("goods_id") int goodsId,
                    @Param("user_id") String userId,
                    @Param("reply_comment_id") int replyCommentId,
                    @Param("reply_user_id") String replyUserId,
                    @Param("content") String content);

    @Select("select id,name from region where parent_id=#{parent_id}")
    List<Region> getRegionByParentId(@Param("parent_id") int parentId);

    @Select("select id,name from category where parent_id=#{parent_id}")
    List<Category> getCateByParentId(@Param("parent_id") int parentId);
}
