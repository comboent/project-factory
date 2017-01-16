package org.combo.app.shiro.authc;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;

public class AppAuthenticationInfo implements AuthenticationInfo {

    private String hashedPwd;

    private String salt;

    private String hashAlgorithmName;

    private int hashIterations;

    public AppAuthenticationInfo(String hashedPwd, String salt, String hashAlgorithmName, int hashIterations) {
        this.hashedPwd = hashedPwd;
        this.salt = salt;
        this.hashAlgorithmName = hashAlgorithmName;
        this.hashIterations = hashIterations;
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return hashedPwd;
    }

    public String getHashedPwd() {
        return hashedPwd;
    }

    public String getSalt() {
        return salt;
    }

    public String getHashAlgorithmName() {
        return hashAlgorithmName;
    }

    public int getHashIterations() {
        return hashIterations;
    }
}
