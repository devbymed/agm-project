package ma.cimr.agmbackend.user;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import ma.cimr.agmbackend.auth.LoginUserResponse;
import ma.cimr.agmbackend.feature.Feature;
import ma.cimr.agmbackend.profile.Profile;
import ma.cimr.agmbackend.profile.ProfileMapper;

@Mapper(componentModel = "spring", uses = ProfileMapper.class, unmappedTargetPolicy = IGNORE)
public interface UserMapper {

	@Mapping(source = "profile", target = "profile", qualifiedByName = "toProfileResponse")
	UserResponse toUserResponse(User user);

	@Mapping(source = "profile", target = "profile", qualifiedByName = "toProfileResponseWithoutFeatures")
	UserResponse toUserResponseWithoutFeatures(User user);

	default List<UserResponse> toUserResponseListWithoutFeatures(List<User> users) {
		return users.stream()
				.map(this::toUserResponseWithoutFeatures)
				.collect(Collectors.toList());
	}

	@Named("toUserResponseWithoutEnabled")
	default UserResponse toUserResponseWithoutEnabled(User user) {
		UserResponse response = toUserResponseWithoutFeatures(user);
		ProfileMapper profileMapper = Mappers.getMapper(ProfileMapper.class);
		response.setProfile(profileMapper.toProfileResponseWithoutEnabled(user.getProfile()));
		return response;
	}

	User toUser(UserAddRequest userAddRequest);

	@Named("toLoginUserResponse")
	default LoginUserResponse toLoginUserResponse(User user) {
		LoginUserResponse response = new LoginUserResponse();
		response.setId(user.getId());
		response.setFirstName(user.getFirstName());
		response.setLastName(user.getLastName());
		response.setEmail(user.getEmail());
		ProfileMapper profileMapper = Mappers.getMapper(ProfileMapper.class);
		response.setProfile(profileMapper.toLoginProfileResponse(user.getProfile())); // Use toLoginProfileResponse here
		return response;
	}
}