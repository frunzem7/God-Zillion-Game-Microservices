package student.examples.uservice.api.client;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.javafaker.Faker;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import student.examples.uservice.api.client.dto.UserSignupRequest;

@SpringBootTest
class ValidationTest {

	private Faker faker = new Faker();
	private UserSignupRequest userSignupRequest = new UserSignupRequest();

	@Autowired
	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

	@Test
	public void validateTrueDataTest() throws IllegalArgumentException, IllegalAccessException {

		for (int i = 0; i < 10; i++) {

			userSignupRequest.setUsername(faker.regexify("^[a-zA-Z0-9]{8,}$"));
			userSignupRequest.setEmail(faker.internet().emailAddress());
			userSignupRequest.setPassword(
					faker.regexify("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%*?&])[A-Za-z\\d@#$!%*?&]{8,}$"));
			userSignupRequest.setPasswordConfirmation(userSignupRequest.getPassword());

			Validator validator = factory.getValidator();

			Set<ConstraintViolation<UserSignupRequest>> validation = validator.validate(userSignupRequest);

			System.out.println(validation);

			assertTrue(validation.isEmpty());
		}
	}

	@Test
	public void validateFalseDataTest() throws IllegalArgumentException, IllegalAccessException {

		for (int i = 0; i < 10; i++) {

			userSignupRequest.setUsername(faker.lorem().characters(1, 2));
			userSignupRequest.setEmail(faker.lorem().word());
			userSignupRequest.setPassword(faker.lorem().characters(1, 7));
			userSignupRequest.setPasswordConfirmation(faker.lorem().characters(1, 7));

			Validator validator = factory.getValidator();

			Set<ConstraintViolation<UserSignupRequest>> validation = validator.validate(userSignupRequest);

			System.out.println(validation);

			assertFalse(validation.isEmpty());
		}
	}
}
