package student.examples.uservice.api.client.rest;

import java.util.HashMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import student.examples.uservice.api.client.dto.RestResponse;
import student.examples.uservice.api.client.dto.RestSuccessResponse;
import student.examples.uservice.api.client.dto.UserSigninRequest;
import student.examples.uservice.api.client.dto.UserSignupRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@PostMapping("/signup")
	public RestResponse signup(@Valid @RequestBody UserSignupRequest userSignupRequest) {
		return new RestSuccessResponse(200, new HashMap<String, String>() {
			{
				put("message", String.format("an email has been sent to, please verify and activate your account",
						userSignupRequest.getEmail()));
			}
		});
	}

	@PostMapping("/signin")
	public RestResponse signin(@Valid @RequestBody UserSigninRequest userSigninRequest) {
		return new RestSuccessResponse(200, "signin success");
	}
}
