package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.goodsservice.model.vo.inner.ChannelVo;
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
    private List<GoodsSimpleVO> indexGoodsList;
    private List<BannerVo> banner;
    private List<ChannelVo> channel;

}

@Data
class GoodsSimpleVO {
    private Integer id;
    private String name;
    private String list_pic_url;
    private Double retail_price;
}

@Data
class BannerVo {
    private Integer id;
    private String name;
    private String image_url;
    private Integer ad_position_id = 1;
    private Integer media_type = 1;
    private String link;
    private String content;
    private Integer end_time = 0;
    private Integer enabled = 1;
}

