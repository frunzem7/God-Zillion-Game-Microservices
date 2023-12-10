package student.examples.business.uservice.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import student.examples.business.uservice.domain.entity.User;
import student.examples.business.uservice.services.UserMangementService;

@RestController
@RequestMapping("/user")
public class UserManagementController {

	@Autowired
	private UserMangementService activationService;

	@GetMapping("/activation")
	public ResponseEntity<User> activationByToken(@RequestParam(name = "token", required = false) String token) {
		return new ResponseEntity<>(activationService.activationByToken(token), HttpStatus.OK);
	}

	@GetMapping("/withdraw")
	public ResponseEntity<Boolean> deleteByToken(@RequestParam(name = "token", required = false) String token) {
		return new ResponseEntity<>(activationService.deleteByToken(token), HttpStatus.OK);
	}
}