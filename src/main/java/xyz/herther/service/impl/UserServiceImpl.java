package xyz.herther.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.herther.dao.UserMapper;
import xyz.herther.pojo.User;
import xyz.herther.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    public User findByName(String username) {
        User byName = userMapper.findByName(username);
        return byName;
    }
}
