package ma.xproce.getrich.config;

import ma.xproce.getrich.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

@EnableWebSecurity
@Configuration

public class SecurityConfig {

    private final JwtService jwtService;
    private final MyUserDetailsService myUserDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final AuthenticationProvider authenticationProvider;


    @Autowired
    public SecurityConfig(
            JwtService jwtService,
            MyUserDetailsService myUserDetailsService,
            HandlerExceptionResolver handlerExceptionResolver,
            AuthenticationProvider authenticationProvider
    ) {
        this.jwtService = jwtService;
        this.myUserDetailsService = myUserDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.authenticationProvider = authenticationProvider;
    }
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
    ) {
        return new JwtAuthenticationFilter(jwtService, myUserDetailsService, handlerExceptionResolver);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET","POST"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }




}