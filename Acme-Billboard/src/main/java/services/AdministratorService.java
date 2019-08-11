
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

import repositories.AdministratorRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@Transactional
@Service
public class AdministratorService {

	@Autowired
	private AdministratorRepository		administratorRepository;

	@Autowired
	private ActorService				actorService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	public Administrator findOneNot(final Integer id) {
		final Administrator administrator = this.administratorRepository.findOne(id);
		Assert.notNull(administrator);
		return administrator;
	}

	public Administrator create() {

		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(actor, "ADMIN"));
		Administrator res;
		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Administrator();

		auth.setAuthority(Authority.ADMIN);
		authority.add(auth);
		userAccount.setAuthorities(authority);

		res.setUserAccount(userAccount);

		return res;
	}

	public Administrator save(final Administrator administrator) {
		final Administrator res = this.create();
		Actor principal;

		Assert.notNull(administrator);

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"), "no.permission");

		final char[] phoneArray = administrator.getPhoneNumber().toCharArray();
		if ((!administrator.getPhoneNumber().equals(null) && !administrator.getPhoneNumber().equals("")))
			if (phoneArray[0] != '+' && Character.isDigit(phoneArray[0])) {
				final String cc = this.systemConfigurationService.findMySystemConfiguration().getCountryCode();
				administrator.setPhoneNumber(cc + " " + administrator.getPhoneNumber());
			}
		if (administrator.getId() == 0)
			res.setUserAccount(administrator.getUserAccount());
		else {
			Assert.isTrue(principal.getId() == administrator.getId(), "no.permission");
			administrator.setUserAccount(principal.getUserAccount());
			res.setUserAccount(principal.getUserAccount());
			res.setId(principal.getId());
		}
		res.setName(administrator.getName());
		res.setSurname(administrator.getSurname());
		res.setMiddleName(administrator.getMiddleName());
		res.setPhoto(administrator.getPhoto());
		res.setEmail(administrator.getEmail());
		res.setPhoneNumber(administrator.getPhoneNumber());
		res.setAddress(administrator.getAddress());
		return this.administratorRepository.save(res);
	}
	/* Other methods */

	public Administrator reconstruct(final EditionFormObject form, final BindingResult binding) {

		/* Creating admin */
		final Administrator res = this.create();
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
				Assert.isTrue(this.actorService.checkEmail(form.getEmail(), "ADMIN"), "actor.email.error");

			} catch (final Throwable oops) {
				binding.rejectValue("email", "email.error");
			}

		return res;
	}

	public Administrator reconstruct(final RegisterFormObject form, final BindingResult binding) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));

		/* Creating admin */
		final Administrator res = this.create();

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
		authority.setAuthority(Authority.ADMIN);
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
				Assert.isTrue(this.actorService.checkEmail(form.getEmail(), "ADMIN"), "actor.email.error");

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

	public void delete() {
		// TODO Auto-generated method stub
		final Actor principal = this.actorService.findByPrincipal();
		this.administratorRepository.delete(principal.getId());

	}

}
