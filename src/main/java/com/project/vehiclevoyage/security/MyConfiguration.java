package com.project.vehiclevoyage.security;

import com.project.vehiclevoyage.service.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfiguration {

    @Autowired
    private UserDetailsServiceImplementation userDetailsService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(requestMatcher -> requestMatcher
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/signup").permitAll()
                        .requestMatchers("/registration").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/home/**").permitAll()
                        .requestMatchers("/uploadimage").permitAll()
                        .requestMatchers("/vehicle/upload-image").permitAll()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/admin/**").permitAll()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(new CustomAuthenticationSuccessHandler())
                )
                .logout(logout -> logout.logoutUrl("/logout"));

        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsServiceImplementation UserDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(UserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
