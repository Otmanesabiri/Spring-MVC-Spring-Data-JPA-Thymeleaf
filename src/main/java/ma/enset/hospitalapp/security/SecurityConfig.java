package ma.enset.hospitalapp.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
            .formLogin(formLogin -> formLogin
                .defaultSuccessUrl("/patients", true)
                .permitAll()
            )
            .authorizeHttpRequests(ar->ar.requestMatchers("/deletePatient/**").hasRole("ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/admin/**").hasRole("ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/medecins/**").hasAnyRole("USER", "ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/infirmiers/**").hasAnyRole("USER", "ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/personnel-administratif/**").hasAnyRole("USER", "ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/techniciens/**").hasAnyRole("USER", "ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/consultations/**").hasAnyRole("USER", "ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/suivis/**").hasAnyRole("USER", "ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/rendez-vous/**").hasAnyRole("USER", "ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/creneaux-horaires/**").hasAnyRole("USER", "ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/dossiers-medicaux/**").hasAnyRole("USER", "ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/dossier/**").hasAnyRole("USER", "ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/historique/**").hasAnyRole("USER", "ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/prescription/**").hasAnyRole("USER", "ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/examen/**").hasAnyRole("USER", "ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/allergie/**").hasAnyRole("USER", "ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/note/**").hasAnyRole("USER", "ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/antecedent/**").hasAnyRole("USER", "ADMIN"))
            .authorizeHttpRequests(ar->ar.requestMatchers("/user/**").hasRole("USER"))
            .authorizeHttpRequests(ar->ar.anyRequest().authenticated())
            .exceptionHandling(eh -> eh.accessDeniedPage("/403"))
            .build();
    }
}
