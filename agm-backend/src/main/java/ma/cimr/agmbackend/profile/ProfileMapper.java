package ma.cimr.agmbackend.profile;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ma.cimr.agmbackend.auth.LoginProfileResponse;
import ma.cimr.agmbackend.feature.Feature;
import ma.cimr.agmbackend.feature.FeatureResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ProfileMapper {
	@Named("toProfileResponse")
	@Mapping(target = "features", expression = "java(mapFeatures(profile.getFeatures()))")
	ProfileResponse toProfileResponse(Profile profile);

	@Named("toProfileResponseWithoutFeatures")
	@Mapping(target = "features", ignore = true)
	ProfileResponse toProfileResponseWithoutFeatures(Profile profile);

	@Named("toProfileResponseWithoutEnabled")
	default ProfileResponse toProfileResponseWithoutEnabled(Profile profile) {
		ProfileResponse response = new ProfileResponse();
		response.setId(profile.getId());
		response.setName(profile.getName());
		response.setFeatures(profile.getFeatures().stream()
				.map(feature -> {
					FeatureResponse featureResponse = new FeatureResponse();
					featureResponse.setName(feature.getName());
					// Ignore the enabled attribute
					featureResponse.setEnabled(null);
					return featureResponse;
				})
				.collect(Collectors.toList()));
		return response;
	}

	Profile toProfile(ProfileAddRequest profileAddRequest);

	default List<FeatureResponse> mapFeatures(Set<Feature> features) {
		return features.stream()
				.map(feature -> {
					FeatureResponse featureResponse = new FeatureResponse();
					featureResponse.setName(feature.getName());
					return featureResponse;
				})
				.collect(Collectors.toList());
	}

	default List<FeatureResponse> mapFeaturesWithEnabled(Set<Feature> features, Profile profile) {
		return features.stream()
				.map(feature -> {
					FeatureResponse featureResponse = new FeatureResponse();
					featureResponse.setName(feature.getName());
					featureResponse.setEnabled(profile.getFeatures().contains(feature));
					return featureResponse;
				})
				.collect(Collectors.toList());
	}

	@Named("toProfileResponseWithAllFeatures")
	default ProfileResponse toProfileResponseWithAllFeatures(Profile profile, List<Feature> allFeatures) {
		ProfileResponse response = toProfileResponseWithoutFeatures(profile);
		List<FeatureResponse> featureResponses = mapFeaturesWithEnabled(new HashSet<>(allFeatures), profile);
		response.setFeatures(featureResponses);
		return response;
	}

	@Named("toLoginProfileResponse")
	default LoginProfileResponse toLoginProfileResponse(Profile profile) {
		List<String> features = profile.getFeatures().stream()
				.map(Feature::getName)
				.collect(Collectors.toList());
		return LoginProfileResponse.builder()
				.id(profile.getId())
				.name(profile.getName())
				.features(features)
				.build();
	}
}
