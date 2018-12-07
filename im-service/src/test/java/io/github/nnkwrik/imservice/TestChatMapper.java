package io.github.nnkwrik.imservice;

import io.github.nnkwrik.imservice.dao.ChatMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author nnkwrik
 * @date 18/12/06 16:06
 */
@RunWith(SpringRunner.class)
//@SpringBootTest <-ServerEndpointExporter会报错
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestChatMapper {

    @Autowired
    private ChatMapper chatMapper;

    @Test
    public void testGetChatId(){
        String u1 = "1";
        String u2 = "3";
        int goodsId = 0;

        //exist
        Integer chatId = chatMapper.getChatId(u1, u2, goodsId);
        System.out.println(chatId);

        //not exist
        Integer chatId2 = chatMapper.getChatId("21", "11", goodsId);
        System.out.println(chatId2);

    }



}
