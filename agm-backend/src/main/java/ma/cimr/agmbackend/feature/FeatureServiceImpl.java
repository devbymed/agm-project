package ma.cimr.agmbackend.feature;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {

	private final FeatureRepository featureRepository;

	@Override
	public List<Feature> getAllFeatures() {
		return featureRepository.findAll();
	}
}
