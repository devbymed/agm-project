package ma.cimr.agmbackend.profile;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ma.cimr.agmbackend.common.BaseEntity;
import ma.cimr.agmbackend.permission.Permission;
import ma.cimr.agmbackend.user.User;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profiles")
public class Profile extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String name;

	@Builder.Default
	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private Set<User> users = new HashSet<>();

	@Builder.Default
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "profile_permissions", joinColumns = @JoinColumn(name = "profile_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Set<Permission> permissions = new HashSet<>();
}
