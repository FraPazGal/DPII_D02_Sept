
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.InfoFile;

@Repository
public interface InfoFileRepository extends JpaRepository<InfoFile, Integer> {

	@Query("select i from InfoFile i where i.contract.id = ?1")
	Collection<InfoFile> findAllInfoFileByContract(int id);
	
	@Query("select max(1.0*(select count(*) from InfoFile i where i.contract = c)),min(1.0*(select count(*) from InfoFile i where i.contract = c)),avg(1.0*(select count(*) from InfoFile i where i.contract = c)),stddev(1.0*(select count(*) from InfoFile i where i.contract = c)) from Contract c")
	Double[] statsInfoFilesPerContract();

}
