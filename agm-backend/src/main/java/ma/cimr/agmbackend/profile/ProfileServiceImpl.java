package ma.cimr.agmbackend.profile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;
import ma.cimr.agmbackend.permission.Permission;
import ma.cimr.agmbackend.permission.PermissionRepository;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(ProfileServiceImpl.class);

	private final ProfileMapper profileMapper;
	private final ProfileRepository profileRepository;
	private final PermissionRepository permissionRepository;

	@Override
	public List<ProfileResponse> getProfiles() {
		return profileRepository.findAll().stream()
				.map(profileMapper::toProfileResponse)
				.collect(Collectors.toList());
	}

	@Override
	public ProfileResponse getProfile(Long id) {
		Profile profile = profileRepository.findById(id)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.PROFILE_NOT_FOUND));
		return profileMapper.toProfileResponse(profile);
	}

	@Override
	public ProfileResponse createProfile(ProfileAddRequest profileAddRequest) {
		if (profileRepository.existsByName(profileAddRequest.getName())) {
			throw new ApiException(ApiExceptionCodes.PROFILE_ALREADY_EXISTS);
		}
		Profile profile = profileMapper.toProfile(profileAddRequest);
		Set<Permission> permissions = getPermissionsFromIds(profileAddRequest.getPermissionIds());
		if (permissions == null) {
			permissions = new HashSet<>();
		}
		profile.setPermissions(permissions);
		profile = profileRepository.save(profile);
		return profileMapper.toProfileResponse(profile);
	}

	@Override
	public ProfileResponse updateProfile(Long id, ProfileEditRequest profileEditRequest) {
		Profile profile = profileRepository.findById(id)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.PROFILE_NOT_FOUND));

		// Mettre à jour le nom du profil si fourni
		if (profileEditRequest.getName() != null) {
			profile.setName(profileEditRequest.getName());
		}

		// Mettre à jour les permissions du profil si fournies
		if (profileEditRequest.getPermissionIds() != null) {
			Set<Permission> permissions = getPermissionsFromIds(profileEditRequest.getPermissionIds());
			profile.setPermissions(permissions);
		}

		// Sauvegarder le profil mis à jour
		profile = profileRepository.save(profile);

		return profileMapper.toProfileResponse(profile);
	}

	@Override
	public void updatePermissions(Long profileId, List<Long> permissionIds) {
		Profile profile = profileRepository.findById(profileId)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.PROFILE_NOT_FOUND));
		List<Permission> permissions = permissionRepository.findAllById(permissionIds);
		Set<Permission> permissionsSet = new HashSet<>(permissions);
		profile.setPermissions(permissionsSet);
		profileRepository.save(profile);
	}

	private Set<Permission> getPermissionsFromIds(List<Long> permissionIds) {
		if (permissionIds == null || permissionIds.isEmpty()) {
			return new HashSet<>();
		}
		return permissionIds.stream()
				.map(permissionRepository::findById)
				.map(optional -> optional.orElseThrow(() -> new ApiException(ApiExceptionCodes.PERMISSION_NOT_FOUND)))
				.collect(Collectors.toSet());
	}
}
