package example.WebShopTrening.UserService;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import example.WebShopTrening.Dto.AuthResponseDto;
import example.WebShopTrening.Dto.LogInDto;
import example.WebShopTrening.Dto.RegisterDto;
import example.WebShopTrening.ProductService.ProductService;
import example.WebShopTrening.configurations.JwtGenerator;
import example.WebShopTrening.repositories.RoleRepository;
import example.WebShopTrening.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;

@Service
public class UserService implements IUserService{
	
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtGenerator jwtGenerator;
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	

	public UserService(
			AuthenticationManager authenticationManager, 
			UserRepository userRepository,
			RoleRepository roleRepository, 
			PasswordEncoder passwordEncoder, 
			JwtGenerator jwtGenerator
			) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtGenerator = jwtGenerator;
	}

	@Override
	public String login(LogInDto logInDto, HttpSession session) {
		logger.info("Executing login " + getClass() + " for user " + logInDto.getUserName());
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						logInDto.getUserName(), logInDto.getPassword())
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtGenerator.generateToken(authentication);
		
		return "Bearer " + new AuthResponseDto(token).getAccesToken();
	}

	@Override
	public  ResponseEntity<String> register(RegisterDto registerDto) {
		logger.info("Executing register " + getClass() + " user " + registerDto.getUserName());
		
		 if (userRepository.existsByUserName(registerDto.getUserName())) {
			 logger.error("Usernem exists in db");
	            return ResponseEntity
	                .badRequest()
	                .body("Username already exists");
	        }

	        UserEntity user = new UserEntity();
	        user.setUserName(registerDto.getUserName());
	        user.setEmail(registerDto.getEmail());
	        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

	        Role defaultRole = roleRepository.findByName("ROLE_USER")
	                            .orElseGet(() -> {
	                                Role newRole = new Role();
	                                newRole.setName("ROLE_USER");
	                                return roleRepository.save(newRole);
	                            });
	        
	        user.setRoles(Collections.singletonList(defaultRole));
	        userRepository.save(user);
	        logger.info("Executing " + getClass() + " for user " + registerDto.getUserName() + " registered sucessiful");
	        return ResponseEntity.ok("User registered successfully");
	}

}
