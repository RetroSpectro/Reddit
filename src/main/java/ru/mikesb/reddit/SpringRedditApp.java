package ru.mikesb.reddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringRedditApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringRedditApp.class, args);
    }

}