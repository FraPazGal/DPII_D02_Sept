
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SocialNetworkFileRepository;
import domain.Actor;
import domain.Contract;
import domain.Manager;
import domain.Request;
import domain.SocialNetworkFile;

@Transactional
@Service
public class SocialNetworkFileService {

	@Autowired
	private SocialNetworkFileRepository	socialNetworkFileRepository;

	@Autowired
	private ContractService	contractService;
	
	@Autowired
	private RequestService	requestService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private Validator		validator;


	public SocialNetworkFile create(final Contract contract) {
		SocialNetworkFile result;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
		Assert.isTrue(contract.getRequest().getCustomer().getId() == principal.getId() || contract.getRequest().getPack().getManager().getId() == principal.getId());
		result = new SocialNetworkFile();
		result.setContract(contract);
		result.setFileType("SOCIALNETWORK");
		return result;
	}

	public Collection<SocialNetworkFile> getListAllByContract(final int id) {
		final Request request = this.requestService.findOne(id);
		final Contract contract = this.contractService.findOneByRequestAndOwner(request.getId());
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(contract.getRequest().getCustomer().getId() == principal.getId() || contract.getRequest().getPack().getManager().getId() == principal.getId());
		final Collection<SocialNetworkFile> socialNetworkFiles = this.socialNetworkFileRepository.findAllSNFileByContract(contract.getId());
		return socialNetworkFiles;
	}
	
	public Collection<SocialNetworkFile> getListAllByContractToDelete(final int contractId) {
		final Contract contract = this.contractService.findOneToDelete(contractId);
		return this.socialNetworkFileRepository.findAllSNFileByContract(contract.getId());
	}
	
	public SocialNetworkFile findOneIfOwner(final int id) {
		final SocialNetworkFile socialNetworkFile = this.socialNetworkFileRepository.findOne(id);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(socialNetworkFile.getContract().getSignedManager() != null, "not allowed");
		Assert.isTrue(socialNetworkFile.getContract().getRequest().getCustomer().getId() == principal.getId() || socialNetworkFile.getContract().getRequest().getPack().getManager().getId() == principal.getId());
		return socialNetworkFile;
	}
	
	public SocialNetworkFile findOneIfOwnerAndDraft(final int id) {
		final SocialNetworkFile socialNetworkFile = this.socialNetworkFileRepository.findOne(id);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(socialNetworkFile.getContract().getSignedManager() == null, "not allowed");
		Assert.isTrue(socialNetworkFile.getContract().getRequest().getCustomer().getId() == principal.getId() || socialNetworkFile.getContract().getRequest().getPack().getManager().getId() == principal.getId());
		return socialNetworkFile;
	}

	public SocialNetworkFile reconstruct(final SocialNetworkFile fileF, final BindingResult binding) {
		final SocialNetworkFile result = this.create(fileF.getContract());
		final Manager principal = (Manager) this.actorService.findByPrincipal();
		this.contractService.findOne(fileF.getContract().getId());
		if (fileF.getId() != 0) {
			final SocialNetworkFile orig = this.socialNetworkFileRepository.findOne(fileF.getId());
			Assert.notNull(orig);
			Assert.isTrue(orig.getContract().getRequest().getPack().getManager().equals(principal));
			result.setId(fileF.getId());
		}
		result.setBanner(fileF.getBanner());
		result.setTarget(fileF.getTarget());
		this.validator.validate(result, binding);
		return result;
	}

	public SocialNetworkFile save(final SocialNetworkFile fileD) {
		final Actor principal = this.actorService.findByPrincipal();
		SocialNetworkFile result = this.create(fileD.getContract());
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"), "not.allowed");
		Assert.isTrue(fileD.getContract().getRequest().getPack().getManager().equals(principal));
		Assert.isNull(fileD.getContract().getSignedManager());
		if (fileD.getId() != 0) {
			result = this.socialNetworkFileRepository.findOne(fileD.getId());
			Assert.notNull(result);
			Assert.isTrue(result.getContract().getRequest().getPack().getManager().equals(principal));
			Assert.isNull(result.getContract().getSignedManager());
		}
		result.setBanner(fileD.getBanner());
		result.setTarget(fileD.getTarget());
		return this.socialNetworkFileRepository.save(result);

	}

	public Integer delete(final int id) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"), "not.allowed");
		Assert.notNull(principal);
		final SocialNetworkFile socialNetworkFile = this.socialNetworkFileRepository.findOne(id);
		Assert.isNull(socialNetworkFile.getContract().getSignedManager());
		Assert.notNull(socialNetworkFile);
		Assert.isTrue(socialNetworkFile.getId() != 0);
		Assert.isTrue(socialNetworkFile.getContract().getRequest().getPack().getManager().equals(principal));
		final Integer i = socialNetworkFile.getContract().getRequest().getId();
		this.socialNetworkFileRepository.delete(socialNetworkFile.getId());
		return i;
	}

	public void deleteAccount(final Contract c) {
		final Collection<SocialNetworkFile> socialNetworkFiles = this.socialNetworkFileRepository.findAllSNFileByContract(c.getId());
		if (!socialNetworkFiles.isEmpty())
			this.socialNetworkFileRepository.deleteInBatch(socialNetworkFiles);
	}
	
	public Double[] StatsSocialNetworkFilesPerContract() {
		return this.socialNetworkFileRepository.statsSNFilesPerContract();
	}
	
	public void deleteSocialNetworkFilesFromContract (Integer contractId) {
		Collection<SocialNetworkFile> socialNetworkFiles = this.getListAllByContractToDelete(contractId);
		for(SocialNetworkFile file : socialNetworkFiles) {
			this.socialNetworkFileRepository.delete(file.getId());
			this.flush();
		}
	}
	
	public void flush () {
		this.socialNetworkFileRepository.flush();
	}
}
