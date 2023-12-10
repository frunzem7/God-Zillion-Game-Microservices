package student.examples.uservice.api.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.Reader;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
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
import java.io.BufferedWriter;
import java.io.FileWriter;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import student.examples.uservice.api.client.dto.UserSignupRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.jgit.api.AddCommand;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

@Slf4j
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
class ValidationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

	@TempDir
	static File tempDir;

	private static String VALID_DATA_CSV_FILE = "C:\\Users\\User\\AppData\\Local\\Temp\\data\\validData.csv";
	private static String INVALID_DATA_CSV_FILE = "C:\\Users\\User\\AppData\\Local\\Temp\\data\\invalidData.csv";

	private static final String REPOSITORY_URL = "https://github.com/frunzem7/DataTestForGame.git";
	private static final String BRANCH_NAME = "main";

	@BeforeEach
	public void setUp() throws IOException {
		cloneRepository();
	}

	@AfterAll
	public static void tearDown() {
		pushToRepository();
	}

	private void cloneRepository() throws IOException {
		try {
			File destinationDirectory = new File("C:\\Users\\User\\AppData\\Local\\Temp\\data");
			log.info("Destination Directory: " + destinationDirectory.getAbsolutePath());

			if (destinationDirectory.exists() && destinationDirectory.list().length == 0) {
				Git.cloneRepository().setURI(REPOSITORY_URL).setDirectory(destinationDirectory).setBranch(BRANCH_NAME)
						.call();
			} else {
				log.info("Destination directory is not empty. Skipping cloning.");
			}
		} catch (GitAPIException e) {
			e.printStackTrace();
			log.info("Error cloning repository: " + e.getMessage());
		}
	}

	private static void pushToRepository() {
		try (Git git = Git.open(new File("C:\\Users\\User\\AppData\\Local\\Temp\\data\\.git"))) {
			git.add().addFilepattern(".").call();
			git.add().addFilepattern(".").call();
			git.commit().setMessage("Test results").call();
			git.push().setCredentialsProvider(
					new UsernamePasswordCredentialsProvider("frunzem7", "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"))
					.call();
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
			log.info("Error pushing to repository: " + e.getMessage());
		}
	}

	@BeforeAll
	public static void init() throws IOException {

		Faker faker = new Faker();

		Files.createDirectories(Paths.get(tempDir.getAbsolutePath(), "data"));

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(VALID_DATA_CSV_FILE)));
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

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(INVALID_DATA_CSV_FILE)));
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

		try (Git git = Git.open(new File("C:\\Users\\User\\AppData\\Local\\Temp\\data"))) {
			AddCommand add = git.add();
			add.addFilepattern(".").call();
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
			log.info("Error adding files to repository: " + e.getMessage());
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
