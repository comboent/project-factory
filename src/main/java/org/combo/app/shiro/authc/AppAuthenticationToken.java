package org.combo.app.shiro.authc;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

public class AppAuthenticationToken implements AuthenticationToken, RememberMeAuthenticationToken{

    private String username;

    private String rawPwd;

    private boolean rememberMe;

    public AppAuthenticationToken(String username, String rawPwd, boolean rememberMe) {
        this.username = username;
        this.rawPwd = rawPwd;
        this.rememberMe = rememberMe;
    }

    public String getUsername() {
        return username;
    }

    public String getRawPwd() {
        return rawPwd;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return rawPwd;
    }

    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }
}
