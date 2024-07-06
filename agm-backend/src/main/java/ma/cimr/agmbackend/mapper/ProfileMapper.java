package ma.cimr.agmbackend.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import ma.cimr.agmbackend.dto.request.ProfileAddRequest;
import ma.cimr.agmbackend.dto.response.ProfileResponse;
import ma.cimr.agmbackend.model.Profile;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ProfileMapper {
	ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

	@Named("toProfileResponse")
	@Mapping(source = "permissions", target = "permissions")
	ProfileResponse toProfileResponse(Profile profile);

	Profile toProfile(ProfileAddRequest profileAddRequest);

	@Named("toProfileResponseWithoutPermissions")
	@Mapping(target = "permissions", ignore = true)
	ProfileResponse toProfileResponseWithoutPermissions(Profile profile);
}
