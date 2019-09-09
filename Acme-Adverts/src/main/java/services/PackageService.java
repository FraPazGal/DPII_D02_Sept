
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PackageRepository;
import domain.Actor;
import domain.Manager;
import forms.PackageForm;

@Transactional
@Service
public class PackageService {

	@Autowired
	private PackageRepository	packageRepository;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private FinderService		finderService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;

	@Autowired
	private UtilityService		utilityService;


	public domain.Package create() {
		domain.Package result;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));

		result = new domain.Package();
		result.setManager((Manager) principal);
		result.setFinalMode(false);
		return result;
	}
	public PackageForm createF() {
		PackageForm result;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));

		result = new PackageForm();
		result.setManager((Manager) principal);
		result.setFinalMode(false);
		return result;
	}

	public Collection<domain.Package> getListAll() {
		Collection<domain.Package> packs;
		packs = this.packageRepository.findAllFinal();

		return packs;
	}

	public Collection<domain.Package> getListLoged() {
		final Actor principal = this.actorService.findByPrincipal();
		Collection<domain.Package> packs;
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
		packs = this.packageRepository.findAllManager(principal.getId());

		return packs;
	}

	public domain.Package findOne(final int id) {
		return this.packageRepository.findOne(id);
	}

	public domain.Package findOneMode(final int id) {
		final domain.Package pack = this.findOne(id);
		if (!pack.getFinalMode()) {
			final Actor actor = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(actor, "MANAGER"));
			Assert.isTrue(pack.getManager().equals(actor));
		}
		return pack;
	}

	public void delete(final int id) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
		Assert.notNull(principal);
		final domain.Package pack = this.findOne(id);
		Assert.notNull(pack);
		Assert.isTrue(pack.getId() != 0);
		Assert.isTrue(pack.getFinalMode() == false);
		Assert.isTrue(pack.getManager().equals(principal));
		this.packageRepository.delete(pack);
	}

	public domain.Package reconstruct(final PackageForm packageF, final BindingResult binding) {
		// TODO Auto-generated method stub
		//ticker
		final domain.Package result = this.create();
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
		if (packageF.getId() != 0) {
			final domain.Package orig = this.findOne(packageF.getId());
			Assert.notNull(orig);
			Assert.isTrue(orig.getManager().equals(principal));
			Assert.isTrue(orig.getFinalMode() == false);
			result.setId(packageF.getId());
			result.setTicker(orig.getTicker());

		} else {
			final String ticker = this.utilityService.getTicker();
			result.setTicker(ticker);
		}

		result.setDescription(packageF.getDescription());
		result.setEndDate(packageF.getEndDate());
		result.setFinalMode(packageF.getFinalMode());
		result.setPhoto(packageF.getPhoto());
		result.setPrice(packageF.getPrice());
		result.setStartDate(packageF.getStartDate());
		result.setTitle(packageF.getTitle());
		this.validator.validate(result, binding);

		final Date now = new Date();
		if (packageF.getStartDate() != null)
			try {
				Assert.isTrue((packageF.getStartDate().after(now)), "package.startDate.error");

			} catch (final Throwable oops) {
				binding.rejectValue("startDate", "startDate.error");
			}

		if (packageF.getStartDate() != null && packageF.getEndDate() != null)
			try {
				Assert.isTrue((packageF.getStartDate().before(packageF.getEndDate())), "package.startDate.error");

			} catch (final Throwable oops) {
				binding.rejectValue("startDate", "startDate.error.after");
			}

		return result;
	}

	public domain.Package save(final domain.Package packageD) {
		// TODO Auto-generated method stub
		final Manager principal = (Manager) this.actorService.findByPrincipal();
		domain.Package result = this.create();

		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"), "not.allowed");
		Assert.isTrue(packageD.getManager().equals(principal));
		if (packageD.getId() != 0) {
			result = this.findOne(packageD.getId());
			Assert.notNull(result);
			Assert.isTrue(result.getFinalMode() == false);
			Assert.isTrue(result.getManager().equals(principal));

		}
		result.setDescription(packageD.getDescription());
		result.setEndDate(packageD.getEndDate());
		result.setFinalMode(packageD.getFinalMode());
		result.setManager(packageD.getManager());
		result.setPhoto(packageD.getPhoto());
		result.setPrice(packageD.getPrice());
		result.setStartDate(packageD.getStartDate());
		result.setTicker(packageD.getTicker());
		result.setTitle(packageD.getTitle());
		return this.packageRepository.save(result);
	}

	public domain.Package getNumberOfTickers(final String ticker) {
		return this.packageRepository.getPackageByTicker(ticker);
	}

	public List<String> getListLogedTitle() {
		final Actor principal = this.actorService.findByPrincipal();
		final List<String> packs = this.packageRepository.findAllManagerTitle(principal.getId());
		final List<String> titles = new ArrayList<>();
		for (int i = 0; i < packs.size(); i++)
			titles.add("'" + packs.get(i) + "'");
		return titles;
	}

	public void deleteAccount() {
		final Actor principal = this.actorService.findByPrincipal();
		final Collection<domain.Package> packs = this.packageRepository.findAllManager(principal.getId());
		if (!packs.isEmpty()) {
			for (final domain.Package p : packs) {
				this.finderService.deleteAccountManager(p);
				this.requestService.deleteAccountManager(p);
			}
			this.packageRepository.deleteInBatch(packs);
		}
	}
}
