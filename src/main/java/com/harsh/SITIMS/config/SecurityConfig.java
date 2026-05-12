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

                        // ✅ Public endpoints — no login needed
                        .requestMatchers(
                                "/api/auth/**",
                                "/tips/submit",
                                "/tips/**",
                                "/api/tips/**",
                                "/login.html",
                                "/register.html",
                                "/dashboard.html",
                                "/home.html",
                                "/admin-dashboard.html",
                                "/admin-incidents.html",
                                "/admin-informant.html",
                                "/admin-add-informant.html",
                                "/admin-edit-informant.html",
                                "/admin-tips.html",
                                "/admin-user.html",
                                "/admin-officers.html",
                                "/add-officer.html",
                                "/admin-update-incident.html",
                                "/admin-user-update.html",
                                "/Officer-dashboard.html",
                                "/officer-incident-details.html",
                                "/officer-incident-list.html",
                               "/user-edit-profile.html",
                                "/admin-officer-update.html",
                                "/Officer-edit-profile.html",
                                "/Officer-tips.html",
                                "/update-incident.html",
                                "/submit-tip.html",
                                "/officer-update-status.html",
                                "/officer.html",
                                "/create-incident.html",
                               "/edit-officer.html","/my-incidents.html"


                        ).permitAll()

                        // ✅ FIXED — Admin only endpoints
                        .requestMatchers(
                                "/api/admin/**",
                                "/admin-dashboard.html",
                                "/admin-incidents.html",
                                "/admin-informant.html",
                                "/admin-add-informant.html",
                                "/admin-edit-informant.html",
                                "/admin-tips.html",
                                "/admin-user.html",
                                "/admin-officers.html",
                                "/add-officer.html",
                                "/admin-update-incident.html",
                                "/admin-user-update.html"
                        ).hasRole("ADMIN")

                        // ✅ FIXED — Officer only endpoints
                        .requestMatchers(
                                "/api/officer/**",
                                "/Officer-dashboard.html",
                                "/officer-incident-details.html",
                                "/officer-incident-list.html",
                                "/officer-profile.html",
                                "/Officer-tips.html",
                                "/edit-profile.html"
                        ).hasAnyRole("ADMIN", "OFFICER")

                        // ✅ All other requests need login
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
