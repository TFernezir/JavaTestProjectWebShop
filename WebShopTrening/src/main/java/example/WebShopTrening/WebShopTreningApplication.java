package example.WebShopTrening;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WebShopTreningApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebShopTreningApplication.class, args);
	}
}
