package ma.cimr.agmbackend.returnedmail;

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
import ma.cimr.agmbackend.member.Member;
import ma.cimr.agmbackend.reason.Reason;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "returned_mails")
public class ReturnedMail extends BaseEntity {
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "reason_id")
    private Reason reason;

    @Column(name = "return_date")
    private LocalDate returnDate;
}
