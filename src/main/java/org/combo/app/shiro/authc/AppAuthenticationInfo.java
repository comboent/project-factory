package org.combo.app.shiro.authc;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

public class AppAuthenticationInfo implements AuthenticationInfo {

    private String username;

    private String hashedPwd;

    private String salt;

    private String hashAlgorithmName;

    private int hashIterations;

    private PrincipalCollection pc;

    public AppAuthenticationInfo(String realm, String username, String hashedPwd, String salt, String hashAlgorithmName, int hashIterations) {
        this.username = username;
        this.hashedPwd = hashedPwd;
        this.salt = salt;
        this.hashAlgorithmName = hashAlgorithmName;
        this.hashIterations = hashIterations;
        pc = new SimplePrincipalCollection(username, realm);
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return pc;
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

    public String getUsername() {
        return username;
    }
}
