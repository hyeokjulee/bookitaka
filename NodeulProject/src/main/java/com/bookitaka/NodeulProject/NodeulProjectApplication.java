package com.bookitaka.NodeulProject;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.context.annotation.Bean;

//@ImportAutoConfiguration
@EnableJpaAuditing
@SpringBootApplication
public class NodeulProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(NodeulProjectApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}