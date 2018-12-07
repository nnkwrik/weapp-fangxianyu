package io.github.nnkwrik.imservice;

import io.github.nnkwrik.imservice.model.vo.WsMessage;
import io.github.nnkwrik.imservice.redis.RedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/12/07 13:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestRedisClient {

    @Autowired
    private RedisClient redisClient;

    @Test
    public void testNoUnread(){
        //not exist
        List<WsMessage> hvals = redisClient.hvals("1212");
        if (hvals == null){
            System.out.println("null");
        }else {
            System.out.println(hvals.size());
        }

    }
}
