package ma.cimr.agmbackend.member;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {

	// Map une entité Member vers un DTO MemberResponse
	MemberResponse toResponse(Member member);

	// Map une liste d'entités Member vers une liste de DTOs MemberResponse
	List<MemberResponse> toResponses(List<Member> members);
}