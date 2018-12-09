package fangxianyu.innerApi.user;

import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.dto.SimpleUser;
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
public class UserClientHandler {
    @Autowired
    private UserClient userClient;

    public SimpleUser getSimpleUser(String openId) {
        log.info("从用户服务查询用户的简单信息");
        Response<SimpleUser> response = userClient.getSimpleUser(openId);
        if (response.getErrno() != 0) {
            log.info("从用户服务获取用户信息列表失败,errno={},原因={}", response.getErrno(), response.getErrmsg());
            return SimpleUser.unknownUser();
        }
        return response.getData();
    }

    public Map<String, SimpleUser> getSimpleUserList(List<String> openIdList) {
        log.info("从用户服务查询用户的简单信息");
        if (openIdList.size() < 1){
            log.info("用户idList为空,返回空的结果");
            return new HashMap<>();
        }
        Response<Map<String, SimpleUser>> response = userClient.getSimpleUserList(openIdList);
        if (response.getErrno() != 0) {
            log.info("从用户服务获取用户信息列表失败,errno={},原因={}", response.getErrno(), response.getErrmsg());
            return new HashMap<>();
        }
        return response.getData();
    }
}
