package io.github.nnkwrik.goodsservice.util;

import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.po.Channel;
import io.github.nnkwrik.goodsservice.model.vo.inner.CategoryVo;
import io.github.nnkwrik.goodsservice.model.vo.inner.ChannelVo;
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
                BeanUtils.copyProperties(po,vo);
                //手动拷贝和po不相同的属性。
                vo.setWap_banner_url(po.getIconUrl());
                return vo;
            };

    public static Function<Channel, ChannelVo> Channel =
            po -> {
                ChannelVo vo = new ChannelVo();
                BeanUtils.copyProperties(po,vo);
                vo.setIcon_url(po.getIconUrl());
                return vo;
            };

    public static <T,R> List<R> convertList(Function<T,R> converter, List<T> poList){
        return poList.stream()
                .map(converter).collect(Collectors.toList());
    }



}
