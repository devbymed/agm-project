package ma.cimr.agmbackend.user;

import static org.mapstruct.ReportingPolicy.IGNORE;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ma.cimr.agmbackend.profile.ProfileMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE, uses = ProfileMapper.class)
public interface UserMapper {

	@Mapping(source = "profile", target = "profile", qualifiedByName = "toProfileResponse")
	UserResponse toUserResponse(User user);

	User toUser(UserAddRequest userAddRequest);

	@Mapping(source = "profile", target = "profile", qualifiedByName = "toProfileResponseWithoutPermissions")
	UserResponse toUserResponseWithoutPermissions(User user);
}