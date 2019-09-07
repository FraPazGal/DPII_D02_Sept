
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.TVFile;

@Repository
public interface TVFileRepository extends JpaRepository<TVFile, Integer> {

	@Query("select t from TVFile t where t.contract.id = ?1")
	Collection<TVFile> findAllTVFileByContract(int id);
	
	@Query("select max(1.0*(select count(*) from TVFile t where t.contract = c)),min(1.0*(select count(*) from TVFile t where t.contract = c)),avg(1.0*(select count(*) from TVFile t where t.contract = c)),stddev(1.0*(select count(*) from TVFile t where t.contract = c)) from Contract c")
	Double[] statsTVFilesPerContract();

}
