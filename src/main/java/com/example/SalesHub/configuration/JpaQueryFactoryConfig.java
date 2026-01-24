package com.example.SalesHub.configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaQueryFactoryConfig {

    @Bean
    public JPAQueryFactory factory (EntityManager manager){
        return new JPAQueryFactory(manager);
    }
}
