package com.example.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by mazhenhua on 2017/3/30.
 *
 * 接收配置文件中的sso单点登陆的的值
 */

@Component
@ConfigurationProperties(prefix = "sso")
public class SSOProperties {

    /**
     * 是否允许重复登陆
     */
    private boolean repeatLogin;

    public boolean isRepeatLogin() {
        return repeatLogin;
    }

    public void setRepeatLogin(boolean repeatLogin) {
        this.repeatLogin = repeatLogin;
    }
}
