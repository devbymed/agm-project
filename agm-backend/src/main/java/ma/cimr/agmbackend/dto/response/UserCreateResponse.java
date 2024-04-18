package ma.cimr.agmbackend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserCreateResponse {
	private String firstName;
	private String lastName;
	private String createdAt;
	private String message;
}
