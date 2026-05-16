package com.harsh.SITIMS.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.harsh.SITIMS.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity
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
                                "/admin-officer-update.html",
                                "/Officer-dashboard.html",
                                "/officer-incident-details.html",
                                "/officer-incident-list.html",
                                "/user-edit-profile.html",
                                "/incident-history.html",
                                "/Officer-edit-profile.html",
                                "/Officer-tips.html",
                                "/update-incident.html",
                                "/submit-tip.html",
                                "/officer-update-status.html",
                                "/officer.html",
                                "/create-incident.html",
                                "/informant-dashboard.html",
                                "/informant-edit-profile.html",
                                "/edit-officer.html",
                                "/edit-tip.html",
                                "/my-incidents.html",
                                "/user-update-incidents.html",
                                "/my-tips.html"
                        ).permitAll()

                        // ✅ USER + ADMIN incident endpoints
                        .requestMatchers(
                                "/api/incidents/create",
                                "/api/incidents/user/**",
                                "/api/incidents/my",
                                "/api/incidents/update/**",
                                "/api/incidents/delete/**"
                        ).hasAnyRole("USER", "ADMIN")

                        // ✅ Admin only API endpoints
                        .requestMatchers(
                                "/api/admin/**",
                                "/api/incidents/all",
                                "/api/incidents/assign"
                        ).hasRole("ADMIN")

                        // ✅ Officer only API endpoints
                        .requestMatchers(
                                "/api/officer/**"
                        ).hasAnyRole("ADMIN", "OFFICER")

                        // ✅ All other requests need login
                        .anyRequest().authenticated()
                )

                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}