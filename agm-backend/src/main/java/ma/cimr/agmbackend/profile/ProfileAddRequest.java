package ma.cimr.agmbackend.profile;

import jakarta.validation.constraints.NotBlank;

public record ProfileAddRequest(@NotBlank(message = "Le nom du profil est obligatoire") String name) {
}
