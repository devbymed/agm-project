package ma.cimr.agmbackend.profile;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = NON_NULL)
public record ProfileResponse(Long id, String name) {
}
