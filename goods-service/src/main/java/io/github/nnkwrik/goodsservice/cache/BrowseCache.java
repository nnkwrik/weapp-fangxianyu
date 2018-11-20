package io.github.nnkwrik.goodsservice.cache;

import com.google.common.cache.*;
import io.github.nnkwrik.goodsservice.dao.OtherMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 商品浏览量的缓存,过期(10天)/超出缓存大小/超过10个时更新数据库
 * 由于需要在非静态的RemovalListener才能调用mapper,做成单例
 *
 * @author nnkwrik
 * @date 18/11/20 12:03
 */
@Slf4j
@Component
public class BrowseCache {

    @Autowired
    private OtherMapper otherMapper;

    private static BrowseCache browseCache = new BrowseCache(); //单例


    //过期(1天)/超出缓存队列大小(1000)/缓存超过10个时 会触发
    RemovalListener<Integer, AtomicInteger> listener =
            notification -> {
                log.info("BrowseCache缓存刷入数据库，原因 ：【{}】,数据 ：【key={} , value={}】",
                        notification.getCause(), notification.getKey(), notification.getValue().get());
                otherMapper.addBrowseCount(notification.getKey(), notification.getValue().get());
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


    public static void add(Integer goodsId) {
        Cache<Integer, AtomicInteger> singleCache =  browseCache.cache;
        AtomicInteger browseCount = singleCache.getIfPresent(goodsId);
        if (browseCount != null) {
            int count = browseCount.incrementAndGet();
            if (count > 10) {
                //手动移除缓存，让他触发removalListener，刷新数据库
                singleCache.invalidate(goodsId);
            }
        } else {
            singleCache.put(goodsId, new AtomicInteger(1));
        }

        //检查过期的缓存,让他触发removalListener
        //必须执行这个cache才会去检查是否过期, 否则尽管过期他也不会触发removalListener
        singleCache.cleanUp();
    }


}
