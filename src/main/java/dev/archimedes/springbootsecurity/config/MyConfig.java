package dev.archimedes.springbootsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class MyConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder){
        UserDetails amdin = User.withUsername("Ayush Jaiswal")
                .password(encoder.encode("admin@123"))
                .roles("ADMIN", "USER")
                .build();

        UserDetails user1 = User.withUsername("Rajesh Kumar")
                .password(encoder.encode("user@123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(amdin, user1);
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration builder) throws Exception{
        return builder.getAuthenticationManager();
    }
}
