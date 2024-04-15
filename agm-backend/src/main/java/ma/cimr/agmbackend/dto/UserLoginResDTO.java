package ma.cimr.agmbackend.dto;

import lombok.Data;

@Data
public class UserLoginResDTO {
	private String accessToken;
	private String refreshToken;
}
