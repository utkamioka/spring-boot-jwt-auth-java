package com.example.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import sun.security.krb5.internal.crypto.Nonce;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

class TokenAuthenticationService {
    static final long EXPIRATION_TIME = 3 * 60 * 1000L; // 1 minutes
    static final String SECRET = "ThisIsASecret";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    static void addAuthentication(HttpServletResponse res, String username, Collection<? extends GrantedAuthority> authorities) {
        System.out.println(">>> TokenAuthenticationService.addAuthentication()");
        System.out.println("username = " + username);
        System.out.println("authorities = " + authorities);

        // TODO: authoritiesをトークンに格納する？
        List<String> x = authorities.stream().map(Objects::toString).map(String::toLowerCase).collect(Collectors.toList());
        System.out.println("x = " + x);
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", x);

        String JWT = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
    }

    static Authentication getAuthentication(HttpServletRequest request) {
        System.out.println(">>> TokenAuthenticationService.getAuthentication()");
        String token = request.getHeader(HEADER_STRING);
        System.out.println("token = " + token);
        if (token != null) {
            // parse the token.
            try {
                String user = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                        .getBody()
                        .getSubject();

                // TODO: どうやって該当ユーザのロールを取得して、設定するのか
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                authorities.add(new SimpleGrantedAuthority("ROLE_XXX"));

                return user != null ?
                        new UsernamePasswordAuthenticationToken(user, null, authorities) :
                        null;
            } catch (SignatureException e) {
                // TODO: tokenが不正（この例外はHTTPレスポンスに反映されていない）
                throw new BadCredentialsException("XXX", e);
            } catch (ExpiredJwtException e) {
                // TODO: tokenが期限切れ（この例外はHTTPレスポンスに反映されていない）
                throw new NonceExpiredException("XXX", e);
            }
        }
        return null;
    }
}
