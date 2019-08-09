
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	@Query("select distinct r.pack.manager from Request r where r.status='PENDING' group by (r.pack.manager)")
	//	@Query("select m from Manager m join c.fixUpTasks f join f.complaints co order by co.size desc")
	//  @Query("select r.pack.manager from Request r join r.pack.manager m where r.status='PENDING' order by r.size")
	List<Manager> top10();

}
