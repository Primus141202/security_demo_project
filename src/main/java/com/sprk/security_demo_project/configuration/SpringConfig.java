package com.sprk.security_demo_project.configuration;

import com.sprk.security_demo_project.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SpringConfig {

    // In Memory Users
    /*@Bean
    public UserDetailsService userDetailsService() {
        UserDetails normal = User.withUsername("abdul")
                .password("{noop}1234")
                .roles("USER")
                .build(); // builder approach
        UserDetails admin = User.withUsername("rutik")
                .password("{noop}1234")
                .roles("USER", "ADMIN")
                .build();
        UserDetails tester = User.withUsername("shoaib")
                .password("{noop}1234")
                .roles("USER", "TESTER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(normal, admin, tester);
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());
        http.authorizeHttpRequests(req -> req
                .requestMatchers("/home", "/test","/signup")
                .permitAll()
                .anyRequest()
                .authenticated()
        );

        http.httpBasic(Customizer.withDefaults());
        http.formLogin(Customizer.withDefaults());
        http.logout(Customizer.withDefaults());
        return http.build();
    }

    // We nee password encoder object in IOC container
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        System.out.println("Auth Provider Called");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(getPasswordEncoder());
        authProvider.setUserDetailsService(userDetailsService());
        return authProvider;
    }
}