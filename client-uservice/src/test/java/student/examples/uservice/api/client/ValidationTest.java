//package student.examples.uservice.api.client;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.Set;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import student.examples.uservice.api.client.dto.UserSignupRequest;
//
//@SpringBootTest
//class ValidationTest {
//
//	@Test
//	public void testValidationForInputTrueData() throws IllegalArgumentException, IllegalAccessException {
//		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//
//		UserSignupRequest userSignupRequest = new UserSignupRequest();
//		java.lang.reflect.Field[] userField = userSignupRequest.getClass().getDeclaredFields();
//
//		for (java.lang.reflect.Field field : userField) {
//			if (field.getName().equals("username")) {
//				field.setAccessible(true);
//				field.set(userSignupRequest, "marianaF567");
//			}
//			if (field.getName().equals("email")) {
//				field.setAccessible(true);
//				field.set(userSignupRequest, "frunzem7@gmail.com");
//			}
//			if (field.getName().equals("password")) {
//				field.setAccessible(true);
//				field.set(userSignupRequest, "mariana56G#");
//			}
//			if (field.getName().equals("passwordConfirmation")) {
//				field.setAccessible(true);
//				field.set(userSignupRequest, "mariana56G#");
//			}
//		}
//		Validator validator = factory.getValidator();
//
//		Set<ConstraintViolation<UserSignupRequest>> validation = validator.validate(userSignupRequest);
//
//		System.out.println(validation);
//
//		assertTrue(validation.isEmpty());
//
//	}
//}
