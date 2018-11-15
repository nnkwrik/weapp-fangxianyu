package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.goodsservice.model.po.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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




    //todo
    public static CatalogVo createFromPo(List<Category> subCategory){
        return createFromPo(null,subCategory);
    }

    public static CatalogVo createFromPo(List<Category> mainCategory,List<Category> subCategory){
        Function<Category, CategoryVo> po2vo =
                cate -> {
                    CategoryVo vo = new CategoryVo();
                    vo.setId(cate.getId());
                    vo.setName(cate.getName());
                    vo.setWap_banner_url(cate.getIconUrl());
                    return vo;
                };
        List<CategoryVo> categoryListVo = mainCategory.stream()
                .map(po2vo).collect(Collectors.toList());

        List<CategoryVo> subCategoryVo = subCategory.stream()
                .map(po2vo).collect(Collectors.toList());

        CategoryVo currentCategoryVo = new CategoryVo();
        currentCategoryVo.setId(mainCategory.get(0).getId());
        currentCategoryVo.setName(mainCategory.get(0).getName());
        currentCategoryVo.setSubCategoryList(subCategoryVo);

        return new CatalogVo(categoryListVo,currentCategoryVo);

    }

}

@Data
class CategoryVo {
    private Integer id;
    private String name;
    private String wap_banner_url;
    private List<CategoryVo> subCategoryList;
}


