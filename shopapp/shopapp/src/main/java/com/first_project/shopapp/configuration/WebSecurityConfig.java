package com.first_project.shopapp.configuration;

import com.first_project.shopapp.models.Role;
import com.first_project.shopapp.filters.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebMvc
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    JwtTokenFilter filter;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request->{
                    request
                            .requestMatchers(
                                    String.format("%s/user/register", apiPrefix),
                                    String.format("%s/user/login", apiPrefix)
                            )
                            .permitAll()
                            .requestMatchers(GET,
                                    String.format("%s/role",apiPrefix))
                            .permitAll()
                            .requestMatchers(GET,
                                    String.format("%s/categories/**", apiPrefix))
                            .permitAll()

                            .requestMatchers(POST,
                                    String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                            .requestMatchers(PUT,
                                    String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                            .requestMatchers(DELETE,
                                    String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                            .requestMatchers(GET,
                                    String.format("%s/products/**", apiPrefix))
                            .permitAll()

                            .requestMatchers(POST,
                                    String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                            .requestMatchers(PUT,
                                    String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                            .requestMatchers(DELETE,
                                    String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.ADMIN)


                            .requestMatchers(POST,
                                    String.format("%s/order/**", apiPrefix)).hasAnyRole(Role.USER)

                            .requestMatchers(GET,
                                    String.format("%s/order/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)

                            .requestMatchers(PUT,
                                    String.format("%s/order/**", apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(DELETE,
                                    String.format("%s/order/**", apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(POST,
                                    String.format("%s/detail_order/**", apiPrefix)).hasAnyRole(Role.USER)

                            .requestMatchers(GET,
                                    String.format("%s/detail_order/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)

                            .requestMatchers(PUT,
                                    String.format("%s/detail_order/**", apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(DELETE,
                                    String.format("%s/detail_order/**", apiPrefix)).hasRole(Role.ADMIN)

                            .anyRequest().authenticated();

                })
                .csrf(AbstractHttpConfigurer::disable);
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return http.build();
    }
}