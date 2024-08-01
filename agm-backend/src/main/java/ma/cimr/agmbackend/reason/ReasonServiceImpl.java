package ma.cimr.agmbackend.reason;

import static ma.cimr.agmbackend.exception.ApiExceptionCodes.REASON_NOT_FOUND;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.exception.ApiException;

@Service
@RequiredArgsConstructor
public class ReasonServiceImpl implements ReasonService {

	private final ReasonRepository reasonRepository;
	private final ReasonMapper reasonMapper;

	@Override
	public List<ReasonResponse> getReasons() {
		return reasonRepository.findAll().stream()
				.map(reasonMapper::toResponse)
				.toList();
	}

	@Override
	public ReasonResponse getReason(Long id) {
		return reasonRepository.findById(id)
				.map(reasonMapper::toResponse)
				.orElseThrow(() -> new ApiException(REASON_NOT_FOUND));
	}

	@Override
	public ReasonResponse createReason(ReasonAddRequest reasonAddRequest) {
		Reason reason = reasonMapper.toEntity(reasonAddRequest);
		Reason savedReason = reasonRepository.save(reason);
		return reasonMapper.toResponse(savedReason);
	}

	@Override
	public ReasonResponse updateReason(Long id, ReasonEditRequest reasonEditRequest) {
		Reason reason = reasonRepository.findById(id)
				.orElseThrow(() -> new ApiException(REASON_NOT_FOUND));
		reasonMapper.updateEntityFromDto(reasonEditRequest, reason);
		Reason updatedReason = reasonRepository.save(reason);
		return reasonMapper.toResponse(updatedReason);
	}

	@Override
	public void deleteReason(Long id) {
		reasonRepository.deleteById(id);
	}
}
