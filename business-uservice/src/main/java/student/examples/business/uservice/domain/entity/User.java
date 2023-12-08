package student.examples.business.uservice.domain.entity;

import java.util.UUID;

import javax.validation.constraints.Size;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	private UUID id;
	@Size(min = 8, max = 20, message = "Username must be between 8 and 20 characters")
	private String username;
	private String email;
	private String password;
}
