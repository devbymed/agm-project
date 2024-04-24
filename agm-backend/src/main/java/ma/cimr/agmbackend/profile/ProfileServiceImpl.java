package ma.cimr.agmbackend.profile;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

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
				.orElseThrow(ProfileNotFoundException::new);
	}

	@Override
	public ProfileResponse createProfile(ProfileAddRequest request) {
		Profile profile = profileMapper.toProfile(request);
		profile = profileRepository.save(profile);
		return profileMapper.toProfileResponse(profile);
	}
}
