package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@RestController
	@RequiredArgsConstructor
	class HelloController {

		private final RestTemplate restTemplate;

		private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
//		private final SleepService sleepService;

		@GetMapping("/hello")
		public String hello() {
			LOGGER.info("---------Hello method started---------");
			LOGGER.error("---------Hello method started, id missing---------");
			ResponseEntity<String> responseEntity = this.restTemplate.postForEntity("https://httpbin.org/post", "Hello, Cloud!", String.class);
			return responseEntity.getBody();
		}

		@GetMapping("/exception")
		public String exception() {
			throw new IllegalArgumentException("This id is invalid");
		}

//		@GetMapping("/sleep")
//		public Long sleep(@RequestParam Long ms) {
//			Long result = this.sleepService.doSleep(ms);
//			return result;
//		}

		@ExceptionHandler(value = { IllegalArgumentException.class })
		protected ResponseEntity<String> handleConflict(IllegalArgumentException ex) {
			LOGGER.error(ex.getMessage(), ex);
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
}
