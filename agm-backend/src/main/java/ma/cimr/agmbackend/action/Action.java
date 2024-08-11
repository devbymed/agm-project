package ma.cimr.agmbackend.action;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@ManyToOne(fetch = EAGER)
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

	@Builder.Default
	@Column(name = "progress_status", nullable = false)
	private Integer progressStatus = 0;

	@Column
	private String observation;

	@OneToMany(mappedBy = "action", cascade = ALL, fetch = EAGER)
	private List<ActionAttachment> attachments;
}