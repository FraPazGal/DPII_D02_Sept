
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

import repositories.CustomerRepository;
import security.Authority;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Actor;
import domain.CreditCard;
import domain.Customer;
import forms.EditionCustomerFormObject;
import forms.RegisterCustomerFormObject;

@Transactional
@Service
public class CustomerService {

	@Autowired
	private CustomerRepository			customerRepository;

	@Autowired
	private RequestService				requestService;

	@Autowired
	private ActorService				actorService;

	@Autowired
	private UtilityService				utilityService;

	@Autowired
	private CreditCardService			creditCardService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;
	@Autowired
	private UserAccountRepository		userAccountRepository;

	@Autowired
	private FinderService				finderService;


	public Customer findOneNot(final Integer id) {
		final Customer customer = this.customerRepository.findOne(id);
		Assert.notNull(customer);
		return customer;
	}

	public Customer create() {
		Customer res;
		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;
		CreditCard creditCard;

		creditCard = new CreditCard();
		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Customer();

		auth.setAuthority(Authority.CUSTOMER);
		authority.add(auth);
		userAccount.setAuthorities(authority);

		res.setUserAccount(userAccount);
		res.setCreditCard(creditCard);

		return res;
	}

	public Customer save(final Customer customer) {
		final Customer res = this.create();
		Customer principal;

		Assert.notNull(customer);
		final char[] phoneArray = customer.getPhoneNumber().toCharArray();
		if ((!customer.getPhoneNumber().equals(null) && !customer.getPhoneNumber().equals("")))
			if (phoneArray[0] != '+' && Character.isDigit(phoneArray[0])) {
				final String cc = this.systemConfigurationService.findMySystemConfiguration().getCountryCode();
				customer.setPhoneNumber(cc + " " + customer.getPhoneNumber());
			}
		if (customer.getId() == 0)
			res.setUserAccount(customer.getUserAccount());
		else {
			principal = (Customer) this.actorService.findByPrincipal();
			Assert.isTrue(principal.getId() == customer.getId(), "no.permission");
			res.setUserAccount(principal.getUserAccount());
			res.setId(principal.getId());
		}
		res.setName(customer.getName());
		res.setSurname(customer.getSurname());
		res.setMiddleName(customer.getMiddleName());
		res.setPhoto(customer.getPhoto());
		res.setEmail(customer.getEmail());
		res.setVat(customer.getVat());
		res.setPhoneNumber(customer.getPhoneNumber());
		res.setAddress(customer.getAddress());

		/* Creating credit card */
		final CreditCard creditCard = new CreditCard();

		creditCard.setHolder(customer.getCreditCard().getHolder());
		creditCard.setMake(customer.getCreditCard().getMake());
		creditCard.setNumber(customer.getCreditCard().getNumber());
		creditCard.setExpirationMonth(customer.getCreditCard().getExpirationMonth());
		creditCard.setExpirationYear(customer.getCreditCard().getExpirationYear());
		creditCard.setCVV(customer.getCreditCard().getCVV());
		res.setCreditCard(creditCard);
		final Customer result = this.customerRepository.save(res);
		this.finderService.defaultFinder(result);
		return result;
	}
	/* Other methods */

	public Customer reconstruct(final EditionCustomerFormObject form, final BindingResult binding) {

		final Customer res = this.create();

		res.setName(form.getName());
		res.setSurname(form.getSurname());
		res.setMiddleName(form.getMiddlename());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setVat(form.getVat());
		res.setPhoneNumber(form.getPhoneNumber());
		res.setAddress(form.getAddress());

		/* Creating credit card */
		final CreditCard creditCard = new CreditCard();

		creditCard.setHolder(form.getHolder());
		creditCard.setMake(form.getMake());
		creditCard.setNumber(form.getNumber());
		creditCard.setExpirationMonth(form.getExpirationMonth());
		creditCard.setExpirationYear(form.getExpirationYear());
		creditCard.setCVV(form.getCVV());

		res.setCreditCard(creditCard);
		final Actor principal = this.actorService.findByPrincipal();
		res.setId(principal.getId());

		if (form.getVat() != null)
			try {
				Assert.isTrue(this.utilityService.checkVAT(form.getVat()), "customer.vat.error");
			} catch (final Throwable oops) {
				binding.rejectValue("vat", "vat.error");
			}
		/* Credit card */
		if (form.getNumber() != null)
			try {
				Assert.isTrue(this.creditCardService.checkCreditCardNumber(creditCard.getNumber()), "card.number.error");
			} catch (final Throwable oops) {
				binding.rejectValue("number", "number.error");
			}

		if (creditCard.getExpirationMonth() != null && creditCard.getExpirationYear() != null) {

			try {
				Assert.isTrue(!this.creditCardService.checkIfExpired(creditCard.getExpirationMonth(), creditCard.getExpirationYear()), "card.date.error");
			} catch (final Throwable oops) {
				binding.rejectValue("expirationMonth", "card.date.error");
			}

			if (form.getCVV() != null)
				try {
					Assert.isTrue(form.getCVV() < 999 && form.getCVV() > 100, "CVV.error");
				} catch (final Throwable oops) {
					binding.rejectValue("CVV", "CVV.error");
				}
		}

		if (form.getEmail() != null)
			try {
				Assert.isTrue(this.actorService.checkEmail(form.getEmail(), "CUSTOMER"), "actor.email.error");
			} catch (final Throwable oops) {
				binding.rejectValue("email", "email.error");
			}

		return res;
	}

	public Customer reconstruct(final RegisterCustomerFormObject form, final BindingResult binding) {

		/* Creating customer */
		final Customer res = this.create();

		res.setName(form.getName());
		res.setSurname(form.getSurname());
		res.setMiddleName(form.getMiddlename());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setVat(form.getVat());
		res.setPhoneNumber(form.getPhoneNumber());
		res.setAddress(form.getAddress());

		/* Creating credit card */
		final CreditCard creditCard = new CreditCard();

		creditCard.setHolder(form.getHolder());
		creditCard.setMake(form.getMake());
		creditCard.setNumber(form.getNumber());
		creditCard.setExpirationMonth(form.getExpirationMonth());
		creditCard.setExpirationYear(form.getExpirationYear());
		creditCard.setCVV(form.getCVV());

		res.setCreditCard(creditCard);

		/* Creating user account */
		final UserAccount userAccount = new UserAccount();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.CUSTOMER);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);
		userAccount.setUsername(form.getUsername());
		userAccount.setPassword(encoder.encodePassword(form.getPassword(), null));
		res.setUserAccount(userAccount);

		if (form.getVat() != null)
			try {
				Assert.isTrue(this.utilityService.checkVAT(form.getVat()), "customer.vat.error");
			} catch (final Throwable oops) {
				binding.rejectValue("vat", "vat.error");
			}

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

		/* Credit card */
		if (form.getNumber() != null)
			try {
				Assert.isTrue(this.creditCardService.checkCreditCardNumber(creditCard.getNumber()), "card.number.error");
			} catch (final Throwable oops) {
				binding.rejectValue("number", "number.error");
			}

		if (creditCard.getExpirationMonth() != null && creditCard.getExpirationYear() != null) {

			try {
				Assert.isTrue(!this.creditCardService.checkIfExpired(creditCard.getExpirationMonth(), creditCard.getExpirationYear()), "card.date.error");
			} catch (final Throwable oops) {
				binding.rejectValue("expirationMonth", "card.date.error");
			}

			if (form.getCVV() != null)
				try {
					Assert.isTrue(form.getCVV() < 999 && form.getCVV() > 100, "CVV.error");
				} catch (final Throwable oops) {
					binding.rejectValue("CVV", "CVV.error");
				}
		}

		if (form.getEmail() != null)
			try {
				Assert.isTrue(this.actorService.checkEmail(form.getEmail(), "CUSTOMER"), "actor.email.error");
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
		//final Actor principal = this.actorService.findByPrincipal();
		this.requestService.deleteAccountCustomer();
		this.finderService.deleteAccountCustomer();
		//this.customerRepository.delete(principal.getId());
	}
}
