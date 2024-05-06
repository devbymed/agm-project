package ma.cimr.agmbackend.user;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cimr.agmbackend.auth.LoginProfileResponse;
import ma.cimr.agmbackend.feature.FeatureResponse;
import ma.cimr.agmbackend.profile.ProfileResponse;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = NON_NULL)
public class UserResponse {
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private ProfileResponse profile;
}