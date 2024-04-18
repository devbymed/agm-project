package ma.cimr.agmbackend.dto.response;

import lombok.Data;

@Data
public class UserLoginResponse {
	private String accessToken;
	private String refreshToken;
}
