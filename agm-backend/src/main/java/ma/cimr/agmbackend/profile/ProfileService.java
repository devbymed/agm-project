package ma.cimr.agmbackend.profile;

import java.util.List;
import java.util.Set;

public interface ProfileService {

	List<ProfileResponse> getProfiles();

	ProfileResponse getProfile(Long id);

	ProfileResponse createProfile(ProfileAddRequest request);

	Set<Feature> getProfileFeatures(Long id);

	void updateProfileFeatures(Long id, Set<Feature> features);
}
