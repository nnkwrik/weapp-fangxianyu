package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通过分类查询商品时的页面
 *
 * @author nnkwrik
 * @date 18/11/17 20:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPageVo {

    /*同一个父分类下的兄弟分类*/
    private List<Category> brotherCategory;

    /*当前分类的商品列表*/
    private List<Goods> goodsList;
}


