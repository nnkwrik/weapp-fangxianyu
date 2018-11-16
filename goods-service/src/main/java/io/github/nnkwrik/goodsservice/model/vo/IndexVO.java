package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.goodsservice.model.vo.inner.BannerVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.ChannelVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.GoodsSimpleVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/14 20:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexVO {
    private List<GoodsSimpleVo> indexGoodsList;
    private List<BannerVo> banner;
    private List<ChannelVo> channel;
}

