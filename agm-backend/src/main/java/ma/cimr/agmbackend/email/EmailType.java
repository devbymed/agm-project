package ma.cimr.agmbackend.email;

import lombok.Getter;

@Getter
public enum EmailType {
	NEW_USER("new_user");

	private final String name;

	private EmailType(String name) {
		this.name = name;
	}
}
