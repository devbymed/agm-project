package ma.cimr.agmbackend.member;

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
public class MemberResponse {
	private Long id;
	private String memberNumber;
	private String type;
	private String companyName;
	private String address1;
	private String address2;
	private String city;
	private String phone1;
	private String phone2;
	private LocalDate membershipDate;
	private int workforce;
	private String title;
	private int dbrTrimester;
	private int dbrYear;
	private int dtrTrimester;
	private int dtrYear;
	// private Long agentId;
	private String agentFullName;
	private MemberInvitationStatus status;
	private LocalDate assignmentDate;
	private LocalDate editionDate;
	private String invitationLetterPath;
	private String attendanceSheetPath;
	private String proxyPath;
}
