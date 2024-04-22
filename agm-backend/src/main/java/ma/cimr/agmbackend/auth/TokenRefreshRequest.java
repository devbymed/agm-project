package ma.cimr.agmbackend.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshRequest {
	private String refreshToken;
}
