package ma.cimr.agmbackend.profile;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@JsonInclude(value = NON_EMPTY)
public class ProfileAddRequest {

	@NotBlank(message = "Le nom du profil est obligatoire")
	private String name;

	@NotNull(message = "Les permissions du profil sont obligatoires")
	private List<Long> permissionIds;
}
