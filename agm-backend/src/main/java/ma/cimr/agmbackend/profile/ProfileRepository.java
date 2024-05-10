package ma.cimr.agmbackend.profile;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends PagingAndSortingRepository<Profile, Long>, CrudRepository<Profile, Long> {
	Optional<Profile> findByName(String name);
}
