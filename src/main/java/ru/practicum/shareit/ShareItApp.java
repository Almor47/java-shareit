package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
public class ShareItApp {

	public static void main(String[] args) {
		SpringApplication.run(ShareItApp.class, args);
	}

}
