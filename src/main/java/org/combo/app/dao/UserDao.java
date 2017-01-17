package org.combo.app.dao;

import org.combo.app.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserDao {
    private Map<String, User> db = new ConcurrentHashMap<String, User>();

    public void save(User user) {
        db.put(user.getUsername(), user);
    }

    public User find_username(String username) {
        return db.get(username);
    }
}
