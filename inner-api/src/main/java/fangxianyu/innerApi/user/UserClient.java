package fangxianyu.innerApi.user;

import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.dto.SimpleUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 使用user-service的api
 *
 * @author nnkwrik
 * @date 18/11/23 18:06
 */
@FeignClient(name = "user-service")
@RequestMapping("/user-service")
public interface UserClient {

    /**
     * 获取用户openId的相关信息
     *
     * @param openId
     * @return
     */
    @GetMapping("/simpleUser/{openId}")
    Response<SimpleUser> getSimpleUser(@PathVariable("openId") String openId);

    /**
     * 获取用户openIdList的相关信息
     *
     * @param openIdList
     * @return
     */
    @GetMapping("/simpleUserList")
    Response<Map<String, SimpleUser>> getSimpleUserList(@RequestParam List<String> openIdList);
}
