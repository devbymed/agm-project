package ma.cimr.agmbackend.profile;

import static ma.cimr.agmbackend.exception.ApiExceptionCodes.PROFILE_NOT_FOUND;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.exception.ApiException;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;
	private final ProfileMapper profileMapper;

	@Override
	public List<ProfileResponse> getProfiles() {
		return profileRepository.findAll().stream().map(profileMapper::toProfileResponse).collect(Collectors.toList());
	}

	@Override
	public ProfileResponse getProfile(Long id) {
		return profileRepository.findById(id).map(profileMapper::toProfileResponse)
				.orElseThrow(() -> new ApiException(PROFILE_NOT_FOUND));
	}

	@Override
	public ProfileResponse createProfile(ProfileAddRequest profileAddRequest) {
		Profile profile = profileMapper.toProfile(profileAddRequest);
		profile = profileRepository.save(profile);
		return profileMapper.toProfileResponse(profile);
	}

	@Override
	public Set<Feature> getProfileFeatures(Long id) {
		Profile profile = profileRepository.findById(id).orElseThrow(() -> new ApiException(PROFILE_NOT_FOUND));
		return profile.getFeatures();
	}

	@Override
	public void updateProfileFeatures(Long id, Set<Feature> features) {
		Profile profile = profileRepository.findById(id).orElseThrow(() -> new ApiException(PROFILE_NOT_FOUND));
		profile.setFeatures(features);
		profileRepository.save(profile);
	}
}
