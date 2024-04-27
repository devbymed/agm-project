package ma.cimr.agmbackend.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@JsonInclude(value = Include.NON_EMPTY)
public record UserEditRequest(
		@Size(max = 50, message = "Le prénom ne peut pas dépasser 50 caractères") String firstName,

		@Size(max = 50, message = "Le nom ne peut pas dépasser 50 caractères") String lastName,

		@Positive(message = "L'identifiant du profil ne peut pas être négatif") Long profileId) {
}