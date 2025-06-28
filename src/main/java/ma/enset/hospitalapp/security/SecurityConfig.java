package ma.enset.hospitalapp.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder){
        String encodedPassword = passwordEncoder.encode("1234");
        System.out.println(encodedPassword);
        return new InMemoryUserDetailsManager(
            User.withUsername("user1").password(encodedPassword).roles("USER").build(),
            User.withUsername("user2").password(encodedPassword).roles("USER").build(),
            User.withUsername("admin").password(encodedPassword).roles("USER","ADMIN").build()
        );
    }

    @Bean
    public AuthenticationManager authenticationManager(@Qualifier("inMemoryUserDetailsManager") UserDetailsService userDetailsService, 
                                                      PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .formLogin(Customizer.withDefaults())
            .authorizeHttpRequests(ar->ar.requestMatchers("/deletePatient/**").hasRole("ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/admin/**").hasRole("ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/user/**").hasRole("USER"))
            .authorizeHttpRequests(ar->ar.anyRequest().authenticated())
            .exceptionHandling(eh -> eh.accessDeniedPage("/403"))
            .build();
    }
}
