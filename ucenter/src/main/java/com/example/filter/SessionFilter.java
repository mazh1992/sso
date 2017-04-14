package com.example.filter;

import com.example.common.Constant;
import com.example.common.SSOProperties;
import com.example.entity.UserInfo;
import com.example.mapper.UserMapper;
import com.example.utils.MemcachedUtils;
import com.example.utils.SsoCookie;
import com.example.utils.SsoUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mazhenhua on 2017/3/28.
 */
public class SessionFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    private String              prersonPassUrl = "css.*,frame.*,image.*,js.*,login.*";

    private String[]            prersonPassUrls;

    @Resource
    private SSOProperties ssoProperties;

    @Resource
    private UserMapper userMapper;

    /**
     * 封装，不需要过滤的list列表
     */
    protected static List<Pattern> patterns = new ArrayList<Pattern>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        prersonPassUrls = prersonPassUrl.split(",");
        for (String url : prersonPassUrls) {
            Pattern p = Pattern.compile(url);
            patterns.add(p);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String url = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        if (url.startsWith("/") && url.length() > 1) {
            url = url.substring(1);
        }

        if (isInclude(url)) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        } else {
            HttpSession session = httpRequest.getSession();
            UserInfo userInfo = (UserInfo) session.getAttribute(Constant.LOGIN_SESSION);
            if (ssoProperties.isRepeatLogin() && userInfo != null) {
                chain.doFilter(httpRequest, httpResponse);
                return;
            } else {
                this.refreshSession(session, httpRequest, userInfo);
                if (session.getAttribute(Constant.LOGIN_SESSION) == null){
                    logger.info("session刷新失败，请登录");

                    httpResponse.sendRedirect("/login.htm?returnUrl=" + url);
                    return;
                }
                logger.info("session刷新成功");
                chain.doFilter(httpRequest, httpResponse);
                return;
            }
        }
    }


    @Override
    public void destroy() {

    }
        /**
         * 是否需要过滤
         * @param url
         * @return
         */
    private boolean isInclude(String url) {
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 刷新session
     * @param session
     * @param request
     */
    private void refreshSession(HttpSession session, HttpServletRequest request, UserInfo userInfo){
        Cookie cookie = SsoCookie.getCookie(request);
        if (cookie != null){
            String cookieValue = cookie.getValue();
            if (StringUtils.isNotBlank(cookieValue)) {
                // 通过cookie值，获取缓存中用户的信息
                String userId = (String) MemcachedUtils.get(cookieValue);

                // cookie值为key对应的userId不存在 && userId为key对应的cookie存在，并且与之不相等
                if (StringUtils.isBlank(userId) && userInfo != null) {
                    String memcachedCookie = (String) MemcachedUtils.get(userInfo.getIdString());
                    if (StringUtils.isNotBlank(memcachedCookie) && !StringUtils.equals(memcachedCookie, cookieValue)){
                        session.setAttribute(Constant.LOGIN_SESSION, null);
                        logger.info("您已被踢下线，如不是您操作，请尽快修改密码。。。");
                        return;
                    }
                    /*else if (StringUtils.isBlank(userId)){
                        // 缓存时间超时自动过期，重新设置缓存值
                        String key =SsoUtils.saveMemcached(cookieValue, userInfo.getIdString());
                        logger.info("缓存自动过期，从session中将信息刷会缓存：{},{}", userInfo.getIdString(), key);
                    }*/
                }

                // 如果session中有值，直接跳过
                if (userInfo != null) {
                    return;
                }

                // 如果缓存值有，session值过期，则从新获取user信息并设置session
                if (StringUtils.isNotBlank(userId)) {
                    // 通过缓存中存的userId来获取用户的详细信息。
                    userInfo = userMapper.findById(Long.parseLong(userId));
                    session.setAttribute(Constant.LOGIN_SESSION, userInfo);
                }
            }
        }
    }
}
