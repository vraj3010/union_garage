package com.example.spring_boot;

// import org.hibernate.annotations.DialectOverride.OverridesAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application{
	@Autowired
	private JdbcTemplate j;
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		System.out.println("Welcome to Basics of Spring Boot");
	}
}
