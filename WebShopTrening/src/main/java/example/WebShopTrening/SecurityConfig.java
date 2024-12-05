/*
 * package example.WebShopTrening;
 * 
 * 
 * import org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.security.config.Customizer; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.annotation.web.configuration.
 * EnableWebSecurity; import org.springframework.security.core.userdetails.User;
 * import org.springframework.security.core.userdetails.UserDetails; import
 * org.springframework.security.core.userdetails.UserDetailsService; import
 * org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import
 * org.springframework.security.crypto.password.PasswordEncoder; import
 * org.springframework.security.provisioning.InMemoryUserDetailsManager; import
 * org.springframework.security.web.SecurityFilterChain;
 * 
 * @Configuration
 * 
 * @EnableWebSecurity public class SecurityConfig {
 * 
 * @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 * return http .authorizeHttpRequests(auth -> auth
 * .requestMatchers("/products/**") .authenticated())
 * .httpBasic(Customizer.withDefaults()) .build(); }
 * 
 * @Bean UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
 * User.UserBuilder users = User.builder(); UserDetails sarah = users
 * .username("sarah") .password(passwordEncoder.encode("abc123"))
 * .roles("ADMIN") // No roles for now .build();
 * 
 * UserDetails hank = users .username("hank")
 * .password(passwordEncoder.encode("qrs456")) .roles("USER") // new role
 * .build();
 * 
 * return new InMemoryUserDetailsManager(sarah,hank); }
 * 
 * @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();
 * } }
 */
