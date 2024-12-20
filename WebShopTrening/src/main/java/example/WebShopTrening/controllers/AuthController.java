package example.WebShopTrening.controllers;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.WebShopTrening.Dto.LogInDto;
import example.WebShopTrening.Dto.RegisterDto;
import example.WebShopTrening.entitets.Role;
import example.WebShopTrening.entitets.UserEntity;
import example.WebShopTrening.repositories.RoleRepository;
import example.WebShopTrening.repositories.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	
	public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
			RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		super();
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LogInDto logInDto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						logInDto.getUserName(), logInDto.getPassword())
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return ResponseEntity.ok("Loged in successfully");
	}
	
   @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if (userRepository.existsByUserName(registerDto.getUserName())) {
            return ResponseEntity
                .badRequest()
                .body("Username already exists");
        }

        UserEntity user = new UserEntity();
        user.setUserName(registerDto.getUserName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role defaultRole = roleRepository.findByName("USER")
                            .orElseGet(() -> {
                                Role newRole = new Role();
                                newRole.setName("USER");
                                return roleRepository.save(newRole);
                            });
        
        user.setRoles(Collections.singletonList(defaultRole));
        userRepository.save(user);
        
        return ResponseEntity.ok("User registered successfully");
    }
}
