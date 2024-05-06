package ma.cimr.agmbackend.profile;

import java.util.List;

public interface ProfileService {

	List<ProfileResponse> getProfiles();

	ProfileResponse getProfile(Long id);

	ProfileResponse createProfile(ProfileAddRequest request);

	void updateProfileFeature(Long profileId, String featureName, boolean isEnabled);

	// Set<Feature> getProfileFeatures(Long id);

	// void updateProfileFeatures(Long id, Set<Feature> features);
}
