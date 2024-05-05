package ma.cimr.agmbackend.profile;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ProfileMapper {

	@Mapping(target = "features", expression = "java(mapFeatures(profile.getFeatures()))")
	ProfileResponse toProfileResponse(Profile profile);

	Profile toProfile(ProfileAddRequest profileAddRequest);

	default Set<String> mapFeatures(Set<Feature> features) {
		return features.stream()
				.map(Feature::name)
				.collect(Collectors.toSet());
	}

	@Named("toProfileResponseWithoutFeatures")
	@Mapping(target = "features", ignore = true)
	ProfileResponse toProfileResponseWithoutFeatures(Profile profile);
}
