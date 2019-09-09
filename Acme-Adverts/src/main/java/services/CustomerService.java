
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
import domain.Actor;
import domain.BillboardFile;
import domain.Contract;
import domain.CreditCard;
import domain.Customer;
import domain.Finder;
import domain.InfoFile;
import domain.RadioFile;
import domain.Request;
import domain.SocialNetworkFile;
import domain.TVFile;
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
	private FinderService				finderService;
	
	@Autowired
	private ContractService	contractService;
	
	@Autowired
	private BillboardFileService	billboardFileService;
	
	@Autowired
	private SocialNetworkFileService	socialNetworkFileService;
	
	@Autowired
	private InfoFileService	infoFileService;
	
	@Autowired
	private TVFileService	TVFileService;
	
	@Autowired
	private RadioFileService	radioFileService;


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
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "CUSTOMER"));
		this.requestService.deleteAccountCustomer();
		this.finderService.deleteAccountCustomer();
		//this.customerRepository.delete(principal.getId());
	}
	
	public String export() {
		String res;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "CUSTOMER"));
		final Customer actor = (Customer) principal;
		res = "Data of your user account:";
		res += "\r\n\r\n";
		res += "Name: " + actor.getName() + " \r\n" + "Surname: " + actor.getSurname() + " \r\n" + "MiddleName:" + actor.getMiddleName() + " \r\n" + "VAT:" + actor.getVat() + " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: " + actor.getEmail()
			+ " \r\n" + "Phone Number: " + actor.getPhoneNumber() + " \r\n" + "Address: " + actor.getAddress() + "\r\n" + " \r\n" + " \r\n" + "\r\n" + "Credit Card:" + "\r\n" + "Holder:" + actor.getCreditCard().getHolder() + "\r\n" +

			"Make:" + actor.getCreditCard().getMake() + "\r\n" + "Number:" + actor.getCreditCard().getNumber() + "\r\n" + "Date expiration:" + actor.getCreditCard().getExpirationMonth() + "/" + actor.getCreditCard().getExpirationYear() + "\r\n" + "CVV:"
			+ actor.getCreditCard().getCVV();
		res += "\r\n\r\n";
		res += "----------------------------------------";
		res += "\r\n\r\n";

		final Collection<Request> requests = this.requestService.getListAll();

		for (final Request r : requests) {
			res += "----------------------------------------";
			res += "\r\n\r\n";
			res += "Request: " + "\r\n\r\n";
			res += "Status: " + r.getStatus() + "\r\n\r\n";
			res += "Customer comments:  " + r.getCommentsCustomer() + "\r\n\r\n";
			res += "Manager Comments: " + r.getCommentsManager() + "\r\n\r\n";
			res += "Pack: " + r.getPack().getTitle() + "\r\n\r\n";
			res += "-----------";
		}
		
		final Finder find = this.finderService.finderByCustomer();
		
		res += "----------------------------------------";
		res += "\r\n\r\n";
		res += "Finder: " + "\r\n\r\n";
		res += "Keyword: " + find.getKeyWord() + "\r\n\r\n";
		res += "Max price:  " + find.getMaxPrice() + "\r\n\r\n";
		res += "Min price " + find.getMinPrice() + "\r\n\r\n";
		res += "Max date: " + find.getMaxDate() + "\r\n\r\n";
		res += "Min date: " + find.getMinDate() + "\r\n\r\n";
		res += "Packages: " + "\r\n\r\n";
		if(find.getPackages() != null) {
			for (final domain.Package p : find.getPackages()) {
				res += "----------------------------------------";
				res += "\r\n\r\n";
				res += "Package: " + "\r\n\r\n";
				res += "Ticker: " + p.getTicker() + "\r\n\r\n" + "Title: " + p.getTitle() + "\r\n\r\n" + "Description: " + p.getDescription() + "\r\n\r\n" + "Start Date: " + p.getStartDate() + "\r\n\r\n" + "End Date: " + p.getEndDate() + "\r\n\r\n"
					+ "Photo: " + p.getPhoto() + "Price: " + p.getPrice() + "\r\n\r\n";
				res += "\r\n\r\n";
			}
		}
		res += "-----------";

		final Collection<Contract> contracts = this.contractService.getListAll();
		for (final Contract c : contracts) {
			final Collection<BillboardFile> billboardFiles = this.billboardFileService.getListAllByContractToDelete(c.getId());
			final Collection<InfoFile> infoFiles = this.infoFileService.getListAllByContractToDelete(c.getId());
			final Collection<RadioFile> radioFiles = this.radioFileService.getListAllByContractToDelete(c.getId());
			final Collection<TVFile> TVFiles = this.TVFileService.getListAllByContractToDelete(c.getId());
			final Collection<SocialNetworkFile> socialNetworkFiles = this.socialNetworkFileService.getListAllByContractToDelete(c.getId());
			res += "----------------------------------------";
			res += "\r\n\r\n";
			res += "Contract: " + "\r\n\r\n";
			res += "Customer signed date: " + c.getSignedCustomer() + "\r\n\r\n";
			res += "Manager signed date: " + c.getSignedManager() + "\r\n\r\n";
			res += "Text:  " + c.getText() + "\r\n\r\n";
			res += "Pack: " + c.getRequest().getPack().getTitle() + "\r\n\r\n\r\n";
			res += "Files: " + "\r\n\r\n\r\n";
			res += "Billboard Files: " + "\r\n\r\n";
			for (final BillboardFile f : billboardFiles) {
				res += "Location: " + f.getLocation() + "\r\n\r\n";
				res += "Image: " + f.getImage() + "\r\n\r\n";
				res += "-----";
				res += "\r\n";
			}
			res += "-----------" + "\r\n\r\n";
			res += "Info Files: " + "\r\n\r\n";
			for (final InfoFile f : infoFiles) {
				res += "Title: " + f.getTitle() + "\r\n\r\n";
				res += "Text: " + f.getText() + "\r\n\r\n";
				res += "-----";
				res += "\r\n";
			}
			res += "-----------" + "\r\n\r\n";
			res += "Radio Files: " + "\r\n\r\n";
			for (final RadioFile f : radioFiles) {
				res += "Broadcaster Name: " + f.getBroadcasterName() + "\r\n\r\n";
				res += "Schedule: " + f.getSchedule() + "\r\n\r\n";
				res += "Sound: " + f.getSound() + "\r\n\r\n";
				res += "-----";
				res += "\r\n";
			}
			res += "-----------" + "\r\n\r\n";
			res += "TV Files: " + "\r\n\r\n";
			for (final TVFile f : TVFiles) {
				res += "Broadcaster Name: " + f.getBroadcasterName() + "\r\n\r\n";
				res += "Schedule: " + f.getSchedule() + "\r\n\r\n";
				res += "Video: " + f.getVideo() + "\r\n\r\n";
				res += "-----";
				res += "\r\n";
			}
			res += "-----------" + "\r\n\r\n";
			res += "Social Network Files: " + "\r\n\r\n";
			for (final SocialNetworkFile f : socialNetworkFiles) {
				res += "Banner: " + f.getBanner() + "\r\n\r\n";
				res += "Target: " + f.getTarget() + "\r\n\r\n";
				res += "-----";
				res += "\r\n";
			}
			res += "-----------";
			
		}
		
		return res;
	}
}
