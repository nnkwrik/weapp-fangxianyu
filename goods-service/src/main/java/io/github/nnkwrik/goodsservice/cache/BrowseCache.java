package io.github.nnkwrik.goodsservice.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;
import io.github.nnkwrik.goodsservice.dao.GoodsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 商品浏览量的缓存,过期(10天)/超出缓存大小/超过10个时更新数据库
 *
 * @author nnkwrik
 * @date 18/11/20 12:03
 */
@Slf4j
@Component
public class BrowseCache {

    @Autowired
    private GoodsMapper goodsMapper;


    //过期(1天)/超出缓存队列大小(1000)/缓存超过10个时 会触发
    private RemovalListener<Integer, AtomicInteger> listener =
            notification -> {
                log.info("BrowseCache缓存刷入数据库，原因 ：【{}】,数据 ：【key={} , value={}】",
                        notification.getCause(), notification.getKey(), notification.getValue().get());
                goodsMapper.addBrowseCount(notification.getKey(), notification.getValue().get());
            };


    /**
     * @key 商品id
     * @value 缓存的浏览量
     */
    private Cache<Integer, AtomicInteger> cache =
            CacheBuilder.newBuilder()
                    .maximumSize(1000)
                    .expireAfterWrite(1, TimeUnit.DAYS)
                    .removalListener(
                            RemovalListeners.asynchronous(listener, Executors.newWorkStealingPool()))
                    .build();


    /**
     * 在缓存中增加某商品的浏览次数
     * @param goodsId
     */
    public void add(Integer goodsId) {
        int count = 0;
        AtomicInteger browseCount = cache.getIfPresent(goodsId);
        if (browseCount != null) {
            count = browseCount.incrementAndGet();
            if (count > 5) {
                //手动移除缓存，让他触发removalListener，刷新数据库
                cache.invalidate(goodsId);
            }
        } else {
            count = 1;
            cache.put(goodsId, new AtomicInteger(1));
        }
        log.debug("BrowseCache更新商品id【{}】的浏览次数为【{}】", goodsId, count);
        //用cleanUp()检查过期的缓存,让他触发removalListener
        //必须执行这个cache才会去检查是否过期, 否则尽管过期他也不会触发removalListener
        cache.cleanUp();
    }


}
