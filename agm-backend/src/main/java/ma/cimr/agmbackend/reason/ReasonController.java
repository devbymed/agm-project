package ma.cimr.agmbackend.reason;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.util.ApiResponse;
import ma.cimr.agmbackend.util.ApiResponseFormatter;

@RestController
@RequestMapping("reasons")
@RequiredArgsConstructor
// @PreAuthorize("hasAuthority('REASON_MANAGEMENT')")
// @Tag(name = "Gestion des raisons")
public class ReasonController {

	private final ReasonService reasonService;

	@GetMapping
	public ResponseEntity<ApiResponse> getReasons() {
		List<ReasonResponse> reasons = reasonService.getReasons();
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, reasons);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getReason(@PathVariable Long id) {
		ReasonResponse reason = reasonService.getReason(id);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, reason);
	}

	@PostMapping
	public ResponseEntity<ApiResponse> createReason(@Valid @RequestBody ReasonAddRequest reasonAddRequest) {
		ReasonResponse createdReason = reasonService.createReason(reasonAddRequest);
		return ApiResponseFormatter.generateResponse(HttpStatus.CREATED, "Motif créé avec succès", createdReason);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse> updateReason(@PathVariable Long id,
			@Valid @RequestBody ReasonEditRequest reasonEditRequest) {
		ReasonResponse updatedReason = reasonService.updateReason(id, reasonEditRequest);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Motif mis à jour avec succès", updatedReason);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteReason(@PathVariable Long id) {
		reasonService.deleteReason(id);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Motif supprimé avec succès");
	}
}
