
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<domain.Package, Integer> {

	@Query("select p from Package p where p.finalMode = true")
	Collection<domain.Package> findAllFinal();

	@Query("select p from Package p where p.finalMode = true or p.manager.id = ?1")
	Collection<domain.Package> findAllManager(int id);

	@Query("select p from Package p where p.ticker =?1")
	domain.Package getPackageByTicker(String tick);

	@Query("select p.title from Package p where p.manager.id = ?1")
	List<String> findAllManagerTitle(int id);
}
