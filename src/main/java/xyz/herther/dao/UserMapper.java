package xyz.herther.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xyz.herther.pojo.User;

/**
 * Herther
 * 2020/7/8
 * mapper接口
 */
@Repository
public interface UserMapper {
     public User findByName(@Param("username") String username); //登录
     public User findById(@Param("id") int id);//根据ID查询权限
}
