package com.example.server.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.*;

public class JwtProvider {

    public static String generateToken(Authentication authentication) {
        SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String roles = populate(authorities);

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()*86400000))
                .claim("accountType", authentication.getName())
                .claim("authorities", roles)
                .signWith(key)
                .compact();
    }

    private static String populate(Collection<? extends GrantedAuthority> authorities) {
        Set<String> set = new HashSet<>();

        for(GrantedAuthority authority : authorities){
            set.add(authority.getAuthority());
        }

        return String.join(",", set);
    }
}
