
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select p from Package p where  p.price >= ?1 and p.price <= ?2 and p.startDate >= ?4 and p.startDate <= ?3 and p.finalMode=1 and p.endDate >= ?4 and p.endDate <= ?3 and (p.ticker like %?5% or p.description like %?5% or p.title like %?5% or p.description like %?5%)")
	Collection<domain.Package> search(Double minimumSalary, Double maximumSalary, Date maximumDate, Date minimumDate, String keyWord);

	@Query("select p from Package p where p.finalMode = true and (p.ticker like %?1% or p.description like %?1% or p.title like %?1%)")
	Collection<domain.Package> searchAnon(String keyWord);

	@Query("select p from Package p where p.finalMode=0 ")
	Collection<domain.Package> AllPackages();

	@Query("select f from Finder f where f.customer.id = ?1")
	Finder findByCustomer(int id);

	@Query("select (sum(case when m.packages.size=0 then 1.0 else 0 end)/count(m)) from Finder m")
	Double RatioFindersEmpty();

	@Query("select max(f.packages.size), min(f.packages.size), avg(f.packages.size),sqrt(sum(f.packages.size* f.packages.size) / count(f.packages.size) -(avg(f.packages.size) * avg(f.packages.size))) from Finder f")
	Double[] StatsFinder();

	@Query("select count(f) from Finder f where ?1 MEMBER OF f.packages")
	Integer countRequestByPackage(int id);

	@Query("select f from Finder f where ?1 MEMBER OF f.packages")
	Collection<Finder> findByPack(int id);

}
