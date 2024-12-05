package example.WebShopTrening;

import org.springframework.data.annotation.Id;

import CustomValidations.NotEmptyOrWhitespace;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record Product (
		@Id 
		Long id,
		@NotNull(message = "Price is required")
		@NotEmptyOrWhitespace
		String name,
		@NotNull(message = "Price is required")
		@Positive(message = "Price must be greater than 0")
		Double price, 
		String about
		)  {

}