package io.github.nnkwrik.userservice.controller;


import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 对于内部服务的api
 *
 * @author nnkwrik
 * @date 18/11/10 21:51
 */
@RestController
@Slf4j
@RequestMapping("/user-service")
public class UserServiceController {


    @Autowired
    private UserService userService;


    /**
     * 查询用户基本信息
     *
     * @param openId
     * @return
     */
    @GetMapping("/simpleUser/{openId}")
    Response<SimpleUser> getSimpleUser(@PathVariable("openId") String openId) {
        SimpleUser dto = userService.getSimpleUser(openId);
        log.info("其他服务通过openId : [{}] 查询用户基本信息，查询结果：{}", openId, dto);
        if (dto == null)
            return Response.fail(Response.USER_IS_NOT_EXIST, "不存在的用户");
        return Response.ok(dto);
    }

    /**
     * 查询用户基本信息列表
     *
     * @param openIdList
     * @return
     */
    @GetMapping("/simpleUserList")
    Response<Map<String, SimpleUser>> getSimpleUserList(@RequestParam List<String> openIdList) {

        Map<String, SimpleUser> dtoMap = userService.getSimpleUserList(openIdList);
        log.info("其他服务通过openId : [{}] 查询用户基本信息，查询结果：{}", openIdList, dtoMap);
        if (ObjectUtils.isEmpty(dtoMap))
            return Response.fail(Response.USER_IS_NOT_EXIST, "不存在的用户");
        return Response.ok(dtoMap);
    }


}
