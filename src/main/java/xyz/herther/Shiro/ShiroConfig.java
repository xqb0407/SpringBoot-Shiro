package xyz.herther.Shiro;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * shiro配置类
 * Herther
 * 2020/7/5
 */
@Configuration
public class ShiroConfig {
    /**
     *创建 ShiroFilterfactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager")DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean= new ShiroFilterFactoryBean();
        //1、设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        /**
         *Shiro内置过滤器，可以实现权限相关拦截
         * 常用的过滤器：
         * anno：无需认证（登录可以访问）
         * authc：必须认证才可以访问
         * user：如果使用rememberMe的功能才可以直接访问
         * role：该资源必须得到角色的权限才可以访问
         */
        Map<String, String> filterMap=new LinkedHashMap<String,String>();
//        filterMap.put("/add","authc");
//        filterMap.put("/update", "authc");
        filterMap.put("/*","authc");
        //部分拦截
        filterMap.put("/hello", "anno");

        //修改登录页面
        shiroFilterFactoryBean.setLoginUrl("tologin");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 安全管理器
     * 创建 DeafultWebSecurityManager
     */
    @Bean(name = "defaultWebSecurityManager")
    public DefaultWebSecurityManager getDeafaultWebSecurityManager(@Qualifier("userReaml")UserReaml userReaml){
        DefaultWebSecurityManager WebSecurityManager = new DefaultWebSecurityManager();
        //关联Reaml
        WebSecurityManager.setRealm(userReaml);
        return WebSecurityManager;
    }
    /**
     * 创建Reaml
     */
    @Bean(name = "userReaml")
    public UserReaml getRealm(){
        return new UserReaml();
    }

}
