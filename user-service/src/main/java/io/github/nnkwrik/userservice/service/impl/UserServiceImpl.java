package io.github.nnkwrik.userservice.service.impl;

import io.github.nnkwrik.userservice.dao.UserMapper;
import io.github.nnkwrik.userservice.model.User;
import io.github.nnkwrik.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
