package ma.cimr.agmbackend.returnedmail;

import java.time.LocalDate;

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
public class ReturnedMailRequest {
    @NotNull(message = "N° Adhérent est obligatoire")
    private String memberNumber;

    @NotNull(message = "Date de retour est obligatoire")
    private LocalDate returnDate;

    @NotNull(message = "Le motif est obligatoire")
    private Long reasonId;
}
