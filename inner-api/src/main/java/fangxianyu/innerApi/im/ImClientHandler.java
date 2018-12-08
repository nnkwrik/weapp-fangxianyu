package fangxianyu.innerApi.im;

import io.github.nnkwrik.common.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author nnkwrik
 * @date 18/12/08 19:23
 */
@Component
@Slf4j
public class ImClientHandler {
    @Autowired
    private ImClient imClient;

    public Integer createChat(int goodsId, String senderId, String receiverId) {
        log.info("让消息服务创建消息");
        Response<Integer> response = imClient.createChat(goodsId, senderId, receiverId);
        if (response.getErrno() != 0) {
            log.info("消息服务创建消息失败,errno={},原因={}", response.getErrno(), response.getErrmsg());
            return null;
        }
        return response.getData();
    }
}
