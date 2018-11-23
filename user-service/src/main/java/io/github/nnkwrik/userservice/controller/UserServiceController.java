package io.github.nnkwrik.userservice.controller;


import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nnkwrik
 * @date 18/11/10 21:51
 */
@RestController
@Slf4j
@RequestMapping("/user-service")
public class UserServiceController {


    @Autowired
    private UserService userService;


    @PostMapping("/simpleUser")
    public Response<SimpleUser> getSimpleUser(@RequestBody Map<String, String> openId) {
        SimpleUser dto = userService.getSimpleUser(openId.get("openId"));
        log.info("【商品服务】通过openId : [{}] 查询用户基本信息，查询结果：{}", openId.get("openId"), dto);
        if (dto == null)
            return Response.fail(Response.USER_IS_NOT_EXIST, "不存在的用户");
        return Response.ok(dto);
    }

    @PostMapping("/simpleUserList")
    public Response<HashMap<String, SimpleUser>> getSimpleUserList(@RequestBody List<String> openIdList) {

        Map<String, SimpleUser> dtoMap = userService.getSimpleUserList(openIdList);
        log.info("【商品服务】通过openId : [{}] 查询用户基本信息，查询结果：{}", openIdList, dtoMap);
        if (dtoMap == null)
            return Response.fail(Response.USER_IS_NOT_EXIST, "不存在的用户");
        return Response.ok(dtoMap);
    }


}
