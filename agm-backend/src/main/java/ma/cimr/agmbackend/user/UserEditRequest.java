package ma.cimr.agmbackend.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_EMPTY)
public class UserEditRequest {

	@Size(max = 50, message = "Le prénom ne peut pas dépasser 50 caractères")
	private String firstName;

	@Size(max = 50, message = "Le nom ne peut pas dépasser 50 caractères")
	private String lastName;

	@Size(max = 50, message = "L'email ne peut pas dépasser 50 caractères")
	private String email;

	@Positive(message = "L'identifiant du profil ne peut pas être négatif")
	private Long profileId;
}