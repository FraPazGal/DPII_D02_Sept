
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.BillboardFile;

@Repository
public interface BillboardFileRepository extends JpaRepository<BillboardFile, Integer> {

	@Query("select b from BillboardFile b where b.contract.id = ?1")
	Collection<BillboardFile> findAllBillFileByContract(int id);
	
	@Query("select max(1.0*(select count(*) from BillboardFile b where b.contract = c)),min(1.0*(select count(*) from BillboardFile b where b.contract = c)),avg(1.0*(select count(*) from BillboardFile b where b.contract = c)),stddev(1.0*(select count(*) from BillboardFile b where b.contract = c)) from Contract c")
	Double[] statsBillboardFilesPerContract();
	
	@Query("select max(1.0*(select count(*) from File f where f.contract = c)),min(1.0*(select count(*) from File f where f.contract = c)),avg(1.0*(select count(*) from File f where f.contract = c)),stddev(1.0*(select count(*) from File f where f.contract = c)) from Contract c")
	Double[] statsFilesPerContract();

}
