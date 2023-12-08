package student.examples.uservice.api.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import student.examples.custom.validation.ValidToken;

@Getter
@Setter
@NoArgsConstructor
public class UserSignoutRequest {
	@ValidToken(message = "Invalid token format")
	private String token;
	
}
