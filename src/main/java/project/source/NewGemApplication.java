package project.source;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NewGemApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewGemApplication.class, args);
	}
}
