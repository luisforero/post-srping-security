package com.example.postspringsecurity.security;

import com.example.postspringsecurity.auth.services.MongoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    final MongoUserDetailsService mongoUserDetailsService;

    @Autowired
    public ApplicationSecurityConfig(MongoUserDetailsService mongoUserDetailsService) {
        this.mongoUserDetailsService = mongoUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .csrf()// CROSS SITE REQUEST FORGERY IT'S ENABLED BY DEFAULT.
//                WE HAVE TO DISABLE IT IF THE API IS EXPOSED TO FINAL CLIENTS OR WEB
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .anyRequest().authenticated()
                .and()
                //IF WE WANT TO USE THE FORM LOGIN FOR OUR SITE WE HAVE TO SET UP THE STRATEGY DEFINED FOR THAT.
//                .formLogin()
//                .loginPage("/login").permitAll()
//                .defaultSuccessUrl("/api/v1/posts")
//                .and()
//                .rememberMe()//Default to 2 weeks
//                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
//                .key("S0m3Th1n6R341YStR0n6")
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
//                .permitAll()
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .deleteCookies("JSESSIONID", "remember-me")
//                .logoutSuccessUrl("/login")
                // WE JUST WANT TO ENABLE THE HTTP BASIC SINCE WE ARE JUST USING AS AN API.
                .httpBasic()
                .and().sessionManagement().disable() // Tells Spring not to hold session information for users, as this is uneccesary in an API
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(mongoUserDetailsService);
    }

    // IF WE WANT TO MANAGE SOME IN MEMORY USERS.
    //    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails luwifor = User.builder()
//                .username("luwifor")
//                .password(passwordEncoder.encode("symphony"))
////                .roles(VIEWER.name())//ROLE_VIEWER
//                .authorities(VIEWER.getGrantedAuthorities())
//                .build();
//        UserDetails katya = User.builder()
//                .username("katya")
//                .password(passwordEncoder.encode("symphony"))
////                .roles(AUTHOR.name())//ROLE_AUTHOR
//                .authorities(AUTHOR.getGrantedAuthorities())
//                .build();
//        UserDetails ludwig = User.builder()
//                .username("ludwig")
//                .password(passwordEncoder.encode("symphony"))
////                .roles(ADMIN.name())//ROLE_ADMIN
//                .authorities(ADMIN.getGrantedAuthorities())
//                .build();
//        return new InMemoryUserDetailsManager(luwifor, katya, ludwig);
//    }
}
