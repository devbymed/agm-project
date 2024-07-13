package ma.cimr.agmbackend.action;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

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
@JsonInclude(value = NON_NULL)
public class ActionResponse {

	private Long id;
	private String description;
	private String responsible;
	private LocalDate startDate;
	private LocalDate endDate;
	private String progressStatus;
	private LocalDate realizationDate;
	private String observation;
	private String deliverable;
	// private List<DocumentResponse> attachments;
}
