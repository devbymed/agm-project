package ma.cimr.agmbackend.member;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("SELECT m FROM Member m WHERE m.type = '1' " +
			"AND (m.dtrYear > :year OR (m.dtrYear = :year AND m.dtrTrimester >= :trimester)) " +
			"AND m.workforce > :minAffiliates")
	List<Member> findEligibleType1Members(@Param("trimester") int trimester, @Param("year") int year,
			@Param("minAffiliates") int minAffiliates);

	@Query("SELECT m FROM Member m WHERE m.type IN ('2', '3', '4') " +
			"AND (m.dtrYear > :year OR (m.dtrYear = :year AND m.dtrTrimester >= :trimester)) " +
			"AND m.workforce > :minAffiliates")
	List<Member> findEligibleType2to4Members(@Param("trimester") int trimester, @Param("year") int year,
			@Param("minAffiliates") int minAffiliates);
}
