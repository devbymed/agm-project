package ma.cimr.agmbackend.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ma.cimr.agmbackend.common.BaseEntity;
import ma.cimr.agmbackend.enums.AssemblyType;

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

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "route_sheet_id", referencedColumnName = "id")
	private Document routeSheet;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "invitation_letter_id", referencedColumnName = "id")
	private Document invitationLetter;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "attendance_sheet_id", referencedColumnName = "id")
	private Document attendanceSheet;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "proxy_id", referencedColumnName = "id")
	private Document proxy;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "attendance_form_id", referencedColumnName = "id")
	private Document attendanceForm;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "assembly")
	private List<Action> actions;
}
