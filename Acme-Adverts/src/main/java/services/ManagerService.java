
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.ManagerRepository;
import security.Authority;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Actor;
import domain.Manager;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@Transactional
@Service
public class ManagerService {

	@Autowired
	private ManagerRepository			managerRepository;

	@Autowired
	private PackageService				packageService;

	@Autowired
	private ActorService				actorService;

	@Autowired
	private UtilityService				utilityService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private UserAccountRepository		userAccountRepository;


	public Manager create() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));

		Manager res;
		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Manager();

		auth.setAuthority(Authority.MANAGER);
		authority.add(auth);
		userAccount.setAuthorities(authority);

		res.setUserAccount(userAccount);

		return res;
	}

	public Manager findOneNot(final Integer id) {
		final Manager manager = this.managerRepository.findOne(id);
		Assert.notNull(manager);
		return manager;
	}

	public Manager save(final Manager manager) {
		final Manager res = this.create();
		Manager principal;

		Assert.notNull(manager);
		final char[] phoneArray = manager.getPhoneNumber().toCharArray();
		if ((!manager.getPhoneNumber().equals(null) && !manager.getPhoneNumber().equals("")))
			if (phoneArray[0] != '+' && Character.isDigit(phoneArray[0])) {
				final String cc = this.systemConfigurationService.findMySystemConfiguration().getCountryCode();
				manager.setPhoneNumber(cc + " " + manager.getPhoneNumber());
			}
		if (manager.getId() == 0)
			res.setUserAccount(manager.getUserAccount());
		else {
			principal = (Manager) this.actorService.findByPrincipal();
			Assert.isTrue(principal.getId() == manager.getId(), "no.permission");
			res.setUserAccount(principal.getUserAccount());
			res.setId(principal.getId());
		}
		res.setName(manager.getName());
		res.setSurname(manager.getSurname());
		res.setMiddleName(manager.getMiddleName());
		res.setPhoto(manager.getPhoto());
		res.setEmail(manager.getEmail());
		res.setPhoneNumber(manager.getPhoneNumber());
		res.setAddress(manager.getAddress());
		return this.managerRepository.save(res);
	}

	/* Other methods */

	public Manager reconstruct(final EditionFormObject form, final BindingResult binding) {

		final Manager res = this.create();
		final Actor principal = this.actorService.findByPrincipal();
		res.setId(principal.getId());
		res.setVersion(form.getVersion());
		res.setName(form.getName());
		res.setSurname(form.getSurname());
		res.setMiddleName(form.getMiddlename());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setPhoneNumber(form.getPhoneNumber());
		res.setAddress(form.getAddress());

		if (form.getEmail() != null)
			try {
				Assert.isTrue(this.actorService.checkEmail(form.getEmail(), "MANAGER"), "actor.email.error");
			} catch (final Throwable oops) {
				binding.rejectValue("email", "email.error");
			}

		return res;
	}

	public Manager reconstruct(final RegisterFormObject form, final BindingResult binding) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		/* Creating manager */
		final Manager res = this.create();

		res.setName(form.getName());
		res.setSurname(form.getSurname());
		res.setMiddleName(form.getMiddlename());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setPhoneNumber(form.getPhoneNumber());
		res.setAddress(form.getAddress());
		/* Creating user account */
		final UserAccount userAccount = new UserAccount();

		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.MANAGER);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);
		userAccount.setUsername(form.getUsername());
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		userAccount.setPassword(encoder.encodePassword(form.getPassword(), null));
		res.setUserAccount(userAccount);

		/* Password confirmation */
		if (form.getPassword() != null)
			try {
				Assert.isTrue(form.getPassword().equals(form.getPassConfirmation()), "pass.confirm.error");
			} catch (final Throwable oops) {
				binding.rejectValue("password", "pass.confirm.error");
			}

		/* Terms&Conditions */
		if (form.getTermsAndConditions() != null)
			try {
				Assert.isTrue((form.getTermsAndConditions()), "terms.error");
			} catch (final Throwable oops) {
				binding.rejectValue("termsAndConditions", "terms.error");
			}

		if (form.getEmail() != null)
			try {
				Assert.isTrue(this.actorService.checkEmail(form.getEmail(), "MANAGER"), "actor.email.error");
			} catch (final Throwable oops) {
				binding.rejectValue("email", "email.error");
			}

		/* Username */
		if (form.getUsername() != null)
			try {
				Assert.isTrue(this.actorService.existsUsername(form.getUsername()), "username.error");
			} catch (final Throwable oops) {
				binding.rejectValue("username", "username.error");
			}

		return res;
	}

	public List<Manager> top10() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		final List<Manager> managers = this.managerRepository.top10();
		return (managers.size() >= 10) ? managers.subList(0, 10) : managers.subList(0, managers.size());

	}

	public void delete() {
		final Actor principal = this.actorService.findByPrincipal();
		this.packageService.deleteAccount();
		this.managerRepository.delete(principal.getId());
		// TODO Auto-generated method stub

	}
}
