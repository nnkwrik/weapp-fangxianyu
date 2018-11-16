package io.github.nnkwrik.goodsservice.dao;

import io.github.nnkwrik.goodsservice.model.po.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/14 22:19
 */
@Mapper
public interface CategoryMapper {

    @Select("select id,name from category where parent_id = 0 order by sort_order asc")
    List<Category> findMainCategory();

    @Select("select id,name,icon_url from category where parent_id = #{parentId} order by sort_order asc")
    List<Category> findSubCategory(@Param("parentId") int parentId);

    @Select("select id,name from category where id = #{id}")
    Category findCategoryById(@Param("id") int id);

}
