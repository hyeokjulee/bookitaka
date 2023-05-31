package com.bookitaka.NodeulProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ImportAutoConfiguration
@EnableJpaAuditing
@SpringBootApplication
public class NodeulProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(NodeulProjectApplication.class, args);
	}
}