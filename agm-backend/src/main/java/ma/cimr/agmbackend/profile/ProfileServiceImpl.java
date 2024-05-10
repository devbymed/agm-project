package ma.cimr.agmbackend.profile;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;
import ma.cimr.agmbackend.permission.Permission;
import ma.cimr.agmbackend.permission.PermissionRepository;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileServiceImpl.class);

	private final ProfileRepository profileRepository;
	private final ProfileMapper profileMapper;
	private final PermissionRepository permissionRepository;

	@Override
	public Page<ProfileResponse> getProfiles(int page, int size) {
		LOGGER.info("* Getting profiles page %d with size %d", page, size);
		return profileRepository.findAll(PageRequest.of(page, size)).map(profileMapper::toProfileResponse);
	}

	@Override
	public ProfileResponse createProfile(ProfileAddRequest profileAddRequest) {
		Profile profile = profileMapper.toProfile(profileAddRequest);
		profile = profileRepository.save(profile);
		return profileMapper.toProfileResponse(profile);
	}

	public ProfileResponse addPermissionToProfile(Long profileId, Long permissionId) {
		Profile profile = profileRepository.findById(profileId)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.PROFILE_NOT_FOUND));
		Permission permission = permissionRepository.findById(permissionId)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.PERMISSION_NOT_FOUND));

		profile.getPermissions().add(permission);
		Profile savedProfile = profileRepository.save(profile);
		return profileMapper.toProfileResponse(savedProfile);
	}

	public void removePermissionFromProfile(Long profileId, Long permissionId) {
		Profile profile = profileRepository.findById(profileId)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.PROFILE_NOT_FOUND));
		Permission permission = permissionRepository.findById(permissionId)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.PERMISSION_NOT_FOUND));

		List<Permission> updatedPermissions = profile.getPermissions().stream()
				.filter(p -> !p.getId().equals(permission.getId()))
				.collect(Collectors.toList());

		profile.setPermissions(updatedPermissions);
		profileRepository.save(profile);
	}
}
