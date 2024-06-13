package ma.cimr.agmbackend.permission;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ma.cimr.agmbackend.common.BaseEntity;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permissions")
public class Permission extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private String label;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Permission parent;

	@Builder.Default
	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
	private Set<Permission> children = new HashSet<>();
}
