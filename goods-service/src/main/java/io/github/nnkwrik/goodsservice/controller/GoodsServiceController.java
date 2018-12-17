package io.github.nnkwrik.goodsservice.controller;

import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.dto.SimpleGoods;
import io.github.nnkwrik.goodsservice.dao.GoodsServiceMapper;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对其他服务开放的api
 *
 * @author nnkwrik
 * @date 18/12/07 20:43
 */
@RestController
@RequestMapping("/goods-service")
@Slf4j
public class GoodsServiceController {

    @Autowired
    private GoodsServiceMapper goodsServiceMapper;


    /**
     * 获取简单商品信息
     *
     * @param goodsId
     * @return
     */
    @GetMapping("/simpleGoods/{goodsId}")
    Response<SimpleGoods> getSimpleGoods(@PathVariable("goodsId") Integer goodsId) {
        Goods goods = goodsServiceMapper.getSimpleGoods(goodsId);

        if (goods == null) {
            log.info("其他服务通过goodsId : [{}] 查询商品基本信息，没有查询到该商品", goodsId);
            return Response.fail(Response.GOODS_IN_NOT_EXIST, "不存在的商品");
        }

        SimpleGoods dto = new SimpleGoods();
        BeanUtils.copyProperties(goods, dto);
        log.info("其他服务通过goodsId : [{}] 查询商品基本信息，查询结果={}", goodsId, dto);
        return Response.ok(dto);
    }


    /**
     * 获取简单商品信息列表
     *
     * @param goodsIdList
     * @return
     */
    @GetMapping("/simpleGoodsList")
    Response<Map<Integer, SimpleGoods>> getSimpleGoodsList(@RequestParam List<Integer> goodsIdList) {
        List<Goods> godosList = goodsServiceMapper.getSimpleGoodsList(goodsIdList);
        Map<Integer, SimpleGoods> dtoMap = new HashMap<>();
        godosList.stream().forEach(goods -> {
            SimpleGoods simpleGoods = new SimpleGoods();
            BeanUtils.copyProperties(goods, simpleGoods);
            dtoMap.put(goods.getId(), simpleGoods);
        });

        if (ObjectUtils.isEmpty(dtoMap)) {
            log.info("其他服务通过goodsId : [{}] 查询商品基本信息，没有查询到该商品", goodsIdList);
            return Response.fail(Response.GOODS_IN_NOT_EXIST, "不存在的商品");
        }

        log.info("其他服务通过goodsId : [{}] 查询用户基本信息，查询结果：{}", goodsIdList, dtoMap);
        return Response.ok(dtoMap);
    }

}
