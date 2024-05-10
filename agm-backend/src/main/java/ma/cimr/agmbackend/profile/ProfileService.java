package ma.cimr.agmbackend.profile;

import org.springframework.data.domain.Page;

public interface ProfileService {

	Page<ProfileResponse> getProfiles(int page, int size);

	// ProfileResponse getProfile(Long id);

	ProfileResponse createProfile(ProfileAddRequest request);

	ProfileResponse addPermissionToProfile(Long profileId, Long permissionId);

	void removePermissionFromProfile(Long profileId, Long permissionId);
}
