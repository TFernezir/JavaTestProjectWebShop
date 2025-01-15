package example.WebShopTrening.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.WebShopTrening.Dto.LogInDto;
import example.WebShopTrening.Dto.RegisterDto;
import example.WebShopTrening.UserService.IUserService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private final IUserService userService;
		
	public AuthController(IUserService userService) {
		super();
		this.userService = userService;
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LogInDto logInDto, HttpSession session) {
				
		return ResponseEntity.ok(userService.login(logInDto, session));
	}
	
   @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        
        return userService.register(registerDto);
    }
}
