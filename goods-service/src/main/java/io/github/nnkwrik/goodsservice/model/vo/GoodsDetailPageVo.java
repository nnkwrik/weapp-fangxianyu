package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.goodsservice.model.vo.inner.GalleryVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.GoodsDetailVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/17 21:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDetailPageVo {
    private GoodsDetailVo info;
    private List<GalleryVo> gallery;
    //TODO comment


}
