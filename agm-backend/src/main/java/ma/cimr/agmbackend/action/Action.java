package ma.cimr.agmbackend.action;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ma.cimr.agmbackend.assembly.Assembly;
import ma.cimr.agmbackend.common.BaseEntity;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "actions")
public class Action extends BaseEntity {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "assembly_id", nullable = false)
	private Assembly assembly;

	@Column(nullable = false)
	private String name;

	@Column(nullable = true)
	private String entity;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "realization_date")
	private LocalDate realizationDate;

	@Column(nullable = true)
	private String responsible;

	@Column
	private String deliverable;

	@Column(name = "progress_status")
	private String progressStatus;

	@Column
	private String observation;

	// @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch =
	// FetchType.EAGER)
	// @JoinColumn(name = "action_id")
	// private List<String> attachments;
}