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
public interface IndexMapper {

    @Select("select id,name,url,icon_url from channel order by sort_order asc")
    List<Channel> findChannel();

    @Select("select id, image_url, link\n" +
            "from ad\n" +
            "where enable = true\n" +
            "order by sort_order asc, `create` desc\n" +
            "limit 5")
    List<Ad> findAd();

}
