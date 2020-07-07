package xyz.herther.Shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class UserReaml extends AuthorizingRealm {
    //获取授权信息
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("获取授权信息");
        return null;
    }
    //获取身份验证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行认证操作");
        //假设数据库用户名密码
        String username= "admin";
        String password ="123";
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
//        1、判断用户名
        if (!token.getUsername().equals(username)){
            //用户名不存在
            return null;    //shiro的底层会抛出UnknowAcountException
        }
        //2、判断密密码
        //返回new一个子类 第一参数是返回subject的一些消息可以为空，第二个参数是数据的用户的密码， 第三个是shiro的名字
        return new SimpleAuthenticationInfo("",password,"");
    }
}
