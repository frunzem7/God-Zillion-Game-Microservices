package student.examples.uservice.api.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.Reader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import student.examples.uservice.api.client.dto.UserSignupRequest;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class ValidationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

	private static final String VALID_DATA_CSV_FILE = System.getProperty("user.dir") + "/validData.csv";
	private static final String INVALID_DATA_CSV_FILE = System.getProperty("user.dir") + "/invalidData.csv";

	@BeforeAll
	public static void init() throws IOException {
		Faker faker = new Faker();

		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(VALID_DATA_CSV_FILE));
				CSVPrinter csvPrinter = new CSVPrinter(writer,
						CSVFormat.DEFAULT.withHeader("UserName", "Email", "Password", "PasswordConfirmation"));) {

			for (int i = 1; i <= 10; i++) {
				String username = faker.regexify("^[a-zA-Z0-9]{8,}$");
				String email = faker.internet().emailAddress();
				String generatedPassword = faker.regexify("[a-zA-Z0-9]{8,}$");

				csvPrinter.printRecord(username, email, generatedPassword, generatedPassword);
			}
			csvPrinter.flush();
		}

		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(INVALID_DATA_CSV_FILE));
				CSVPrinter csvPrinter = new CSVPrinter(writer,
						CSVFormat.DEFAULT.withHeader("UserName", "Email", "Password", "PasswordConfirmation"));) {

			for (int i = 1; i <= 10; i++) {
				String username = faker.lorem().characters(1, 2);
				String email = faker.lorem().word();
				String generatedPassword = faker.lorem().characters(1, 7);

				csvPrinter.printRecord(username, email, generatedPassword, generatedPassword);
			}
			csvPrinter.flush();
		}

	}

	@Test
	void validateTrueDataTest() throws Exception {

		Validator validator = factory.getValidator();

		Set<jakarta.validation.ConstraintViolation<UserSignupRequest>> validation = null;

		try (Reader reader = Files.newBufferedReader(Paths.get(VALID_DATA_CSV_FILE));
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
			for (CSVRecord csvRecord : csvParser) {
				String username = csvRecord.get("UserName");
				String email = csvRecord.get("Email");
				String password = csvRecord.get("Password");
				String passwordConfirmation = csvRecord.get("PasswordConfirmation");

				validation = validator.validate(new UserSignupRequest(username, email, password, passwordConfirmation));
			}
		}
		assertTrue(validation.isEmpty());
	}

	@Test
	public void signupWithValidDataTest() throws IllegalArgumentException, IllegalAccessException, Exception {
		try (Reader reader = Files.newBufferedReader(Paths.get(VALID_DATA_CSV_FILE));
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
			for (CSVRecord csvRecord : csvParser) {
				String username = csvRecord.get("UserName");
				String email = csvRecord.get("Email");
				String password = csvRecord.get("Password");
				String passwordConfirmation = csvRecord.get("PasswordConfirmation");

				UserSignupRequest userSignupRequest = new UserSignupRequest(username, email, password,
						passwordConfirmation);

				ObjectMapper objectMapper = new ObjectMapper();
				String jsonRequest = objectMapper.writeValueAsString(userSignupRequest);

				ResultActions result = mockMvc
						.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(jsonRequest));

				result.andExpect(status().isOk());
			}
		}
	}

	@Test
	void validateFalseDataTest() throws Exception {

		Validator validator = factory.getValidator();

		Set<jakarta.validation.ConstraintViolation<UserSignupRequest>> validation = null;

		try (Reader reader = Files.newBufferedReader(Paths.get(INVALID_DATA_CSV_FILE));
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
			for (CSVRecord csvRecord : csvParser) {
				String username = csvRecord.get("UserName");
				String email = csvRecord.get("Email");
				String password = csvRecord.get("Password");
				String passwordConfirmation = csvRecord.get("PasswordConfirmation");

				validation = validator.validate(new UserSignupRequest(username, email, password, passwordConfirmation));
			}
		}
		assertFalse(validation.isEmpty());
	}

	@Test
	public void signupWithInvalidDataTest() throws IllegalArgumentException, IllegalAccessException, Exception {
		try (Reader reader = Files.newBufferedReader(Paths.get(INVALID_DATA_CSV_FILE));
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
			for (CSVRecord csvRecord : csvParser) {
				String username = csvRecord.get("UserName");
				String email = csvRecord.get("Email");
				String password = csvRecord.get("Password");
				String passwordConfirmation = csvRecord.get("PasswordConfirmation");

				UserSignupRequest userSignupRequest = new UserSignupRequest(username, email, password,
						passwordConfirmation);

				ObjectMapper objectMapper = new ObjectMapper();
				String jsonRequest = objectMapper.writeValueAsString(userSignupRequest);

				ResultActions result = mockMvc
						.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(jsonRequest));

				result.andExpect(status().is4xxClientError());
			}
		}
	}
}
