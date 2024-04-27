package ma.cimr.agmbackend.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ma.cimr.agmbackend.profile.ProfileResponse;

@JsonInclude(value = Include.NON_NULL)
public record UserResponse(Long id, String firstName, String lastName, String email, ProfileResponse profile) {
}