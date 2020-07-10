package xyz.herther.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.herther.dao.UserMapper;
import xyz.herther.pojo.User;
import xyz.herther.service.UserService;

/**
 * Herther
 * 2020/7/8
 * 业务逻辑接口实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    public User findByName(String username) {
        User byName = userMapper.findByName(username);
        return byName;
    }


    public User findById(int id) {
        User byId = userMapper.findById(id);
        return byId;
    }
}
