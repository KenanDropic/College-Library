package com.library;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
//@ComponentScans({@ComponentScan("com.library.controller"),@ComponentScan("com.library.config")})
@EntityScan("com.library.entity")
@EnableJpaRepositories(basePackages = "com.library.repository")
@EnableWebSecurity(debug = false)
public class Main {
    public static void main(String [] args){

        SpringApplication.run(Main.class,args);
    }
}
