package com.example.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JWTAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println(">>> JWTAuthenticationFilter.doFilter()");
        Authentication authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest)request);
        System.out.println("authentication = " + authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);

        // https://openid-foundation-japan.github.io/rfc6750.ja.html
        // 保護リソースへのリクエストが, 認証クレデンシャルを含んでいない,
        // または保護リソースへアクセスすることができるアクセストークンを含んでいない場合,
        // リソースサーバはHTTP WWW-Authenticate レスポンスヘッダフィールドを含めなければならない (MUST).
        // 同様に, その他の条件下でもリソースサーバはそれをレスポンスに含めてよい (MAY).
        // WWW-Authenticate ヘッダフィールドはHTTP/1.1 [RFC2617] で定義されているフレームワークを使用する.

//        if (authentication != null) {
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            filterChain.doFilter(request, response);
//        } else {
//            javax.servlet.http.HttpServletResponse x = (javax.servlet.http.HttpServletResponse)response;
//            x.setStatus(javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED);
//            x.setHeader("WWW-Authenticate", "Bearer realm=\"example\"");
//        }
    }
}
