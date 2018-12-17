package fangxianyu.innerApi.goods;

import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.dto.SimpleGoods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nnkwrik
 * @date 18/12/08 11:02
 */
@Component
@Slf4j
public class GoodsClientHandler {

    @Autowired
    private GoodsClient goodsClient;

    public SimpleGoods getSimpleGoods(Integer goodsId) {
        log.info("从商品服务查询商品的简单信息");
        Response<SimpleGoods> response = goodsClient.getSimpleGoods(goodsId);
        if (response.getErrno() != 0) {
            log.info("从商品服务获取商品信息列表失败,errno={},原因={}", response.getErrno(), response.getErrmsg());
            return SimpleGoods.unknownGoods();
        }
        return response.getData();
    }

    public Map<Integer, SimpleGoods> getSimpleGoodsList(List<Integer> goodsIdList) {
        log.info("从商品服务查询商品的简单信息");
        if (goodsIdList == null || goodsIdList.size() < 1) {
            log.info("商品idList为空,返回空的结果");
            return new HashMap<>();
        }
        Response<Map<Integer, SimpleGoods>> response = goodsClient.getSimpleGoodsList(goodsIdList);
        if (response.getErrno() != 0) {
            log.info("从商品服务获取商品信息列表失败,errno={},原因={}", response.getErrno(), response.getErrmsg());
            return new HashMap<>();
        }
        return response.getData();
    }
}
