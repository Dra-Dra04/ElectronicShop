package com.ecomelectronics.customerservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          http
                  .cors(Customizer.withDefaults())
                  .csrf(AbstractHttpConfigurer::disable)
                  .authorizeHttpRequests(auth -> auth

                          // PUBLIC
                          .requestMatchers("/api/auth/**").permitAll()
                          .requestMatchers(HttpMethod.POST, "/api/users/customer").permitAll()
                          .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")

                          // ADMIN only
                          .requestMatchers(HttpMethod.POST, "/api/users/admin").hasRole("ADMIN")
                          .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                          .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

                          .anyRequest().authenticated()
                  )
                  .httpBasic(Customizer.withDefaults());

          return http.build();
      }

    // Password encoder sử dụng BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager (dù hiện tại chưa dùng nhiều, nhưng để sẵn cho tương lai)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
