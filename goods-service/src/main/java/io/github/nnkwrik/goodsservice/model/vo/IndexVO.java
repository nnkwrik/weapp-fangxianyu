package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.goodsservice.model.po.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

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


    //todo
    public static IndexVO createFromPo(List<Channel> channelPo) {
        List<ChannelVo> channelVo = channelPo.stream()
                .map(po -> {
                    ChannelVo vo = new ChannelVo();
                    BeanUtils.copyProperties(po, vo);
                    vo.setIcon_url(po.getIconUrl());
                    return vo;
                }).collect(Collectors.toList());

        return new IndexVO(null,null,channelVo);
    }

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

@Data
class ChannelVo {
    private Integer id;
    private String name;
    private String url;
    private String icon_url;
}
