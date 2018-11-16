package io.github.nnkwrik.goodsservice.service;

import io.github.nnkwrik.goodsservice.model.vo.CatalogVo;

/**
 * @author nnkwrik
 * @date 18/11/16 15:22
 */
public interface CatalogService {

    CatalogVo getCatalogIndex();

    CatalogVo getCatalogById(int id);

}
