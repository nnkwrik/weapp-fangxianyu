package io.github.nnkwrik.userservice.service.impl;

import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.userservice.dao.UserMapper;
import io.github.nnkwrik.userservice.model.User;
import io.github.nnkwrik.userservice.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nnkwrik
 * @date 18/11/11 16:55
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public void register(User user) {
        userMapper.register(user);
    }

    @Override
    public SimpleUser getSimpleUser(String openId) {
        User user = userMapper.getSimpleUser(openId);
        if (user == null) return null;
        SimpleUser simpleUser = new SimpleUser();
        BeanUtils.copyProperties(user, simpleUser);
        return simpleUser;
    }

    @Override
    public Map<String, SimpleUser> getSimpleUserList(List<String> openIdList) {
        List<User> userList = userMapper.getSimpleUserList(openIdList);
        Map<String, SimpleUser> dtoMap = new HashMap<>();
        userList.stream().forEach(user -> {
            SimpleUser simpleUser = new SimpleUser();
            BeanUtils.copyProperties(user, simpleUser);
            dtoMap.put(user.getOpenId(), simpleUser);
        });
        return dtoMap;
    }
}
