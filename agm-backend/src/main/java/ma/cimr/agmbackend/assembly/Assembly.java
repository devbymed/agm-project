package ma.cimr.agmbackend.assembly;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ma.cimr.agmbackend.action.Action;
import ma.cimr.agmbackend.common.BaseEntity;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assemblies")
public class Assembly extends BaseEntity {

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AssemblyType type;

	@Column(nullable = false)
	private int year;

	@Column(nullable = false)
	private LocalDate day;

	@Column(nullable = false)
	private LocalTime time;

	@Column(nullable = false, length = 255)
	private String address;

	@Column(nullable = false, length = 100)
	private String city;

	@Builder.Default
	@Column(nullable = false)
	private boolean closed = false;

	@Column(nullable = true)
	private String routeSheet;

	@Column(nullable = true)
	private String invitationLetter;

	@Column(nullable = true)
	private String attendanceSheet;

	@Column(nullable = true)
	private String proxy;

	@Column(nullable = true)
	private String attendanceForm;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "assembly")
	private List<Action> actions;
}
