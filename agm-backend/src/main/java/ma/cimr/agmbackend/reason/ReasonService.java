package ma.cimr.agmbackend.reason;

import java.util.List;

public interface ReasonService {

	List<ReasonResponse> getReasons();

	ReasonResponse getReason(Long id);

	ReasonResponse createReason(ReasonAddRequest reasonAddRequest);

	ReasonResponse updateReason(Long id, ReasonEditRequest reasonEditRequest);

	void deleteReason(Long id);
}
