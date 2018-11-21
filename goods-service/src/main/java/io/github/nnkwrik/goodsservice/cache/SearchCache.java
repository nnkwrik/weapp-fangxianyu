package io.github.nnkwrik.goodsservice.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 热门关键字的缓存,超出缓存大小时会优先顶替掉最久没更新value的key
 *
 * @author nnkwrik
 * @date 18/11/20 15:23
 */
@Slf4j
@Component
public class SearchCache {
    /**
     * @key 搜索关键字 TODO 分词
     * @value 搜索次数
     */
    private Cache<String, AtomicInteger> cache =
            CacheBuilder.newBuilder()
                    .maximumSize(10000) //超出大小时,替换最久没更新value的key
                    .expireAfterWrite(30, TimeUnit.DAYS)
                    .build();

    public void add(String keyword) {
        int count = 0;
        AtomicInteger browseCount = cache.getIfPresent(keyword);
        if (browseCount != null) {
            count = browseCount.incrementAndGet();
        } else {
            count =1;
            cache.put(keyword, new AtomicInteger(1));
        }

        log.debug("SearchCache更新关键字【{}】的搜索次数为【{}】", keyword, count);
    }

    public List<String> getHot(int num) {
        log.debug("从SearchCache获取热门关键字列表");
        return cache.asMap().entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> -entry.getValue().get()))
                .limit(num)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

    }
}
