
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.SocialNetworkFile;

@Repository
public interface SocialNetworkFileRepository extends JpaRepository<SocialNetworkFile, Integer> {

	@Query("select s from SocialNetworkFile s where s.contract.id = ?1")
	Collection<SocialNetworkFile> findAllSNFileByContract(int id);
	
	@Query("select max(1.0*(select count(*) from SocialNetworkFile s where s.contract = c)),min(1.0*(select count(*) from SocialNetworkFile s where s.contract = c)),avg(1.0*(select count(*) from SocialNetworkFile s where s.contract = c)),stddev(1.0*(select count(*) from SocialNetworkFile s where s.contract = c)) from Contract c")
	Double[] statsSNFilesPerContract();

}
