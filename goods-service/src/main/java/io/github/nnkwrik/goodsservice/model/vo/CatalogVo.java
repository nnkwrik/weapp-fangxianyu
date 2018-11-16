package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.goodsservice.model.vo.inner.CategoryVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/15 16:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogVo {
    private List<CategoryVo> categoryList;   //所有主分类
    private  CategoryVo currentCategory;    //当前主分类的名字和它的子分类

}


