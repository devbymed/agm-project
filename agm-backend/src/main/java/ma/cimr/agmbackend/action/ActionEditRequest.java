package ma.cimr.agmbackend.action;

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

	private String name;
	private String entity;
	private LocalDate startDate;
	private LocalDate endDate;
	private LocalDate realizationDate;
	private String responsible;
	private String deliverable;
	private String progressStatus;
	private String observation;
}
