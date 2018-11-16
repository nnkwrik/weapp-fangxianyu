package io.github.nnkwrik.goodsservice.service.impl;

import io.github.nnkwrik.goodsservice.dao.GoodsMapper;
import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.vo.CatalogVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.CategoryVo;
import io.github.nnkwrik.goodsservice.service.CatalogService;
import io.github.nnkwrik.goodsservice.util.PO2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/16 15:23
 */
@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public CatalogVo getCatalogIndex(){
        List<Category> mainCategory = goodsMapper.findMainCategory();
        Category currentCate = mainCategory.get(0);
        List<Category> subCategory = goodsMapper.findSubCategory(currentCate.getId());


        List<CategoryVo> mainCategoryVo = PO2VO.convertList(PO2VO.Category, mainCategory);
        List<CategoryVo> subCategoryVo = PO2VO.convertList(PO2VO.Category, subCategory);
        CategoryVo currentCategoryVo =
                new CategoryVo(currentCate.getId(), currentCate.getName(), currentCate.getIconUrl(), subCategoryVo);

        return new CatalogVo(mainCategoryVo, currentCategoryVo);
    }

    @Override
    public CatalogVo getCatalogById(int id) {
        List<Category> subCategory = goodsMapper.findSubCategory(id);
        List<CategoryVo> subCategoryVo = PO2VO.convertList(PO2VO.Category, subCategory);
        Category currentCategory = goodsMapper.findCategoryById(id);
        CategoryVo currentCategoryVo = PO2VO.Category.apply(currentCategory);
        currentCategoryVo.setSubCategoryList(subCategoryVo);

        return new CatalogVo(null, currentCategoryVo);
    }





}
