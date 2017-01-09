package org.msc.main.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MainService {
    public Map<String, Object> hello(String name, int x) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("hello", name);
        result.put("x", x);
        return result;
    }

    public void error() {
        throw new RuntimeException("error...");
    }
}
