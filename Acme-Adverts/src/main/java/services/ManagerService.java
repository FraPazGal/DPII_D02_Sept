
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
import domain.Actor;
import domain.BillboardFile;
import domain.Contract;
import domain.InfoFile;
import domain.Manager;
import domain.RadioFile;
import domain.Request;
import domain.SocialNetworkFile;
import domain.TVFile;
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
	private SystemConfigurationService	systemConfigurationService;

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
	
	@Autowired
	private ContractService	contractService;
	
	@Autowired
	private RequestService				requestService;


	public Manager create() {
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
			res.setVersion(principal.getVersion());
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
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
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
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
		this.packageService.deleteAccount();
		this.managerRepository.delete(principal.getId());
	}
	
	public String export() {
		String res;
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(actor, "MANAGER"));
		
		res = "Data of your user account:";
		res += "\r\n\r\n";
		res += "Name: " + actor.getName() + " \r\n" + "Surname: " + actor.getSurname() + " \r\n" + "VAT:" + actor.getMiddleName() + " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: " + actor.getEmail() + " \r\n" + "Phone Number: "
			+ actor.getPhoneNumber() + " \r\n" + "Address: " + actor.getAddress() + " \r\n" + " \r\n" + "\r\n";
		res += "\r\n\r\n";
		res += "----------------------------------------";
		res += "\r\n\r\n";
		res += "Packages:";
		res += "\r\n\r\n";
		final Collection<domain.Package> packs = this.packageService.getListLoged();

		for (final domain.Package p : packs) {
			res += "----------------------------------------";
			res += "\r\n\r\n";
			res += "Package: " + "\r\n\r\n";
			res += "Ticker: " + p.getTicker() + "\r\n\r\n" + "Title: " + p.getTitle() + "\r\n\r\n" + "Description: " + p.getDescription() + "\r\n\r\n" + "Start Date: " + p.getStartDate() + "\r\n\r\n" + "End Date: " + p.getEndDate() + "\r\n\r\n"
				+ "Photo: " + p.getPhoto() + "Price: " + p.getPrice() + "\r\n\r\n";
			res += "\r\n\r\n";

		}
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
