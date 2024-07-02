package ma.cimr.agmbackend.permission;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = NON_NULL)
public class PermissionResponse {

	private Long id;
	private String name;
	private String label;
	private String path;
	private Long parentId;

	@Builder.Default
	private List<PermissionResponse> children = new ArrayList<>();
}
