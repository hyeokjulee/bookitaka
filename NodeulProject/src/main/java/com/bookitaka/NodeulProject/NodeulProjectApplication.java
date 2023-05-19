package com.bookitaka.NodeulProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NodeulProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(NodeulProjectApplication.class, args);
	}

}
