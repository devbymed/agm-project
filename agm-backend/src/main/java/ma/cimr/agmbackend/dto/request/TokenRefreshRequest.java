package ma.cimr.agmbackend.dto.request;

import lombok.Data;

@Data
public class TokenRefreshRequest {

	private String refreshToken;
}
