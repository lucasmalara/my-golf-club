package com.mygolfclub.security.config;

import com.mygolfclub.utils.roles.RoleTypes;
import com.mygolfclub.service.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import static com.mygolfclub.utils.constants.ConstantsProvider.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
public class SecurityConfiguration {

    private static final String EMPLOYEE = RoleTypes.EMPLOYEE.getValue();
    private static final String MODERATOR = RoleTypes.MODERATOR.getValue();
    private static final String ADMIN = RoleTypes.ADMIN.getValue();

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher(API + "/**")
                .authorizeHttpRequests(authorize -> authorize
                        // GET
                        .requestMatchers(GET, MEMBERS_API + "/**")
                        .hasRole(EMPLOYEE)
                        // POST
                        .requestMatchers(POST, MEMBERS_API)
                        .hasRole(MODERATOR)
                        // PUT
                        .requestMatchers(PUT, MEMBERS_API + "/**")
                        .hasRole(ADMIN)
                        // DELETE
                        .requestMatchers(DELETE, MEMBERS_API + "/**")
                        .hasRole(ADMIN)
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public SecurityFilterChain viewFilterChain(HttpSecurity http,
                                               AuthenticationSuccessHandler handler) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        // HOME PAGE & READ members
                        .requestMatchers(HOME, HOME + "members/list/**")
                        .hasRole(EMPLOYEE)
                        // CREATE member
                        .requestMatchers(HOME + "members/add")
                        .hasRole(MODERATOR)
                        // Global access
                        .requestMatchers(HOME + "members/**", HOME + "/users/**")
                        .hasRole(ADMIN)
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticate")
                        .successHandler(handler)
                        .permitAll())
                .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(handle -> handle
                        .accessDeniedPage("/access-denied"));
        return http.build();
    }

}
