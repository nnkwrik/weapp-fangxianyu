package io.github.nnkwrik.goodsservice.util;

import io.github.nnkwrik.goodsservice.model.po.Ad;
import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.po.Channel;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import io.github.nnkwrik.goodsservice.model.vo.inner.BannerVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.CategoryVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.ChannelVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.GoodsSimpleVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author nnkwrik
 * @date 18/11/15 18:00
 */
public class PO2VO {

    public static Function<Category, CategoryVo> Category =
            po -> {
                CategoryVo vo = new CategoryVo();
                BeanUtils.copyProperties(po, vo);
                //手动拷贝和po不相同的属性。
                vo.setWap_banner_url(po.getIconUrl());
                return vo;
            };

    public static Function<Channel, ChannelVo> Channel =
            po -> {
                ChannelVo vo = new ChannelVo();
                BeanUtils.copyProperties(po, vo);
                vo.setIcon_url(po.getIconUrl());
                return vo;
            };

    public static Function<Goods, GoodsSimpleVo> GoodsSimple =
            po -> {
                GoodsSimpleVo vo = new GoodsSimpleVo();
                BeanUtils.copyProperties(po, vo);
                vo.setList_pic_url(po.getPrimaryPicUrl());
                return vo;
            };

    public static Function<Ad, BannerVo> Banner =
            po -> {
                BannerVo vo = new BannerVo();
                BeanUtils.copyProperties(po, vo);
                vo.setImage_url(po.getImageUrl());
                return vo;
            };

    public static <T, R> R convert(Function<T, R> converter, T po) {
        return converter.apply(po);
    }

    public static <T, R> List<R> convertList(Function<T, R> converter, List<T> poList) {
        return poList.stream()
                .map(converter).collect(Collectors.toList());
    }


}
