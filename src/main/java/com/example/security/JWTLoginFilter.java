package com.example.security;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    public JWTLoginFilter(String url, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(url));
        System.out.println(">>> JWTLoginFilter.JWTLoginFilter()");
        System.out.println("url = " + url);
        System.out.println("authenticationManager = " + authenticationManager);
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException, ServletException {
        System.out.println(">>> JWTLoginFilter.attemptAuthentication()");
        try {
            AccountCredentials credentials = new ObjectMapper().readValue(req.getInputStream(), AccountCredentials.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            Collections.emptyList()
                    )
            );
        } catch (JsonMappingException e) {
            throw new AuthenticationCredentialsNotFoundException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
        System.out.println(">>> JWTLoginFilter.successfulAuthentication()");
        System.out.println("auth = " + auth);
        System.out.println("auth.getPrincipal() = " + auth.getPrincipal());
        System.out.println("auth.getCredentials() = " + auth.getCredentials());
        System.out.println("auth.getAuthorities() = " + auth.getAuthorities());
        TokenAuthenticationService.addAuthentication(res, auth.getName(), auth.getAuthorities());

        chain.doFilter(req, res);
        System.out.println("<<< JWTLoginFilter.successfulAuthentication()");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println(">>> JWTLoginFilter.unsuccessfulAuthentication()");
        System.out.println("failed = " + failed);
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
