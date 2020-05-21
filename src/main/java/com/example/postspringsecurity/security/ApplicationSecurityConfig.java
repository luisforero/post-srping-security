package com.example.postspringsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.example.postspringsecurity.security.ApplicationUserPermission.*;
import static com.example.postspringsecurity.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//TODO explain
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
//                .antMatchers("/api/**").hasRole(ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/api/v1/posts/comment").hasAnyAuthority(VIEWER_COMMENT.getPermission(), AUTHOR_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE, "/api/**").hasAuthority(ADMIN_REMOVE.getPermission())
                .antMatchers(HttpMethod.POST, "/api/**").hasAuthority(AUTHOR_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT, "/api/**").hasAuthority(AUTHOR_WRITE.getPermission())
                .antMatchers(HttpMethod.GET, "/api/**").hasAnyRole(ADMIN.name(), AUTHOR.name(), VIEWER.name())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout().permitAll()
                .and()
                .httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails luwifor = User.builder()
                .username("luwifor")
                .password(passwordEncoder.encode("symphony"))
//                .roles(VIEWER.name())//ROLE_VIEWER
                .authorities(VIEWER.getGrantedAuthorities())
                .build();
        UserDetails katya = User.builder()
                .username("katya")
                .password(passwordEncoder.encode("symphony"))
//                .roles(AUTHOR.name())//ROLE_AUTHOR
                .authorities(AUTHOR.getGrantedAuthorities())
                .build();
        UserDetails ludwig = User.builder()
                .username("ludwig")
                .password(passwordEncoder.encode("symphony"))
//                .roles(ADMIN.name())//ROLE_ADMIN
                .authorities(ADMIN.getGrantedAuthorities())
                .build();
        return new InMemoryUserDetailsManager(luwifor, katya, ludwig);
    }
}
