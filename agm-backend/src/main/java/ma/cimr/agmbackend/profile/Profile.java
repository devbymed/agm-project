package ma.cimr.agmbackend.profile;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ma.cimr.agmbackend.common.BaseEntity;
import ma.cimr.agmbackend.user.User;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profiles")
// @EntityListeners(AuditingEntityListener.class)
public class Profile extends BaseEntity {
	// @Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	// private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<User> users;

	// @CreatedDate
	// @Column(name = "created_at", nullable = false, updatable = false)
	// private LocalDateTime createdAt;

	// @LastModifiedDate
	// @Column(name = "updated_at", insertable = false)
	// private LocalDateTime updatedAt;
}
