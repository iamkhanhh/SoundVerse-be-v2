package com.TLU.SoundVerse.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS = { "/users", "/auth/**" };

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS)
                .permitAll()
                .anyRequest()
                .authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())));

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS256");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    // @Bean
    // public Filter jwtAuthenticationFilter() {
    //     return (request, response, chain) -> {
    //         // Lấy JWT từ Cookie
    //         String token = getTokenFromCookie((HttpServletRequest) request);

    //         if (token != null) {
    //             try {
    //                 // Giải mã JWT
    //                 Jwt jwt = jwtDecoder().decode(token);
    //                 String userId = jwt.getClaim("sub"); // "sub" thường là ID người dùng

    //                 // Tạo đối tượng UserDetails
    //                 User userDetails = new User(userId, "", Collections.emptyList());

    //                 // Lưu vào Security Context
    //                 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
    //                         null, userDetails.getAuthorities());
    //                 SecurityContextHolder.getContext().setAuthentication(authToken);

    //                 // Lưu vào request.user
    //                 request.setAttribute("user", userDetails);
    //             } catch (Exception e) {
    //                 System.out.println("Invalid JWT: " + e.getMessage());
    //             }
    //         }

    //         // Tiếp tục xử lý request
    //         chain.doFilter(request, response);
    //     };
    // }

    // private String getTokenFromCookie(HttpServletRequest request) {
    //     Cookie[] cookies = request.getCookies();
    //     if (cookies != null) {
    //         for (Cookie cookie : cookies) {
    //             if ("access_token".equals(cookie.getName())) {
    //                 return cookie.getValue();
    //             }
    //         }
    //     }
    //     return null;
    // }
}
