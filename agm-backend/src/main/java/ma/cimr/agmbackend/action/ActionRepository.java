package ma.cimr.agmbackend.action;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

	@Query("SELECT a FROM Action a WHERE a.endDate < :currentDate AND a.progressStatus < 100")
	List<Action> findOverdueUnclosedActions(@Param("currentDate") LocalDate currentDate);
}
