package com.example.postspringsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

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
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        User.UserBuilder luwi = User.builder()
                .username("luwifor")
                .password(passwordEncoder.encode("password"))
                .roles("VIEWER");//ROLE VIEWER
        UserDetails katya = User.builder()
                .username("katya")
                .password(passwordEncoder.encode("sirena"))
                .roles("AUTHOR")
                .build();
        UserDetails ludwig = User.builder()
                .username("ludwig")
                .password(passwordEncoder.encode("symphony"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(luwi.build(), katya, ludwig);
    }
}
