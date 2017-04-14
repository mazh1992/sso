package com.example.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.common.Constant.CAS_TOKEN_COOKIE_NAME;

/**
 * Created by mazhenhua on 2016/12/28.
 */
public class SsoCookie {

    public static Cookie makeCookie(String token) {
        Cookie cookie = new Cookie(CAS_TOKEN_COOKIE_NAME, token);
        cookie.setPath("/");
        cookie.setHttpOnly(true); // 防止 js脚本等获取到cookie 进行xss攻击
        //cookie.setSecure(true); // 可以防止窃取cookie 只能在https传输中进行传递cookie
        cookie.setDomain("test.com");
        return cookie;
    }

    public static final void removeCookie(HttpServletResponse resp) {
        Cookie cookie = new Cookie(CAS_TOKEN_COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }

    public static final void removeCookie(HttpServletResponse resp, String domain) {
        Cookie cookie = new Cookie(CAS_TOKEN_COOKIE_NAME, "");
        cookie.setDomain(domain);
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }

    public static void setCookie(String token, HttpServletResponse resp) {
        resp.addCookie(makeCookie(token));
    }

    public static void setCookie(String token, HttpServletResponse resp, String domain, String path) {
        Cookie cookie = makeCookie(token);
        //cookie.setSecure(true);
        cookie.setHttpOnly(true);
        if (domain != null) {
            cookie.setDomain(domain);
        }

        resp.addCookie(cookie);
    }

    /**
     * 获取登录cookie
     * @return
     */
    public static Cookie getCookie(HttpServletRequest hsr) {
        Cookie[] cookies = hsr.getCookies();
        Cookie ret = null;
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (CAS_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    ret = cookie;
                    break;
                }
            }
        }
        return ret;
    }
}
