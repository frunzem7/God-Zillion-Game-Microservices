package student.examples.uservice.api.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * THIS IS DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class UserSigninRequest {
	private String usernameOrEmail;
	private String password;
}
