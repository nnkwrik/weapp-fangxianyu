package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.goodsservice.model.vo.inner.GoodsSimpleVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/18 8:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRelatedVo {
    private List<GoodsSimpleVo> goodsList;
}
