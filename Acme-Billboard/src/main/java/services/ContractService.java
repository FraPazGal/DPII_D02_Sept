
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ContractRepository;
import domain.Actor;
import domain.Contract;
import domain.BillboardFile;
import domain.Manager;
import domain.Request;

@Service
@Transactional
public class ContractService {

	@Autowired
	private RequestService		requestService;
	@Autowired
	private BillboardFileService			billboardFileService;

	@Autowired
	private ContractRepository	contractRepository;

	@Autowired
	private PackageService		packageService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;

	@Autowired
	private UtilityService		utilityService;


	public Contract create(final Request request) {
		Contract result;
		final Manager principal = (Manager) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
		result = new Contract();
		result.setRequest(request);
		return result;
	}

	public Contract findOneByRequestAndOwner(final int id) {
		final Actor principal = this.actorService.findByPrincipal();
		final Contract contract = this.contractRepository.findOneByRequestAndOwner(id, principal.getId());
		Assert.notNull(contract);
		return contract;
	}
	public Contract findOne(final int Id) {
		final Actor principal = this.actorService.findByPrincipal();
		final Contract c = this.contractRepository.findOne(Id);
		Assert.isTrue(c.getRequest().getCustomer().getId() == principal.getId() || c.getRequest().getPack().getManager().getId() == principal.getId());
		return c;
	}
	public Contract reconstruct(final Contract contractF, final BindingResult binding) {
		// TODO Auto-generated method stub
		final Contract result = this.create(contractF.getRequest());
		if (contractF.getId() != 0) {
			final Contract orig = this.contractRepository.findOne(contractF.getId());
			Assert.notNull(orig);
			result.setId(contractF.getId());
		}
		result.setHash("");
		result.setText(contractF.getText());
		this.validator.validate(result, binding);
		return result;
	}

	public Contract save(final Contract contract) {
		final Actor principal = this.actorService.findByPrincipal();
		Contract result = this.create(contract.getRequest());
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
		Assert.isTrue(contract.getRequest().getPack().getManager().equals(principal));
		if (contract.getId() != 0) {
			result = this.contractRepository.findOne(contract.getId());
			Assert.notNull(result);
			Assert.isTrue(result.getRequest().getPack().getManager().equals(principal));
		}
		result.setHash(contract.getHash());
		result.setText(contract.getText());

		return this.contractRepository.save(result);

	}

	public Contract sign(final int id) {
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Contract contract = this.contractRepository.findOne(id);
		Assert.notNull(contract);
		Assert.isTrue(contract.getRequest().getCustomer().equals(principal) || contract.getRequest().getPack().getManager().equals(principal));
		final Collection<BillboardFile> billboardFiles = this.billboardFileService.getListAllByContract(id);
		Assert.isTrue(billboardFiles.size() >= 1, "file.one");
		final Date signed = new Date();
		if (this.actorService.checkAuthority(principal, "CUSTOMER")) {
			Assert.isTrue(contract.getSignedCustomer() == null && contract.getSignedManager() != null);
			contract.setSignedCustomer(signed);
		} else if (this.actorService.checkAuthority(principal, "MANAGER")) {
			Assert.isTrue(contract.getSignedCustomer() == null && contract.getSignedManager() == null);
			contract.setSignedManager(signed);
			final String hash = this.utilityService.getHash(contract.getText());
			contract.setHash(hash);
		}
		contract = this.contractRepository.save(contract);
		return contract;
	}

	public Collection<Contract> getListAll() {
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Collection<Contract> contract = new ArrayList<Contract>();
		if (this.actorService.checkAuthority(principal, "MANAGER"))
			contract = this.contractRepository.findAllManager(principal.getId());
		else if (this.actorService.checkAuthority(principal, "CUSTOMER"))
			contract = this.contractRepository.findAllCustomer(principal.getId());
		return contract;
	}

	public Collection<Contract> getList(final int id) {
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Collection<Contract> contract = new ArrayList<Contract>();
		final domain.Package pack = this.packageService.findOne(id);
		Assert.isTrue(pack.getManager().equals(principal));
		contract = this.contractRepository.findAllByPackage(id);
		return contract;
	}

	public void draftContract(final Request result) {
		Assert.isTrue(this.requestService.findOne(result.getId()).getStatus().equals("APPROVED"));
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(result.getPack().getManager().equals(principal));
		String enText = "";
		enText += "Agreement \r\n\r\n";
		enText += "This agreement is made by and between " + result.getPack().getManager().getName() + " (or '[company]' in short), based at " + result.getPack().getManager().getAddress() + " and " + result.getCustomer().getName()
			+ " (or '[contractor]' in short), based at " + result.getCustomer().getAddress() + ". With this agreement, [contractor] agrees to perform services for [company] for the billboard tentatively titled " + result.getPack().getTitle()
			+ " (or '[project]' in short),on the following terms and conditions. " + "\r\n\r\n";
		enText += "Start date: " + result.getPack().getStartDate() + " \r\n\r\n";
		enText += "End date (deadlines): " + result.getPack().getEndDate() + "  \r\n\r\n";
		enText += "Payment Amount: " + result.getPack().getPrice() + "  \r\n\r\n";
		enText += "Description of the project: " + result.getPack().getDescription() + "  \r\n\r\n";
		enText += "Billboard: " + result.getPack().getPhoto() + "  \r\n\r\n";
		enText += "Payment date: " + "To complete" + "  \r\n\r\n";
		enText += "Scope of right: " + "To complete" + "  \r\n\r\n";
		enText += "Jurisdiction: " + "Your country" + "  \r\n\r\n";
		enText += "[company] needs to approve the work resulting from [contractor]'s services before it is considered complete. [company] will therefore try to continuously and clearly communicate what makes the work complete and will try to approve or disapprove the work resulting from the services as soon as possible. [contractor] is aware that minor adjustments and fixes to the work are also part of the services."
			+ "\r\n\r\n";
		enText += "The deadline for [contractor] to complete the services is "
			+ result.getPack().getEndDate()
			+ ". Together, [company] and [contractor] can agree to set a new deadline or redefine the services to be performed for the existing deadline. When [contractor] becomes aware that [contractor] is unable to complete the services on the deadline, [contractor] will immediately notify [company]. If [contractor] is unable to complete the services on the deadline and [company] and [contractor] cannot agree on a new deadline, [company] can decide to no longer use [contractor]’s services."
			+ " \r\n\r\n";
		enText += "If [company] does not pay on time, [contractor] can delay deadlines with as many days as [company] delays the owed payment. If [company] wants [contractor] to perform services that are not listed above, [company] and [contractor] can agree to add services to this agreement or sign a new agreement."
			+ " \r\n\r\n";
		enText += "Compensation " + " \r\n\r\n";
		enText += "After [company] has approved the work delivered by [contractor], [company] agrees to pay [contractor] [paymentAmount] on [paymentDate]. " + " \r\n\r\n";
		enText += "All amounts in this agreement are excluding VAT (value added tax). [contractor] will send invoices to receive payment(s) from [company], after which [company] will make payment [paymentMethod] within 30 days of receiving an invoice. [contractor] will be responsible for all expenses made while performing the services under this agreement. Because [contractor] is not an employee of [company], [contractor] is not entitled to worker's compensation, retirement, insurance or other benefits afforded to employees of [company]. "
			+ " \r\n\r\n";
		enText += "Rights to the work " + " \r\n\r\n";
		enText += "[contractor] will remain owner of the rights to the work resulting from the services for this agreement, but gives [company] the rights to use and distribute the work as part of [projectName] [scopeOfRights]. These rights include all art, images, designs, typography, and text, as they are delivered to [company], but [company] can make minor changes to the work if needed. The rights [contractor] gives to [company] are non-exclusive, meaning that [contractor] still has the rights to use the work for projects other than [projectName] without permission from [company]. [company] may not use the work that [contractor] delivers to [company] in projects other than [projectName] without written permission from [contractor]. If [projectName] is made for another company or if [projectName] is distributed through another company, publisher, or platform, [company] can give that company the same rights to use and distribute the work as part of [projectName]. "
			+ " \r\n\r\n";
		enText += "[contractor] will make sure that [company] can make full use of the work, which means that [contractor] will only deliver work that [contractor] made, or work from others if it comes with a license that allows the work to be included in [projectName]. If [contractor] delivers work to [company] that is claimed to infringe the rights of others, then [contractor] will defend [company] against these claims, and [contractor] is responsible for all damages [company] may suffer. "
			+ " \r\n\r\n";
		enText += "[company] agrees to credit [contractor] in- and outside of [projectName] following the industry standard. [contractor] can showcase the work resulting from the services for [contractor]'s portfolio, but if [projectName] is still unreleased, [contractor] needs written permission from [company]. "
			+ " \r\n\r\n";
		enText += "Confidential information " + " \r\n\r\n";
		enText += "[contractor] agrees not to disclose or use any confidential information of [company] or [projectName] without [company]'s written permission, both during and after the term of this agreement. Confidential information is defined as information that [company] has not made public. "
			+ " \r\n\r\n";
		enText += "In case of a disagreement " + " \r\n\r\n";
		enText += "Both [company] and [contractor] cannot end the agreement without a good reason. In case of a disagreement, [company] and [contractor] will do their best to respectfully resolve the disagreement. If [company] and [contractor] cannot find a solution for their disagreement, and [company] or [contractor] wants to end the agreement, they need to give a seven day deadline to get to a solution. "
			+ " \r\n\r\n";
		enText += "If [company] and [contractor] did their best to resolve the disagreement during these seven days but could not come to a solution, this agreement can be ended, and any rights that [company] already paid for before the disagreement stay with [company]. [company] and [contractor] can come to an additional agreement about rights that [company] has not yet paid for. "
			+ " \r\n\r\n";
		enText += "In case of a lasting conflict and [company] and [contractor] want to use arbitration or go to court, they can only do that in [jurisdiction] using the laws of [jurisdiction]. " + " \r\n\r\n";
		enText += "Signatures " + " \r\n\r\n";
		enText += "[company] and [contractor] have both signed every page of this agreement, and each have their own copy of the signed agreement for future reference. This agreement starts on the latest date that [company] or [contractor] signs. "
			+ " \r\n\r\n";

		final Contract contract = this.create(result);
		contract.setText(enText);
		this.contractRepository.save(contract);
	}

	public void deleteAccount(final Request r) {
		final Actor principal = this.actorService.findByPrincipal();
		final Collection<Contract> con = this.contractRepository.findAllContract(r.getId());
		if (!con.isEmpty())
			for (final Contract c : con)
				this.billboardFileService.deleteAccount(c);
		this.contractRepository.deleteInBatch(con);
	}

}
