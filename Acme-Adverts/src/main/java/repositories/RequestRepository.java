
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

	@Query("select r from Request r where r.pack.manager.id = ?1")
	Collection<Request> findAllFinal(int id);

	@Query("select r from Request r where r.customer.id = ?1")
	Collection<Request> findAllCustomer(int id);

	@Query("select r from Request r where r.pack.id = ?1")
	Collection<Request> findByPackage(int id);
	@Query("select r from Request r where r.pack.id = ?2 and r.pack.manager.id = ?1")
	Collection<Request> findByPackageAndOwner(int act, int id);

	@Query("select r from Request r where r.status='PENDING' and r.pack.startDate < ?1")
	Collection<Request> getOld(Date now);

	@Query("select max(1.0*(select count(*) from Request r where r.pack.manager=m)) from Manager m")
	Integer MaxRequestPerManager();
	@Query("select min(1.0*(select count(*) from Request r where r.pack.manager=m)) from Manager m")
	Integer MinRequestPerManager();
	@Query("select avg(1.0*(select count(*) from Request r where r.pack.manager=m)) from Manager m")
	Double AvgRequestPerManager();
	@Query("select stddev(1.0*(select count(*) from Request r where r.pack.manager=m)) from Manager m")
	Double StddevRequestPerManager();

	@Query("select max(1.0*(select count(*) from Request r where r.pack.manager=m and r.status='PENDING')) from Manager m")
	Integer MaxRequestPerManagerPending();
	@Query("select min(1.0*(select count(*) from Request r where r.pack.manager=m and r.status='PENDING')) from Manager m")
	Integer MinRequestPerManagerPending();
	@Query("select avg(1.0*(select count(*) from Request r where r.pack.manager=m and r.status='PENDING')) from Manager m")
	Double AvgRequestPerManagerPending();
	@Query("select stddev(1.0*(select count(*) from Request r where r.pack.manager=m and r.status='PENDING')) from Manager m")
	Double StddevRequestPerManagerPending();

	@Query("select max(1.0*(select count(*) from Request r where r.customer=c)) from Customer c")
	Integer MaxRequestPerCustomer();
	@Query("select min(1.0*(select count(*) from Request r where r.customer=c)) from Customer c")
	Integer MinRequestPerCustomer();
	@Query("select avg(1.0*(select count(*) from Request r where r.customer=c)) from Customer c")
	Double AvgRequestPerCustomer();
	@Query("select stddev(1.0*(select count(*) from Request r where r.customer=c)) from Customer c")
	Double StddevRequestPerCustomer();

	@Query("select max(1.0*(select count(*) from Request r where r.customer=c and r.status='PENDING')) from Customer c")
	Integer MaxRequestPerCustomerPending();
	@Query("select min(1.0*(select count(*) from Request r where r.customer=c and r.status='PENDING')) from Customer c")
	Integer MinRequestPerCustomerPending();
	@Query("select avg(1.0*(select count(*) from Request r where r.customer=c and r.status='PENDING')) from Customer c")
	Double AvgRequestPerCustomerPending();
	@Query("select stddev(1.0*(select count(*) from Request r where r.customer=c and r.status='PENDING')) from Customer c")
	Double StddevRequestPerCustomerPending();

}
