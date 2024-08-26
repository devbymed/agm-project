package ma.cimr.agmbackend.member;

import static jakarta.persistence.FetchType.EAGER;

import java.time.LocalDate;

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
import ma.cimr.agmbackend.user.User;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "members")
public class Member extends BaseEntity {

	@Column(name = "member_number", nullable = false, unique = true)
	private String memberNumber;

	@Column(nullable = false)
	private String type;

	@Column(name = "company_name")
	private String companyName;

	@Column(name = "address_1")
	private String address1;

	@Column(name = "address_2")
	private String address2;

	@Column(nullable = false)
	private String city;

	@Column(name = "phone_1")
	private String phone1;

	@Column(name = "phone_2")
	private String phone2;

	@Column(name = "membership_date", nullable = false)
	private LocalDate membershipDate;

	@Column(nullable = false)
	private int workforce;

	@Column(nullable = false)
	private String title;

	@Column(name = "dbr_trimester")
	private int dbrTrimester;

	@Column(name = "dbr_year")
	private int dbrYear;

	@Column(name = "dtr_trimester")
	private int dtrTrimester;

	@Column(name = "dtr_year")
	private int dtrYear;

	@ManyToOne(fetch = EAGER)
	@JoinColumn(name = "agent_id")
	private User agent;
}
