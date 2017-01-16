package org.combo.app.shiro.credentials;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.combo.app.shiro.authc.AppAuthenticationInfo;
import org.combo.app.shiro.authc.AppAuthenticationToken;

import java.util.concurrent.atomic.AtomicInteger;

public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private Cache<String, AtomicInteger> passwordRetryCache;

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        if (!(info instanceof AppAuthenticationInfo) || !(token instanceof AppAuthenticationToken)) {
            return false;
        }

        AppAuthenticationInfo authcInfo = (AppAuthenticationInfo) info;
        AppAuthenticationToken authcToken = (AppAuthenticationToken) token;

        String username = authcToken.getUsername();
        //retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        if (retryCount.incrementAndGet() > 5) {
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }

        String rawPwd = authcToken.getRawPwd();
        SimpleHash tokenHash = new SimpleHash(
                authcInfo.getHashAlgorithmName(),
                rawPwd,
                authcInfo.getSalt(),
                authcInfo.getHashIterations());

        String hashedTokenPwd = tokenHash.toHex();

        boolean match = false;
        if(StringUtils.equals(hashedTokenPwd, authcInfo.getHashedPwd())) {
            match = true;
        }

        if (match) {
            //clear retry count
            passwordRetryCache.remove(username);
        }
        return match;
    }
}
