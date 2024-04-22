package ma.cimr.agmbackend.user;

import static org.mapstruct.ReportingPolicy.IGNORE;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface UserMapper {

	UserResponse toUserResponse(User user);

	User toUser(UserAddRequest userAddRequest);
}