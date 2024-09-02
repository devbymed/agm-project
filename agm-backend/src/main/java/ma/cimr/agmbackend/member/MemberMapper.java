package ma.cimr.agmbackend.member;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ma.cimr.agmbackend.user.User;

@Mapper(componentModel = "spring")
public interface MemberMapper {

	// Map une entité Member vers un DTO MemberResponse
	@Mapping(source = "agent", target = "agentFullName", qualifiedByName = "concatNames")
	MemberResponse toResponse(Member member);

	// Map une liste d'entités Member vers une liste de DTOs MemberResponse
	List<MemberResponse> toResponses(List<Member> members);

	@Named("concatNames")
	default String concatNames(User agent) {
		return agent != null ? agent.getFirstName() + " " + agent.getLastName() : null;
	}
}