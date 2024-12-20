package example.WebShopTrening.UserService;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured("ADMIN")
public class UserService implements IUserService{

}
