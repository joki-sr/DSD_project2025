package com.example.factorial.src.filter;

import com.example.factorial.src.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT认证过滤器
 * 拦截所有请求，验证JWT令牌并设置安全上下文
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // 从请求头中获取Authorization
        final String authHeader = request.getHeader("Authorization");
        
        // 检查请求头是否包含Bearer令牌
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // 提取令牌
        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);
        
        // 检查用户名和当前安全上下文
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 从UserDetailsService加载用户详情
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            
            // 验证令牌有效性
            if (jwtService.validateToken(jwt)) {
                // 获取令牌中的角色
                List<String> roles = jwtService.extractRoles(jwt);
                
                // 创建认证对象，使用从令牌中提取的角色
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
                );
                
                // 设置认证详情
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 更新安全上下文
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 继续过滤链
        filterChain.doFilter(request, response);
    }
} 