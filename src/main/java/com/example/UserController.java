package com.example;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableGlobalMethodSecurity(securedEnabled = true)
public class UserController {
    /* Maps to all HTTP actions by default (GET,POST,..)*/
    @Secured("ROLE_ADMIN")
    @RequestMapping("/users")
    @ResponseBody
    public String getUsers(Authentication auth, @AuthenticationPrincipal User user) {
        System.out.println(">>> UserController.getUsers()");
        System.out.println("auth = " + auth);
        System.out.println("auth.getPrincipal() = " + auth.getPrincipal());
        System.out.println("auth.getCredentials() = " + auth.getCredentials());
        System.out.println("auth.getAuthorities() = " + auth.getAuthorities());
        System.out.println("user = " + user);
        System.out.println("authorities = " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return "{\"users\":[{\"firstname\":\"Richard\", \"lastname\":\"Feynman\"}," +
                "{\"firstname\":\"Marie\",\"lastname\":\"Curie\"}]}";
    }
}
