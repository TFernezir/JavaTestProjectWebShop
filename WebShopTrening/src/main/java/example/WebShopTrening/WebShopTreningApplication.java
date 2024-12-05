package example.WebShopTrening;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebShopTreningApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebShopTreningApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ProductRepository repository ) {
		return args -> {
			repository.save(new Product (null, "Spring boot", 15.5, "Book"));
		};
	}
}
