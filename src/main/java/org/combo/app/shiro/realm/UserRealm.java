package org.combo.app.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.combo.app.conf.AppConfig;
import org.combo.app.shiro.authc.AppAuthenticationInfo;

public class UserRealm extends AuthorizingRealm {

    private AppConfig appConfig;

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String hashedPwdFromDb = "hashedPwdFromDb";
        String saltFromDb = "saltFromDb";
        return new AppAuthenticationInfo(hashedPwdFromDb, saltFromDb,appConfig.getHashAlgorithmName(), appConfig.getHashIterations());
    }
}
