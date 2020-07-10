package xyz.herther.Shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.herther.pojo.User;
import xyz.herther.service.UserService;

/**
 * Herther
 * 2020/7/8
 * shiroReaml类 实现授权 验证
 */
public class UserReaml extends AuthorizingRealm {



    //获取授权信息
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("获取授权信息");
        //给资源进行授权
        SimpleAuthorizationInfo info =new SimpleAuthorizationInfo();
        //添加字符串权限
//        动态添加
        //1、到数据获取权限字符串
        //2、获取当前用户字符串
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        // info.addStringPermission("user:add");
        User dbUser = userService.findById(user.getId());
        System.out.println("资源授权层："+dbUser.getPerms());
        info.addStringPermission(dbUser.getPerms());

        return info;
    }
    //注入Service层
    @Autowired
    private UserService userService;

    //获取身份验证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
        System.out.println("执行认证操作");

        UsernamePasswordToken token = (UsernamePasswordToken)arg0;
        System.out.println("UserReaml层:"+token.getUsername());

        User user = userService.findByName(token.getUsername());
//        1、判断用户名
       // System.out.println(user.toString());
        if (user==null){
            //用户名不存在
            return null;    //shiro的底层会抛出UnknowAcountException
        }
        //2、判断密密码
        //返回new一个子类 第一参数是返回subject的一些消息可以为空，第二个参数是数据的用户的密码， 第三个是shiro的名字
        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }
}
