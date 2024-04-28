package ma.cimr.agmbackend.user;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;

import ma.cimr.agmbackend.profile.ProfileResponse;

@JsonInclude(value = NON_NULL)
public record UserResponse(Long id, String firstName, String lastName, String email, ProfileResponse profile) {
}