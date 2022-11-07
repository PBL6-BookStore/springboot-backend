package com.pbl6.bookstore.security;

import com.pbl6.bookstore.common.constant.BookStorePermission;
import com.pbl6.bookstore.filter.CustomAuthenticationFilter;
import com.pbl6.bookstore.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author vndat00
 * @since 05/11/2022
 */

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static String ADMIN = BookStorePermission.Role.ADMIN;
    private static String USER = BookStorePermission.Role.USER;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/login", "/token/refresh/**","/swagger-ui/**", "/v3/api-docs/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/categories","/books/**").hasAnyAuthority(ADMIN,USER);
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/categories","/books","/accounts/**").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/categories/**", "/books/images/**","/books/**").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/categories/**", "/books/**").hasAuthority(ADMIN);
        http.authorizeRequests().anyRequest().authenticated();

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/swagger-ui/**", "/v3/api-docs/**");
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
