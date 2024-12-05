package example.WebShopTrening;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ErrorResponse(
		HttpStatus status, 
	    String message,	    
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd hh:mm:ss")
	    LocalDateTime timestamp
	) {
	public ErrorResponse(HttpStatus status, String message) {
		this(status, message,LocalDateTime.now());
	}
}
