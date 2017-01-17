package org.combo.app.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.combo.app.conf.AppConfig;
import org.combo.app.dao.UserDao;
import org.combo.app.entity.User;
import org.combo.app.shiro.authc.AppAuthenticationInfo;
import org.combo.app.shiro.authc.AppAuthenticationToken;

public class UserRealm extends AuthorizingRealm {

    private AppConfig appConfig;

    private UserDao userDao;

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof AppAuthenticationToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        AppAuthenticationToken appToken = (AppAuthenticationToken) token;
        String username = appToken.getUsername();
        User user = userDao.find_username(username);
        if(user == null) {
            throw new UnknownAccountException();
        }
        String hashedPwdFromDb = user.getHashedPwd();
        String saltFromDb = user.getSalt();
        return new AppAuthenticationInfo(getName(), username, hashedPwdFromDb, saltFromDb,appConfig.getHashAlgorithmName(), appConfig.getHashIterations());
    }
}
