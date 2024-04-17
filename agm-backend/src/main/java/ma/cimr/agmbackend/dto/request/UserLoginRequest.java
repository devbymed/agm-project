package ma.cimr.agmbackend.dto.request;

import lombok.Data;

@Data
public class UserLoginRequest {

	private String email;
	private String password;
}
