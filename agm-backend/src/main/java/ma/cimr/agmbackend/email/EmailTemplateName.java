package ma.cimr.agmbackend.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
	NEW_USER("new_user");

	private final String name;

	EmailTemplateName(String name) {
		this.name = name;
	}
}
