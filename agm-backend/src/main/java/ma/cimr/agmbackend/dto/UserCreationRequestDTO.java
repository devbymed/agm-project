package ma.cimr.agmbackend.dto;

import lombok.Data;

@Data
public class UserCreationRequestDTO {
	private String firstName;
	private String lastName;
	private String email;
	private String password;
}
