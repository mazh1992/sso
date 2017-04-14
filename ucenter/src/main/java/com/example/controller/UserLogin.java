package com.example.controller;

import com.example.common.Constant;
import com.example.common.SSOProperties;
import com.example.entity.UserInfo;
import com.example.filter.SessionFilter;
import com.example.mapper.UserMapper;
import com.example.utils.MemcachedUtils;
import com.example.utils.SsoCookie;
import com.example.utils.SsoUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Created by mazhenhua on 2017/3/28.
 */
@Controller
public class UserLogin {

    private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    @Resource
    private SSOProperties ssoProperties;

    @Resource
    private UserMapper userMapper;

    @RequestMapping("/")
    public String web(){
        return "index";
    }

    @RequestMapping("/index.htm")
    public String index(){
        return "index";
    }

    @RequestMapping("/login.htm")
    public String web(HttpServletRequest request, HttpServletResponse response, Map<String,Object> model){
        String username = request.getParameter("account");
        String password = request.getParameter("password");
        String returnUrl = request.getParameter("returnUrl");

        // 不查数据库，直接构造一个User
        UserInfo userInfo = userMapper.checkUser(username, password);
        if (userInfo != null){
            this.saveUserInfo(userInfo, request, response);
            checkReturnUrl(returnUrl);
            return "redirect:" + returnUrl;
        }
        model.put("returnUrl", returnUrl);
        return "login";
    }

    /**
     * 1.保存session
     * 2.保存memcached缓存
     * 3.保存cookie
     * @param userInfo
     * @param request
     * @param response
     */
    private void saveUserInfo(UserInfo userInfo, HttpServletRequest request, HttpServletResponse response){
        String userId = userInfo.getIdString();
        // 保存session
        request.getSession().setAttribute(Constant.LOGIN_SESSION, userInfo);

        if (!ssoProperties.isRepeatLogin()){
            // 先踢掉之前在线的人
            String oldCookie = (String) MemcachedUtils.get(userId);
            logger.info("不可重复登陆，清楚已在线人：{} 的缓存：{}", userId, oldCookie);
            if (StringUtils.isNotBlank(oldCookie)){
                MemcachedUtils.delete(oldCookie);
                MemcachedUtils.delete(userId);

            }
        }

        String key = SsoUtils.getUUID();

        // 以cookie值为key，userId为value存储
        MemcachedUtils.set(key, userId, new Date(1000 * 60 * 30) ); //  设置30分钟后超时

        // 以userId为Key，cookie值为value存储
        MemcachedUtils.set(userId, key, new Date(1000 * 60 * 30) ); //  设置30分钟后超时

        logger.info("添加登陆人：{} 的缓存：{}", userId, key);
        // 存储cookie值
        SsoCookie.setCookie(key, response);
    }

    /**
     * returnUrl不是http开头时，加上"/"  ---过滤器给去掉了。。。。
     * @param returnUrl
     */
    private void checkReturnUrl(String returnUrl){
        if (!StringUtils.startsWithIgnoreCase(returnUrl, "http")){
            returnUrl = "/" + returnUrl;
        }
    }
}
