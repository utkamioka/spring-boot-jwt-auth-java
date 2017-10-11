package com.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Login {
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map login(HttpServletResponse resp) {
        System.out.println("Login.login()");
        Map<String,Object> result = new HashMap<>();
        result.put("status", true);
        result.put("access_token", resp.getHeader("Authorization").replace("Bearer ", ""));
        return result;
    }
}
