package example.WebShopTrening.configurations;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import example.WebShopTrening.UserService.Role;
import example.WebShopTrening.UserService.UserEntity;
import example.WebShopTrening.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class CustomUserDetailService implements UserDetailsService{

	private UserRepository userRepository;
	
	public CustomUserDetailService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	   @Override
	   @Transactional
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        UserEntity user = userRepository.findByUserName(username)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	        
	        return new User(
	            user.getUserName(),
	            user.getPassword(),
	            mapRolesToAuthorities(user.getRoles())
	        );
	    }
	
    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
    }
}
