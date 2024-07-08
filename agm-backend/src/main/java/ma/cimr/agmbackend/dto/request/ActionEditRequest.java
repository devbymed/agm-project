package ma.cimr.agmbackend.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class ActionEditRequest {

	private String description;
	private String responsible;
	private LocalDate startDate;
	private LocalDate endDate;
	private String progressStatus;
	private LocalDate realizationDate;
	private String observation;
	private String deliverable;
}
