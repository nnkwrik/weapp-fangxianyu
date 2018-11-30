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

    /**
     * 查找所有主分类
     * @return
     */
    @Select("select id,name from category where parent_id = 0 order by sort_order asc")
    List<Category> findMainCategory();

    /**
     * 查找父分类下的所有子分类
     * @param parentId
     * @return
     */
    @Select("select id,name,icon_url from category where parent_id = #{parentId} order by sort_order asc")
    List<Category> findSubCategory(@Param("parentId") int parentId);


    /**
     * 查找同一父分类下的兄弟分类
     * @param id
     * @return
     */
    @Select("select id, name\n" +
            "from category\n" +
            "where parent_id = (select parent_id from category where id = #{id})\n" +
            "order by sort_order asc")
    List<Category> findBrotherCategory(@Param("id") int id);



}
