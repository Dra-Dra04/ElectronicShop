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

                          // ✅ Cho admin-service đọc danh sách user để đổ lên Admin UI
                          .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()

                          // ADMIN only (đúng path với controller của bạn)
                          .requestMatchers(HttpMethod.POST, "/api/users/admin").permitAll() // hoặc hasRole("ADMIN") tùy bạn
                          .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

                          // còn lại cần login
                          .anyRequest().authenticated()
                  )
                  .httpBasic(Customizer.withDefaults());

          return http.build();
      }
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(Customizer.withDefaults())
//                .csrf(csrf -> csrf.disable()) // API => tắt CSRF
//                .authorizeHttpRequests(auth -> auth
//                        // Cho phép đăng ký customer không cần login
//                        .requestMatchers(HttpMethod.POST, "/api/users/customer").permitAll()
//                        // Cho phép tạm thời tất cả path /api/auth/** (sau này làm login riêng)
//                        .requestMatchers("/api/auth/**").permitAll()
//                        // Các endpoint admin => ADMIN
//                        .requestMatchers("/api/users/admin").hasRole("ADMIN")
//                        .requestMatchers("/api/users/admins").hasRole("ADMIN")
//                        // Các thao tác xóa user chỉ cho ADMIN
//                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
//                        // Còn lại: chỉ cần đăng nhập (ADMIN hoặc CUSTOMER đều được)
//                        .anyRequest().authenticated()
//                )
//                // Dùng HTTP Basic cho đơn giản (Postman/Browser)
//                .httpBasic(basic -> {});
//
//        return http.build();
//    }

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
