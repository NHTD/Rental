package com.example.server.security;

import com.example.server.exception.RentalHomeDataModelNotFoundException;
import com.example.server.models.User;
import com.example.server.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsServiceImp implements UserDetailsService {

    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByAccountType(username)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("User with {} account is not found", username));

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> {
                    Set<GrantedAuthority> authoritySet = role.getPermissions().stream()
                            .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                            .collect(Collectors.toSet());
                    authoritySet.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
                    return authoritySet.stream();
                }).collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getAccountType(), user.getPassword(), authorities);
    }
}
