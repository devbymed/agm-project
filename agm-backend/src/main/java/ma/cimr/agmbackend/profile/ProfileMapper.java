package ma.cimr.agmbackend.profile;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ma.cimr.agmbackend.permission.Permission;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ProfileMapper {
	ProfileResponse toProfileResponse(Profile profile);

	Profile toProfile(ProfileAddRequest profileAddRequest);

	default List<String> mapPermissions(List<Permission> permissions) {
		if (permissions == null) {
			return null;
		}

		return permissions.stream()
				.map(Permission::getName)
				.collect(Collectors.toList());
	}

	@Mapping(target = "permissions", ignore = true)
	@Named("toProfileResponseWithoutPermissions")
	ProfileResponse toProfileResponseWithoutPermissions(Profile profile);
}
