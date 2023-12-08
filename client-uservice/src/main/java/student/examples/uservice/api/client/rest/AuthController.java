package student.examples.uservice.api.client.rest;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import student.examples.uservice.api.client.dto.RestResponse;
import student.examples.uservice.api.client.dto.RestSuccessResponse;
import student.examples.uservice.api.client.dto.UserSigninRequest;
import student.examples.uservice.api.client.dto.UserSignoutRequest;
import student.examples.uservice.api.client.dto.UserSignupRequest;
import student.examples.uservice.api.client.grpc.ClientgRPC;

@RestController
@RequestMapping("/auth")
public class AuthController {

	public ClientgRPC clientgRPC = new ClientgRPC();

	@PostMapping("/signup")
	public RestResponse signup(@Valid @RequestBody UserSignupRequest userSignupRequest) {
		clientgRPC.createUserAndSend(userSignupRequest);

		return new RestSuccessResponse(200, new HashMap<String, String>() {
			{
				put("message", String.format("an email has been sent to, please verify and activate your account",
						userSignupRequest.getEmail()));
			}
		});
	}

	@PostMapping("/signin")
	public RestResponse signin(@Valid @RequestBody UserSigninRequest userSigninRequest) {
		String randomUUID = UUID.randomUUID().toString();
		String base64UUID = Base64.getEncoder().encodeToString(randomUUID.getBytes());
		System.out.println(base64UUID);

		return new RestSuccessResponse(200, "signin success");
	}

	@PostMapping("/signout")
	public RestResponse signout(@Valid @RequestBody UserSignoutRequest userDto) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("message", String.format("User successfully signed out"));
		RestResponse response = new RestSuccessResponse(200, map);

		return response;
	}
}
