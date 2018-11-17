package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.goodsservice.model.vo.inner.CategoryVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.GoodsSimpleVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/17 20:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPageVo {
    private List<CategoryVo> brotherCategory; //同一个父分类下的兄弟分类
    private List<GoodsSimpleVo> goodsList;    //当前分类的商品列表
}


