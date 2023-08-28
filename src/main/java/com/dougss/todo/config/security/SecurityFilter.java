package com.dougss.todo.config.security;

import com.dougss.todo.service.TokenService;
import com.dougss.todo.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    final TokenService tokenService;
    final UserService userService;

    public SecurityFilter(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var toekn = this.recoverToekn(request);
        if(toekn!= null) {
            var subject = tokenService.validateToken(toekn);
            UserDetails userDetails = userService.findByUserName(subject);

            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToekn(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) {
            return null;
        }
        if(!authHeader.contains("Bearer")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
