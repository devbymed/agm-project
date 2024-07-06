package ma.cimr.agmbackend.service;

import java.util.List;

import ma.cimr.agmbackend.dto.request.ProfileAddRequest;
import ma.cimr.agmbackend.dto.request.ProfileEditRequest;
import ma.cimr.agmbackend.dto.response.ProfileResponse;

public interface ProfileService {

	List<ProfileResponse> getProfiles();

	ProfileResponse getProfile(Long id);

	ProfileResponse createProfile(ProfileAddRequest request);

	ProfileResponse updateProfile(Long id, ProfileEditRequest profileEditRequest);

	void updatePermissions(Long profileId, List<Long> permissionIds);
}
