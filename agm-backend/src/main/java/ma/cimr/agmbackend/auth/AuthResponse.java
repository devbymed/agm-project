package ma.cimr.agmbackend.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
	private String accessToken;
	private String refreshToken;
}
