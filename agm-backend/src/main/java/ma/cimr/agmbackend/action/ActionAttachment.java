package ma.cimr.agmbackend.action;

import static jakarta.persistence.FetchType.EAGER;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
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
@Table(name = "action_attachments")
public class ActionAttachment extends BaseEntity {

	@ManyToOne(fetch = EAGER)
	@JoinColumn(name = "action_id", nullable = false)
	private Action action;

	@Column(nullable = false)
	private String fileName;

	@Column(nullable = false)
	private String originalFileName;

	@Column(nullable = false)
	private String fileType;

	@Column(nullable = false)
	private String filePath;

	@Column(nullable = false)
	private Long fileSize;
}
