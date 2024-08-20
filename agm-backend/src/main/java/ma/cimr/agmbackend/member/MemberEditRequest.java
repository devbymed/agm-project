package ma.cimr.agmbackend.member;

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
public class MemberEditRequest {
	private String address1;
	private String address2;
	private String city;
	private String phone1;
	private String phone2;
}
