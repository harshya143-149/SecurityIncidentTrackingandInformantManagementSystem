package com.harsh.SITIMS.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Clean URLs — no .html needed in browser
        registry.addRedirectViewController("/", "/home.html");
        registry.addRedirectViewController("/home", "/home.html");
        registry.addRedirectViewController("/login", "/login.html");
        registry.addRedirectViewController("/register", "/register.html");
        registry.addRedirectViewController("/dashboard", "/dashboard.html");
        registry.addRedirectViewController("/admin", "/admin-dashboard.html");
        registry.addRedirectViewController("/admin/incidents", "/admin-incidents.html");
        registry.addRedirectViewController("/admin/officers", "/admin-officers.html");
        registry.addRedirectViewController("/admin/informants", "/admin-informant.html");
        registry.addRedirectViewController("/admin/tips", "/admin-tips.html");
        registry.addRedirectViewController("/admin/users", "/admin-user.html");
        registry.addRedirectViewController("/officer", "/Officer-dashboard.html");
        registry.addRedirectViewController("/officer/incidents", "/officer-incident-list.html");
        registry.addRedirectViewController("/my-incidents", "/my-incidents.html");
        registry.addRedirectViewController("/create-incident", "/create-incident.html");
        registry.addRedirectViewController("/submit-tip", "/submit-tip.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
