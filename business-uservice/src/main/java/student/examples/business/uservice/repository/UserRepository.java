package student.examples.business.uservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import student.examples.business.uservice.domain.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

	User getDistinctByEmail(String email);

	Optional<User> getUserByToken(String token);

	void deleteByToken(String token);
}
