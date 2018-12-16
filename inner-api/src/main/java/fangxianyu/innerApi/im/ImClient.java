package fangxianyu.innerApi.im;

import io.github.nnkwrik.common.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author nnkwrik
 * @date 18/12/08 19:23
 */
@FeignClient(name = "im-service")
@RequestMapping("/chat-service")
public interface ImClient {

    @PostMapping("/createChat/{goodsId}/{senderId}/{receiverId}")
    public Response<Integer> createChat(@PathVariable("goodsId") int goodsId,
                                        @PathVariable("senderId") String senderId,
                                        @PathVariable("receiverId") String receiverId);
}
