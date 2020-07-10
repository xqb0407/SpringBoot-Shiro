package xyz.herther.service;

import xyz.herther.pojo.User;
/**
 * Herther
 * 2020/7/8
 * 业务逻辑接口
 */
public interface UserService {
    public User findByName(String username);
    public User findById(int id);
}
