
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FinderRepository;
import domain.Actor;
import domain.Customer;
import domain.Finder;
import domain.Package;

@Transactional
@Service
public class FinderService {

	// Managed repository ------------------------------
	@Autowired
	private FinderRepository			finderRepository;

	// Supporting services -----------------------
	@Autowired
	private ActorService				actorService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private Validator					validator;


	// Constructors
	public FinderService() {
		super();
	}

	public Finder create() {
		Finder result;
		result = new Finder();
		final Collection<domain.Package> packages = new ArrayList<domain.Package>();
		result.setPackages(packages);
		return result;
	}

	public Finder defaultFinder(final Customer customer) {
		Finder finder = this.finderRepository.findByCustomer(customer.getId());
		if (finder == null) {
			final Finder f = this.create();
			f.setCustomer(customer);
			finder = this.save(f);
		}
		return finder;
	}

	// /FINDONE
	public Finder findOne(final int finderId) {
		Finder result;

		result = this.finderRepository.findOne(finderId);

		return result;
	}

	// FINDALL
	public Collection<Finder> findAll() {
		Collection<Finder> result;
		result = this.finderRepository.findAll();

		return result;

	}

	public Finder save(final Finder finder) {
		Finder result;
		Customer principal;
		Date currentMoment;
		currentMoment = new Date(System.currentTimeMillis() - 1);
		if (finder.getId() != 0) {
			final Actor actor = this.actorService.findByPrincipal();
			principal = (Customer) actor;
			Assert.isTrue(this.actorService.checkAuthority(principal, "CUSTOMER"), "not.allowed");
			Assert.isTrue(this.finderRepository.findByCustomer(principal.getId()).equals(finder), "not.allowed");
			Assert.notNull(finder, "not.allowed");
			if (finder.getMinPrice() != null)
				Assert.isTrue(finder.getMinPrice() >= 0., "not.negative");
			if (finder.getMaxPrice() != null)
				Assert.isTrue(finder.getMaxPrice() >= 0., "not.negative");
			finder.setCacheUpdate(currentMoment);
		}
		result = this.finderRepository.save(finder);
		Assert.notNull(result, "not.null");
		return result;
	}

	//DELETE 
	public void delete(final Finder finder) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "CUSTOMER"), "not.allowed");
		Assert.isTrue(finder.getId() != 0);
		Assert.isTrue(this.finderRepository.findByCustomer(principal.getId()).getId() == (finder.getId()), "not.allowed");
		finder.setKeyWord(null);
		finder.setMaxDate(null);
		finder.setMinDate(null);
		finder.setMaxPrice(null);
		finder.setMinPrice(null);
		final Collection<domain.Package> packages = new ArrayList<domain.Package>();
		finder.setPackages(packages);
		finder.setCacheUpdate(null);

		this.finderRepository.save(finder);
	}

	public void deleteExpiredFinder(final Finder finder) {
		Date maxLivedMoment = new Date();
		int timeChachedFind;
		Date currentMoment;
		currentMoment = new Date(System.currentTimeMillis() - 1);
		timeChachedFind = this.systemConfigurationService.findMySystemConfiguration().getTimeResultsCached();
		maxLivedMoment = DateUtils.addHours(currentMoment, -timeChachedFind);
		if (finder.getCacheUpdate().before(maxLivedMoment)) {
			finder.setKeyWord(null);
			finder.setMaxDate(null);
			finder.setMinDate(null);
			finder.setMaxPrice(null);
			finder.setMinPrice(null);
			finder.setPackages(null);
			finder.setCacheUpdate(null);
			this.finderRepository.save(finder);
		}
	}

	public Collection<domain.Package> search(final Finder finder) {
		Collection<domain.Package> results = new ArrayList<domain.Package>();
		String keyWord;
		Double minimumSalary;
		Double maximumSalary;
		Date maximumDate;
		Date minimumDate;
		final Actor principal = this.actorService.findByPrincipal();
		final Finder orig = this.finderByCustomer();
		Assert.isTrue(orig.getCustomer().equals(finder.getCustomer()) && finder.getCustomer().equals(principal) && finder.getId() == orig.getId());
		
		int nResults;
		if (finder.getMinPrice() != null)
			Assert.isTrue(finder.getMinPrice() >= 0., "not.negative");
		if (finder.getMaxPrice() != null)
			Assert.isTrue(finder.getMaxPrice() >= 0., "not.negative");
		final Collection<domain.Package> resultsPageables = new ArrayList<domain.Package>();
		nResults = this.systemConfigurationService.findMySystemConfiguration().getMaxResults();
		keyWord = (finder.getKeyWord() == null || finder.getKeyWord().isEmpty()) ? "" : finder.getKeyWord();
		minimumSalary = (finder.getMinPrice() == null) ? 0 : finder.getMinPrice();
		maximumSalary = (finder.getMaxPrice() == null) ? 1000000000 : finder.getMaxPrice();
		final Date maxDefaultDate = new GregorianCalendar(2200, Calendar.JANUARY, 1).getTime();
		maximumDate = finder.getMaxDate() == null ? maxDefaultDate : finder.getMaxDate();
		final Date minDefaultDate = new GregorianCalendar(0, Calendar.JANUARY, 1).getTime();
		minimumDate = finder.getMinDate() == null ? minDefaultDate : finder.getMinDate();
		results = this.finderRepository.search(minimumSalary, maximumSalary, maximumDate, minimumDate, keyWord);

		//
		//		if (finder.getDeadline() == null && (finder.getKeyWord() == null || finder.getKeyWord().isEmpty()) && finder.getMinimumSalary() == null && finder.getMaximumDeadline() == null)
		//			results = this.finderRepository.AllPackages();
		//		else if (finder.getDeadline() == null)
		//			results = this.finderRepository.search(minimumSalary, maximumDeadline, keyWord);
		//		else {
		//			if (finder.getMaximumDeadline() != null || !finder.getKeyWord().isEmpty() || finder.getMinimumSalary() != null)
		//				results = this.finderRepository.search(minimumSalary, maximumDeadline, keyWord);
		//			final List<domain.Package> resultsDeadline = new ArrayList<domain.Package>();
		//			resultsDeadline.add(this.finderRepository.searchDeadline(finder.getDeadline()));
		//			for (final domain.Package i : resultsDeadline)
		//				if (!results.contains(i))
		//					results.add(i);
		//		}

		int count = 0;
		for (final domain.Package p : results) {
			resultsPageables.add(p);
			count++;
			if (count >= nResults)
				break;
		}
		finder.setPackages(resultsPageables);
		this.save(finder);
		return resultsPageables;
	}

	public Collection<domain.Package> searchAnon(String keyWord) {

		Collection<domain.Package> results;

		keyWord = (keyWord == null || keyWord.isEmpty()) ? "" : keyWord;

		results = this.finderRepository.searchAnon(keyWord);

		return results;
	}

	public void flush() {
		this.finderRepository.flush();
	}

	protected void deleteFinder(final Customer customer) {
		final Actor principal = this.actorService.findByPrincipal();

		this.finderRepository.delete(this.finderRepository.findByCustomer(principal.getId()));
	}

	public Finder finderByCustomer() {
		Finder finder;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "CUSTOMER"), "not.allowed");
		finder = this.finderRepository.findByCustomer(principal.getId());
		Date maxLivedMoment = new Date();
		if (finder.getCacheUpdate() != null) {
			final int timeChachedFind = this.systemConfigurationService.findMySystemConfiguration().getTimeResultsCached();
			maxLivedMoment = DateUtils.addHours(maxLivedMoment, -timeChachedFind);
			if (finder.getCacheUpdate().before(maxLivedMoment))
				this.deleteExpiredFinder(finder);
		}
		return finder;
	}

	public Finder reconstruct(final Finder finder, final BindingResult binding) {

		final Finder find = this.create();
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "CUSTOMER"), "not.allowed");
		final Finder orig = this.finderByCustomer();
		find.setId(orig.getId());
		find.setVersion(orig.getVersion());
		find.setCustomer(orig.getCustomer());
		find.setKeyWord(finder.getKeyWord());
		find.setMaxDate(finder.getMaxDate());
		find.setMaxPrice(finder.getMaxPrice());
		find.setMinDate(finder.getMinDate());
		find.setMinPrice(finder.getMinPrice());
		this.validator.validate(find, binding);
		return find;
	}

	public Double ratioFinders() {

		return this.finderRepository.RatioFindersEmpty();

	}

	public Double[] StatsFinder() {
		return this.finderRepository.StatsFinder();
	}

	public List<Integer> finderStatisticRequest(final Collection<domain.Package> packs) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"), "not.allowed");
		final List<Integer> requests = new ArrayList<>();
		for (final domain.Package p : packs)
			requests.add(this.finderRepository.countRequestByPackage(p.getId()));
		return requests;
	}

	public void deleteAccountManager(final Package p) {
		final Collection<Finder> f = this.finderRepository.findByPack(p.getId());
		if (!f.isEmpty())
			for (final Finder fi : f) {
				final Collection<domain.Package> pack = fi.getPackages();
				fi.getPackages().remove(p);
				this.finderRepository.save(fi);
			}
	}

	public void deleteAccountCustomer() {
		final Actor principal = this.actorService.findByPrincipal();
		final Finder f = this.finderRepository.findByCustomer(principal.getId());
		final Collection<domain.Package> packages = new ArrayList<domain.Package>();
		f.setPackages(packages);
		this.finderRepository.save(f);
		this.finderRepository.delete(f);

	}
}
