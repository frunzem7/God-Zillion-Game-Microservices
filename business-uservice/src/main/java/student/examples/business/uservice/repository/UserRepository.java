package student.examples.business.uservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import student.examples.business.uservice.domain.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

}
