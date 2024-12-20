package example.WebShopTrening.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LogInDto {
	  	
	    private String userName;

	    @NotBlank(message = "Password is required")
	    @Pattern(
	        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
	        message = "Password must be at least 8 characters long and contain at least one digit, " +
	                 "one lowercase letter, one uppercase letter, and one special character"
	    )
	    private String password;

	    public String getUserName() {
	        return userName;
	    }

	    public void setUserName(String userName) {
	        this.userName = userName;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }
}
