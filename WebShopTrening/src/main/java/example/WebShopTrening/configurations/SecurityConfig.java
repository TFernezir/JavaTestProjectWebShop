package example.WebShopTrening.configurations;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity 
public class SecurityConfig {
	
	JwtAuthEntiryPoint authEntiryPoint;
	CustomUserDetailService userDetailService;

    public SecurityConfig(CustomUserDetailService userDetailService,JwtAuthEntiryPoint authEntiryPoint) {
		this.userDetailService = userDetailService;
		this.authEntiryPoint = authEntiryPoint;
	}


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> 
		            exception.authenticationEntryPoint(authEntiryPoint)
		        )
		        .sessionManagement(session -> 
		            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		        )
            .authorizeHttpRequests(auth -> auth
            	    .requestMatchers("/h2-console/**").permitAll()
            	    .requestMatchers("/auth/**").permitAll()
            	    .requestMatchers("/actuator/**").permitAll()
            	    .requestMatchers("/products/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                    .requestMatchers("/basket/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN") 
            	    .anyRequest().authenticated()
            	)
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            )
            .httpBasic(withDefaults())
            .addFilterBefore(jwtAuthentificationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/h2-console/**").permitAll()
//                .requestMatchers("/auth/**").permitAll()
//                .anyRequest().authenticated()
//            )
//            .headers(headers -> headers
//                .frameOptions(frameOptions -> frameOptions.sameOrigin())
//            )
//            .sessionManagement(session -> session
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            )
//            .addFilterBefore(jwtAuthentificationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    AuthenticationManager authenticationManager(
    		AuthenticationConfiguration authenticationConfiguration) throws Exception
    {
    	return authenticationConfiguration.getAuthenticationManager();
	}
    
    @Bean
    public JwtAuthentificationFilter jwtAuthentificationFilter() {
    	return new JwtAuthentificationFilter();
    }
}