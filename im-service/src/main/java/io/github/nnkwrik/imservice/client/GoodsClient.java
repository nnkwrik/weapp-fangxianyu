package io.github.nnkwrik.imservice.client;

import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.dto.SimpleGoods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nnkwrik
 * @date 18/12/07 21:08
 */
@FeignClient(name = "goods-service")
@RequestMapping("/goods-service")
public interface GoodsClient {


    @GetMapping("/simpleGoods/{goodsId}")
    Response<SimpleGoods> getSimpleGoods(@PathVariable("goodsId") Integer goodsId);


    @GetMapping("/simpleGoodsList")
    Response<Map<Integer, SimpleGoods>> getSimpleGoodsList(@RequestParam List<Integer> goodsIdList);
}
