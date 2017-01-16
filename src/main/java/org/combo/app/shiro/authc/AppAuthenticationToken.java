package org.combo.app.shiro.authc;

import org.apache.shiro.authc.AuthenticationToken;

public class AppAuthenticationToken implements AuthenticationToken{

    private String username;

    private String rawPwd;

    public AppAuthenticationToken(String username, String rawPwd) {
        this.username = username;
        this.rawPwd = rawPwd;
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
}
