package ma.cimr.agmbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import ma.cimr.agmbackend.dto.request.UserCreateRequest;
import ma.cimr.agmbackend.dto.response.UserCreateResponse;
import ma.cimr.agmbackend.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

	@Mapping(target = "role", constant = "USER")
	User toUser(UserCreateRequest userCreateRequest);

	UserCreateResponse toUserCreateResponse(User user);
}
