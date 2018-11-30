package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.goodsservice.model.po.Category;
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
public class CatalogPageVo {
    private List<Category> allCategory;   //其他所有和这个同级的分类
    private List<Category> subCategory;   //它的所有子分类
}


