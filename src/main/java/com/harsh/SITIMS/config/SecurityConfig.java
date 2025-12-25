package com.harsh.SITIMS.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.harsh.SITIMS.service.impl.UserDetailsServiceImpl;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider())
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/tips/**",

                                // ✅ Required to allow tip submission
                                "/tips/submit",
                                "/tips/**",

                                "/login.html",
                                "my-incidents.html",
                                "/admin-incidents.html",
                                "/admin-informant.html",
                                "/admin-add-informant.html",
                                "/admin-edit-informant.html",

                                "/admin-tips.html",
                                "/register.html",
                                "/edit-officer.html",
                                "/admin-user.html",
                                "/admin-officers.html",
                                "/add-officer.html",
                                "/dashboard.html",
                                "/create-incident.html",
                                "/update-incident.html",
                                "/admin-dashboard.html",
                                "/admin-update-incident.html",
                                "/admin-user.html",
                                "/Officer-dashboard.html",
                                "/officer-incident-details.html",
                                "/officer-incident-list.html",
                                "/officer-profile.html",
                                "/admin-user-update.html",
                                "/admin-tips.html",
                                "/Officer-tips.html",
                                "/home.html",
                                "/submit-tip.html","/officer-incident-details.html","/edit-profile.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
