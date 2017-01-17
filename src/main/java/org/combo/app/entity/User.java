package org.combo.app.entity;

public class User {

    private String id;

    private String username;

    private String hashedPwd;

    private String salt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPwd() {
        return hashedPwd;
    }

    public void setHashedPwd(String hashedPwd) {
        this.hashedPwd = hashedPwd;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
