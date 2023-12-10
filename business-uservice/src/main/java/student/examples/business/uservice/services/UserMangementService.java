package student.examples.business.uservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import student.examples.business.uservice.domain.entity.User;
import student.examples.business.uservice.repository.UserRepository;

@Service
public class UserMangementService {

	@Autowired
	private UserRepository userRepository;

	public User activationByToken(String token) {
		User userByToken = userRepository.getUserByToken(token).stream().findFirst().get();

		if (userByToken != null) {
			userByToken.setIsActive(true);
			return userRepository.save(userByToken);
		}
		return null;
	}

	public boolean deleteByToken(String token) {
		User userByToken = userRepository.getUserByToken(token).stream().findFirst().get();

		if (userByToken != null) {
			userRepository.delete(userByToken);
			return true;
		}
		return false;
	}

}
