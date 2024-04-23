package ma.cimr.agmbackend.profile;

import static org.mapstruct.ReportingPolicy.IGNORE;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ProfileMapper {

	ProfileResponse toProfileResponse(Profile profile);

	Profile toProfile(ProfileAddRequest profileAddRequest);
}
