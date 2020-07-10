package xyz.herther.service;

import xyz.herther.pojo.User;

public interface UserService {
    public User findByName(String username);
    public User findById(int id);
}
