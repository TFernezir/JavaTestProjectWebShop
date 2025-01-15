package example.WebShopTrening.UserService;

import org.springframework.http.ResponseEntity;

import example.WebShopTrening.Dto.LogInDto;
import example.WebShopTrening.Dto.RegisterDto;
import jakarta.servlet.http.HttpSession;

public interface IUserService {
	
	String login (LogInDto logInDto, HttpSession session);
	
	ResponseEntity<String>  register (RegisterDto logInDto);

}
