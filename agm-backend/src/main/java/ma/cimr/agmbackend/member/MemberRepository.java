package ma.cimr.agmbackend.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByMemberNumber(String memberNumber);

	List<Member> findAllByMemberNumberIn(List<String> memberNumbers);

	List<Member> findAllByAgentIsNull();
}
