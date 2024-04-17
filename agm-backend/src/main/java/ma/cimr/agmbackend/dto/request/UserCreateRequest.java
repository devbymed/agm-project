package ma.cimr.agmbackend.dto.request;

import lombok.Data;

@Data
public class UserCreateRequest {
	private String firstName;
	private String lastName;
	private String email;
}
