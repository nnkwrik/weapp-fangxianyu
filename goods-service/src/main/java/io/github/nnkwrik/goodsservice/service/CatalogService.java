package io.github.nnkwrik.goodsservice.service;

import io.github.nnkwrik.goodsservice.model.vo.CatalogVo;
import io.github.nnkwrik.goodsservice.model.vo.IndexVO;

/**
 * @author nnkwrik
 * @date 18/11/16 15:22
 */
public interface CatalogService {

    IndexVO getIndex();

    CatalogVo getCatalogIndex();

    CatalogVo getCatalogById(int id);


}
