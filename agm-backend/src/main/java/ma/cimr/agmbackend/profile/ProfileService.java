package ma.cimr.agmbackend.profile;

import java.util.List;

public interface ProfileService {

	List<ProfileResponse> getProfiles();

	ProfileResponse getProfile(Long id);

	ProfileResponse createProfile(ProfileAddRequest request);
}
