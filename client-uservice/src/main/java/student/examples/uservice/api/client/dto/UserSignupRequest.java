package student.examples.uservice.api.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * THIS IS DTO
 */

@Getter
@Setter
@NoArgsConstructor
public class UserSignupRequest {
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$", message = "should be minimum 8 characters, latin alphabet and digits")
	private String username;
	@Email(message = "should be a valid address")
	private String email;
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%*?&])[A-Za-z\\d@#$!%*?&]{8,}$", message = "must be at least 8 characters, contain at least one upper, lower, digit and special characters")
	private String password;
	@NotEmpty(message = "")
	private String passwordConfirmation;
}
