package ma.cimr.agmbackend.dto;

import lombok.Data;

@Data
public class UserLoginReqDTO {
	private String email;
	private String password;
}
