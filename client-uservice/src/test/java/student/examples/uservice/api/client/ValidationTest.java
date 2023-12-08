package student.examples.uservice.api.client;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import student.examples.uservice.api.client.dto.UserSignupRequest;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ValidationTest {
//	@Autowired
//	private MockMvc mockMvc;

	private Faker faker = new Faker();
	private UserSignupRequest userSignupRequest = new UserSignupRequest();

	@Autowired
	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private static final String SAMPLE_CSV_FILE = System.getProperty("user.dir") + "/validData.csv";

	@Test
	public void validateTrueDataTest() throws IllegalArgumentException, IllegalAccessException, Exception {

		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE));
				CSVPrinter csvPrinter = new CSVPrinter(writer,
						CSVFormat.DEFAULT.withHeader("Username", "Email", "Password", "PasswordConfirmation"))) {

			userSignupRequest.setUsername(faker.regexify("^[a-zA-Z0-9]{8,}$"));
			userSignupRequest.setEmail(faker.internet().emailAddress());

			String generatedPassword = faker.regexify("[a-zA-Z0-9]{8,}$");

			userSignupRequest.setPassword(generatedPassword);
			userSignupRequest.setPasswordConfirmation(generatedPassword);

			System.out.println("Generated Password: " + generatedPassword);
			System.out.println("Generated Confirmation: " + userSignupRequest.getPasswordConfirmation());

			// Print CSV record inside the loop
			csvPrinter.printRecord(Arrays.asList(userSignupRequest.getUsername(), userSignupRequest.getEmail(),
					userSignupRequest.getPassword(), userSignupRequest.getPasswordConfirmation()));

			// Flush and close the CSV printer
			csvPrinter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonRequest = objectMapper.writeValueAsString(userSignupRequest);

//		ResultActions result = mockMvc
//				.perform(post("/auth/signup").contentType("application/json").content(jsonRequest));

//		result.andExpect(status().isOk());

		Validator validator = factory.getValidator();

		Set<ConstraintViolation<UserSignupRequest>> validation = validator.validate(userSignupRequest);

		System.out.println(validation);

		assertTrue(validation.isEmpty());
	}

//	@Test
//	public void validateTrueDataTest3() throws IllegalArgumentException, IllegalAccessException {
//
//		for (int i = 0; i < 10; i++) {
//
//			userSignupRequest.setUsername(faker.regexify("^[a-zA-Z0-9]{8,}$"));
//			userSignupRequest.setEmail(faker.internet().emailAddress());
//			userSignupRequest.setPassword(
//					faker.regexify("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%*?&])[A-Za-z\\d@#$!%*?&]{8,}$"));
//			userSignupRequest.setPasswordConfirmation(userSignupRequest.getPassword());
//
//			Validator validator = factory.getValidator();
//
//			Set<ConstraintViolation<UserSignupRequest>> validation = validator.validate(userSignupRequest);
//
//			System.out.println(validation);
//
//			assertTrue(validation.isEmpty());
//		}
//	}

//	@Test
//	public void validateFalseDataTest() throws IllegalArgumentException, IllegalAccessException {
//
//		for (int i = 0; i < 10; i++) {
//
//			userSignupRequest.setUsername(faker.lorem().characters(1, 2));
//			userSignupRequest.setEmail(faker.lorem().word());
//			userSignupRequest.setPassword(faker.lorem().characters(1, 7));
//			userSignupRequest.setPasswordConfirmation(faker.lorem().characters(1, 7));
//
//			Validator validator = factory.getValidator();
//
//			Set<ConstraintViolation<UserSignupRequest>> validation = validator.validate(userSignupRequest);
//
//			System.out.println(validation);
//
//			assertFalse(validation.isEmpty());
//		}
//	}
}
