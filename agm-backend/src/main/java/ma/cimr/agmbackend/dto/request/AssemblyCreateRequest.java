package ma.cimr.agmbackend.dto.request;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cimr.agmbackend.enums.AssemblyType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = NON_NULL)
public class AssemblyCreateRequest {

	@NotNull(message = "Le type est obligatoire")
	private AssemblyType type;

	@NotNull(message = "L'année est obligatoire")
	// @Min(value = 2023, message = "L'année doit être 2023 ou plus")
	// @Max(value = 2024, message = "L'année doit être 2024 ou moins")
	private Integer year;

	@NotNull(message = "Le jour est obligatoire")
	// @PastOrPresent(message = "Le jour ne peut pas être dans le futur")
	private LocalDate day;

	@NotNull(message = "L'heure est obligatoire")
	private LocalTime time;

	@NotBlank(message = "L'adresse est obligatoire")
	@Length(max = 255, message = "L'adresse ne peut pas dépasser 255 caractères")
	private String address;

	@NotBlank(message = "La ville est obligatoire")
	@Length(max = 100, message = "La ville ne peut pas dépasser 100 caractères")
	private String city;
}
