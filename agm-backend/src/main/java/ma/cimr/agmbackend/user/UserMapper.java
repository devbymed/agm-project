package ma.cimr.agmbackend.user;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ma.cimr.agmbackend.profile.ProfileMapper;

@Mapper(componentModel = "spring", uses = ProfileMapper.class, unmappedTargetPolicy = IGNORE)
public interface UserMapper {

	@Mapping(source = "profile", target = "profile")
	UserResponse toUserResponse(User user);

	@Mapping(source = "profile", target = "profile", qualifiedByName = "toProfileResponseWithoutFeatures")
	UserResponse toUserResponseWithoutFeatures(User user);

	default List<UserResponse> toUserResponseListWithoutFeatures(List<User> users) {
		return users.stream()
				.map(this::toUserResponseWithoutFeatures)
				.collect(Collectors.toList());
	}

	User toUser(UserAddRequest userAddRequest);
}