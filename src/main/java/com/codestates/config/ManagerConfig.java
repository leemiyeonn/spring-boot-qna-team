package com.codestates.config;

import com.codestates.member.Member;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import static com.codestates.member.Member.Role.MANAGER;

@Configuration
public class ManagerConfig {
    private EntityManager em;
    private EntityTransaction tx;

    @Bean
    public CommandLineRunner ManagerInit(EntityManagerFactory emFactory) {
        this.em = emFactory.createEntityManager();

        this.tx = em.getTransaction();

        return args -> {
            tx.begin();
            Member member = new Member("admin@gmail.com","admin","1234");
            member.setRole(MANAGER);
            em.persist(member);
            tx.commit();
        };
    }
}