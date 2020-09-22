package com.duo.book;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {
    private static final Logger log = LoggerFactory.getLogger(Database.class);

    @Bean
    CommandLineRunner initDatabase(BookRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Book("Harry Potter", "Bloomsbury Publishing")));
            log.info("Preloading " + repository.save(new Book("Stoorzender", "Podium")));
        };
    }
}
