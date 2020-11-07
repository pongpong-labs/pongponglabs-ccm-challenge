package kr.pongponglabs.pongpong;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableConfigurationProperties(StorageProperties.class)
public class PongpongApplication {
	public static void main(String[] args) {
		SpringApplication.run(PongpongApplication.class, args);
	}
}
