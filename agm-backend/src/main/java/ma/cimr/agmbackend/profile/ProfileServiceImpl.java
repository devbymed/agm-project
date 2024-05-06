package ma.cimr.agmbackend.profile;

import static ma.cimr.agmbackend.exception.ApiExceptionCodes.FEATURE_NOT_FOUND;
import static ma.cimr.agmbackend.exception.ApiExceptionCodes.PROFILE_NOT_FOUND;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.feature.Feature;
import ma.cimr.agmbackend.feature.FeatureRepository;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;
	private final FeatureRepository featureRepository;
	private final ProfileMapper profileMapper;

	@Override
	public List<ProfileResponse> getProfiles() {
		List<Feature> allFeatures = featureRepository.findAll();
		return profileRepository.findAll().stream()
				.map(profile -> profileMapper.toProfileResponseWithAllFeatures(profile, allFeatures))
				.collect(Collectors.toList());
	}

	@Override
	public ProfileResponse getProfile(Long id) {
		List<Feature> allFeatures = featureRepository.findAll();
		return profileRepository.findById(id)
				.map(profile -> profileMapper.toProfileResponseWithAllFeatures(profile, allFeatures))
				.orElseThrow(() -> new ApiException(PROFILE_NOT_FOUND));
	}

	@Override
	public ProfileResponse createProfile(ProfileAddRequest profileAddRequest) {
		Profile profile = profileMapper.toProfile(profileAddRequest);
		profile = profileRepository.save(profile);
		return profileMapper.toProfileResponseWithoutFeatures(profile);
	}

	@Override
	public void updateProfileFeature(Long profileId, String featureName, boolean isEnabled) {
		Profile profile = profileRepository.findById(profileId)
				.orElseThrow(() -> new ApiException(PROFILE_NOT_FOUND));
		Feature feature = featureRepository.findByName(featureName)
				.orElseThrow(() -> new ApiException(FEATURE_NOT_FOUND));
		if (isEnabled) {
			profile.getFeatures().add(feature);
		} else {
			profile.getFeatures().remove(feature);
		}
		profileRepository.save(profile);
	}

	// @Override
	// public Set<Feature> getProfileFeatures(Long id) {
	// Profile profile = profileRepository.findById(id).orElseThrow(() -> new
	// ApiException(PROFILE_NOT_FOUND));
	// return profile.getFeatures();
	// }

	// @Override
	// public void updateProfileFeatures(Long id, Set<Feature> features) {
	// Profile profile = profileRepository.findById(id).orElseThrow(() -> new
	// ApiException(PROFILE_NOT_FOUND));
	// profile.setFeatures(features);
	// profileRepository.save(profile);
	// }
}
