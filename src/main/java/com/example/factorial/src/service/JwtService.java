package com.example.factorial.src.service;

import com.example.factorial.src.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret:defaultSecretKeyThatShouldBeAtLeast32CharactersLong}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}") // 默认24小时
    private long jwtExpiration;

    // 从令牌中提取用户名
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 从令牌中提取到期日期
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    // 从令牌中提取角色列表
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> {
            List<String> roles = (List<String>) claims.get("roles");
            return roles != null ? roles : Collections.emptyList();
        });
    }

    // 从令牌中提取任意声明
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 解析令牌获取所有声明
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 检查令牌是否已过期
    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // 生成令牌，使用User实体
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        // 添加用户角色和权限信息到声明中
        claims.put("isAdmin", user.isAdmin());
        claims.put("isDoctor", user.isDoctor());
        claims.put("isPatient", user.isPatient());
        claims.put("id", user.getId());
        claims.put("name", user.getName());
        
        // 添加角色列表
        List<String> roles = new ArrayList<>();
        if (user.isAdmin()) roles.add("ROLE_ADMIN");
        if (user.isDoctor()) roles.add("ROLE_DOCTOR");
        if (user.isPatient()) roles.add("ROLE_PATIENT");
        if (user.getUserType() != null && !user.getUserType().isEmpty()) {
            roles.add("ROLE_" + user.getUserType());
        }
        claims.put("roles", roles);
        
        return createToken(claims, user.getUsername());
    }
    
    // 生成令牌，使用用户名和权限列表
    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        Map<String, Object> claims = new HashMap<>();
        
        // 从权限中提取角色信息
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        
        // 添加基本的角色标志
        claims.put("isAdmin", roles.contains("ROLE_ADMIN"));
        claims.put("isDoctor", roles.contains("ROLE_DOCTOR"));
        claims.put("isPatient", roles.contains("ROLE_PATIENT"));
        claims.put("roles", roles);
        
        return createToken(claims, username);
    }

    // 创建令牌
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 验证令牌
    public boolean validateToken(String token) {
        try {
            // 检查令牌是否已过期
            if (isTokenExpired(token)) {
                return false;
            }
            
            // 验证令牌签名
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
                
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 获取签名密钥
    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
} 