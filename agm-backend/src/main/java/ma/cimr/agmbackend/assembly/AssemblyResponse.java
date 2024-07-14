package ma.cimr.agmbackend.assembly;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;
import java.time.LocalTime;

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
public class AssemblyResponse {

	private Long id;
	private AssemblyType type;
	private int year;
	private LocalDate day;
	private LocalTime time;
	private String address;
	private String city;
	private boolean closed;
	private String routeSheet;
	private String invitationLetter;
	private String attendanceSheet;
	private String proxy;
	private String attendanceForm;
}
