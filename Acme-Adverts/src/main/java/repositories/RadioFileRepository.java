
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.RadioFile;

@Repository
public interface RadioFileRepository extends JpaRepository<RadioFile, Integer> {

	@Query("select r from RadioFile r where r.contract.id = ?1")
	Collection<RadioFile> findAllRadioFileByContract(int id);
	
	@Query("select max(1.0*(select count(*) from RadioFile r where r.contract = c)),min(1.0*(select count(*) from RadioFile r where r.contract = c)),avg(1.0*(select count(*) from RadioFile r where r.contract = c)),stddev(1.0*(select count(*) from RadioFile r where r.contract = c)) from Contract c")
	Double[] statsRadioFilesPerContract();

}
