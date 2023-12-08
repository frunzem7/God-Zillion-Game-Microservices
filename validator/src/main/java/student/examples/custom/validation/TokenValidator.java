package student.examples.custom.validation;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TokenValidator implements ConstraintValidator<ValidToken, String> {

	@Override
	public boolean isValid(String token, ConstraintValidatorContext cxt) {
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(token);
			String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
			UUID.fromString(decodedString);

			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
