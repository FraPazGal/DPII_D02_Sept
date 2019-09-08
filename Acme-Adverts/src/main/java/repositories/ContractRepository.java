
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {

	@Query("select c from Contract c where c.request.id = ?1")
	Collection<Contract> findAllContract(int id);

	@Query("select c from Contract c where c.request.id = ?1 and (c.request.customer.id = ?2 or c.request.pack.manager.id = ?2)")
	Contract findOneByRequestAndOwner(int id, int id2);

	@Query("select c from Contract c where c.request.customer.id = ?1 and c.signedManager IS NOT NULL ")
	Collection<Contract> findAllCustomer(int id);

	@Query("select c from Contract c where c.request.pack.manager.id = ?1")
	Collection<Contract> findAllManager(int id);

	@Query("select c from Contract c where c.request.pack.id = ?1")
	Collection<Contract> findAllByPackage(int id);
	
	@Query("select c from Contract c where c.signedManager is null and c.request.pack.manager.id = ?1")
	List<Contract> getContractInDraftMode(Integer contractId);
}
