package ma.cimr.agmbackend.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserEditRequest {

	private String firstName;
	private String lastName;
	private Long profileId;
}
