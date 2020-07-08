package xyz.herther.Shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.herther.pojo.User;
import xyz.herther.service.UserService;

public class UserReaml extends AuthorizingRealm {



    //获取授权信息
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("获取授权信息");
        return null;
    }
    //注入Service层
    @Autowired
    private UserService userService;
    //获取身份验证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
        System.out.println("执行认证操作");

        UsernamePasswordToken token = (UsernamePasswordToken)arg0;
        User user = userService.findByName(token.getUsername());
//        1、判断用户名
        System.out.println(user.toString());

        if (user==null){
            //用户名不存在
            return null;    //shiro的底层会抛出UnknowAcountException
        }
        //2、判断密密码
        //返回new一个子类 第一参数是返回subject的一些消息可以为空，第二个参数是数据的用户的密码， 第三个是shiro的名字
        return new SimpleAuthenticationInfo("",user.getPassword(),"");
    }
}
