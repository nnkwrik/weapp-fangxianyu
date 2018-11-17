package io.github.nnkwrik.goodsservice.util;

import io.github.nnkwrik.goodsservice.model.po.*;
import io.github.nnkwrik.goodsservice.model.vo.inner.*;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author nnkwrik
 * @date 18/11/15 18:00
 */
public class PO2VO {

    public static Function<Category, CategoryVo> category =
            po -> {
                CategoryVo vo = new CategoryVo();
                BeanUtils.copyProperties(po, vo);
                //手动拷贝和po不相同的属性。
                vo.setWap_banner_url(po.getIconUrl());
                return vo;
            };

    public static Function<Channel, ChannelVo> channel =
            po -> {
                ChannelVo vo = new ChannelVo();
                BeanUtils.copyProperties(po, vo);
                vo.setIcon_url(po.getIconUrl());
                return vo;
            };

    public static Function<Ad, BannerVo> banner =
            po -> {
                BannerVo vo = new BannerVo();
                BeanUtils.copyProperties(po, vo);
                vo.setImage_url(po.getImageUrl());
                return vo;
            };

    public static Function<Goods, GoodsSimpleVo> goodsSimple =
            po -> {
                GoodsSimpleVo vo = new GoodsSimpleVo();
                BeanUtils.copyProperties(po, vo);
                vo.setList_pic_url(po.getPrimaryPicUrl());
                return vo;
            };

    public static Function<Goods, GoodsDetailVo> goodsDetail =
            po -> {
                GoodsSimpleVo simpleVo = convert(goodsSimple, po);
                GoodsDetailVo vo = new GoodsDetailVo();
                BeanUtils.copyProperties(simpleVo, vo);
                vo.setGoods_brief(po.getDesc());
                vo.setMarket_price(po.getMarketPrice());
                vo.setWant_count(po.getWantCount());
                vo.setBrowse_count(po.getBrowseCount());
                //Date转String
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                vo.setLast_edit(formatter.format(po.getLastEdit()));
                return vo;
            };

    public static Function<GoodsGallery,GalleryVo > gallery =
            po -> {
                GalleryVo vo = new GalleryVo();
                BeanUtils.copyProperties(po, vo);
                vo.setImg_url(po.getImgUrl());
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
