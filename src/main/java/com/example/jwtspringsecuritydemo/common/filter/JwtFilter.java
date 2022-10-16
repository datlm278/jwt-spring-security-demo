package com.example.jwtspringsecuritydemo.common.filter;

import com.example.jwtspringsecuritydemo.common.config.TokenAuthentication;
import com.example.jwtspringsecuritydemo.common.constant.Constant;
import com.example.jwtspringsecuritydemo.common.utils.SecurityUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (ObjectUtils.isEmpty(authHeader) || !authHeader.startsWith(Constant.JWT_PREFIX)) {
            LOGGER.error("Cannot find valid JWT");
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.split(Constant.JWT_PREFIX)[1].trim();

        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(SecurityUtils.getPublicKey("publickey.pem"))
                    .build()
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            TokenAuthentication authentication = new TokenAuthentication(body, null);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            LOGGER.error("Token is not valid");
        } finally {
            filterChain.doFilter(request, response);
        }

    }
}
