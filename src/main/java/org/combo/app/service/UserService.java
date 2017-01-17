package org.combo.app.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.combo.app.conf.AppConfig;
import org.combo.app.dao.UserDao;
import org.combo.app.entity.User;
import org.combo.app.exception.Code;
import org.combo.app.exception.ExceptionFactory;
import org.combo.app.shiro.authc.AppAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CacheManager cacheManager;

    public ModelAndView regUser(String username, String pwd) {
        User user = new User();
        String id = UUID.randomUUID().toString().replace("-", "");
        user.setId(id);
        user.setUsername(username);
        String salt = RandomStringUtils.randomAlphanumeric(16);
        String hashAlgorithmName = appConfig.getHashAlgorithmName();
        int hashIterations = appConfig.getHashIterations();
        SimpleHash hashedPwd = new SimpleHash(
                hashAlgorithmName,
                pwd,
                salt,
                hashIterations
        );

        String hex = hashedPwd.toHex();
        user.setHashedPwd(hex);
        user.setSalt(salt);
        System.out.println("id:" + id + ",username:" + username + ",salt:" + salt + ",hashedPwd:" + hex + ",hashAlg:" + hashAlgorithmName + ",interations:" + hashIterations);
        userDao.save(user);
        ModelAndView mav = new ModelAndView("regOk");
        mav.addObject("user", user);
        return mav;
    }

    public ModelAndView login(String username, String pwd) {
        Subject subject = SecurityUtils.getSubject();
        AppAuthenticationToken token = new AppAuthenticationToken(username, pwd);
        try {
            subject.login(token);
        } catch (IncorrectCredentialsException e) {
            Cache<String, AtomicInteger> passwordRetryCache = cacheManager.getCache("passwordRetryCache");
            AtomicInteger tryNum = passwordRetryCache.get(username);
            if(tryNum == null) {
                tryNum = new AtomicInteger(0);
            }
            throw ExceptionFactory.create(Code.PWD_ERROR, appConfig.getPwdRetryLimit() - tryNum.get(), appConfig.getPwdRetryLimit());
        } catch (ExcessiveAttemptsException e) {
            throw ExceptionFactory.create(Code.EXCEED_PWD_TRY_NUM);
        } catch (AuthenticationException e) {
            throw ExceptionFactory.create(Code.LOGIN_ERROR);
        }
        return new ModelAndView("loginOk");
    }

    public ModelAndView subjectInfo() {
        Subject subject = SecurityUtils.getSubject();
        return new ModelAndView("subject").addObject("sub", subject);
    }
}
