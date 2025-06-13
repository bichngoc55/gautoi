package com.example.gautoi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class GautoiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GautoiApplication.class, args);
	}

}
