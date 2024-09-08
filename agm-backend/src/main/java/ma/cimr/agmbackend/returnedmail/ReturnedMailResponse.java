package ma.cimr.agmbackend.returnedmail;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cimr.agmbackend.reason.Reason;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = NON_NULL)
public class ReturnedMailResponse {
    private String memberNumber;
    private LocalDate returnDate;
    private String reason;
}
