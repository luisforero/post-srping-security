package com.example.postspringsecurity.auth.services;

import com.example.postspringsecurity.auth.models.Users;
import com.example.postspringsecurity.auth.repositories.UsersRepository;
import com.example.postspringsecurity.security.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MongoUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(
                    String.format("Username '%s' not found.", username)
            );
        }

        ApplicationUserRole userRole = ApplicationUserRole.valueOf(user.role);

        return User.builder()
                .username(user.username)
                .password(user.password)
                .authorities(userRole.getGrantedAuthorities())
                .build();
    }
}
