
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.TVFileRepository;
import domain.Actor;
import domain.Contract;
import domain.Manager;
import domain.Request;
import domain.TVFile;

@Transactional
@Service
public class TVFileService {

	@Autowired
	private TVFileRepository	TVFileRepository;

	@Autowired
	private ContractService	contractService;
	
	@Autowired
	private RequestService	requestService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private Validator		validator;


	public TVFile create(final Contract contract) {
		TVFile result;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
		Assert.isTrue(contract.getRequest().getCustomer().getId() == principal.getId() || contract.getRequest().getPack().getManager().getId() == principal.getId());
		result = new TVFile();
		result.setContract(contract);
		result.setFileType("TV");
		return result;
	}

	public Collection<TVFile> getListAllByContract(final int id) {
		final Request request = this.requestService.findOne(id);
		final Contract contract = this.contractService.findOneByRequestAndOwner(request.getId());
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(contract.getRequest().getCustomer().getId() == principal.getId() || contract.getRequest().getPack().getManager().getId() == principal.getId());
		final Collection<TVFile> TVFiles = this.TVFileRepository.findAllTVFileByContract(contract.getId());
		return TVFiles;
	}
	
	public Collection<TVFile> getListAllByContractToDelete(final int contractId) {
		final Contract contract = this.contractService.findOneToDelete(contractId);
		return this.TVFileRepository.findAllTVFileByContract(contract.getId());
	}
	
	public TVFile findOneToDisplay(final int id) {
		final TVFile TVFile = this.TVFileRepository.findOne(id);
		final Actor principal = this.actorService.findByPrincipal();
		if(!this.actorService.checkAuthority(principal, "MANAGER")) {
			Assert.isTrue(TVFile.getContract().getSignedManager() != null, "not allowed");
		}
		Assert.isTrue(TVFile.getContract().getRequest().getCustomer().getId() == principal.getId() || TVFile.getContract().getRequest().getPack().getManager().getId() == principal.getId());
		return TVFile;
	}
	
	public TVFile findOneIfOwnerAndDraft(final int id, boolean draft) {
		final TVFile TVFile = this.TVFileRepository.findOne(id);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue((TVFile.getContract().getSignedManager() == null) == draft, "not allowed");
		Assert.isTrue(TVFile.getContract().getRequest().getCustomer().getId() == principal.getId() || TVFile.getContract().getRequest().getPack().getManager().getId() == principal.getId());
		return TVFile;
	}

	public TVFile reconstruct(final TVFile fileF, final BindingResult binding) {
		final TVFile result = this.create(fileF.getContract());
		final Manager principal = (Manager) this.actorService.findByPrincipal();
		this.contractService.findOne(fileF.getContract().getId());
		if (fileF.getId() != 0) {
			final TVFile orig = this.TVFileRepository.findOne(fileF.getId());
			Assert.notNull(orig);
			Assert.isTrue(orig.getContract().getRequest().getPack().getManager().equals(principal));
			result.setId(fileF.getId());
		}
		result.setBroadcasterName(fileF.getBroadcasterName());
		result.setSchedule(fileF.getSchedule());
		result.setVideo(fileF.getVideo());
		this.validator.validate(result, binding);
		return result;
	}

	public TVFile save(final TVFile fileD) {
		final Actor principal = this.actorService.findByPrincipal();
		TVFile result = this.create(fileD.getContract());
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"), "not.allowed");
		Assert.isTrue(fileD.getContract().getRequest().getPack().getManager().equals(principal));
		Assert.isNull(fileD.getContract().getSignedManager());
		if (fileD.getId() != 0) {
			result = this.TVFileRepository.findOne(fileD.getId());
			Assert.notNull(result);
			Assert.isTrue(result.getContract().getRequest().getPack().getManager().equals(principal));
			Assert.isNull(result.getContract().getSignedManager());
		}
		result.setBroadcasterName(fileD.getBroadcasterName());
		result.setSchedule(fileD.getSchedule());
		result.setVideo(fileD.getVideo());
		return this.TVFileRepository.save(result);

	}

	public Integer delete(final int id) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"), "not.allowed");
		Assert.notNull(principal);
		final TVFile TVFile = this.TVFileRepository.findOne(id);
		Assert.isNull(TVFile.getContract().getSignedManager());
		Assert.notNull(TVFile);
		Assert.isTrue(TVFile.getId() != 0);
		Assert.isTrue(TVFile.getContract().getRequest().getPack().getManager().equals(principal));
		final Integer i = TVFile.getContract().getRequest().getId();
		this.TVFileRepository.delete(TVFile.getId());
		return i;
	}

	public void deleteAccount(final Contract c) {
		final Collection<TVFile> TVFiles = this.TVFileRepository.findAllTVFileByContract(c.getId());
		if (!TVFiles.isEmpty())
			this.TVFileRepository.deleteInBatch(TVFiles);
	}
	
	public Double[] StatsTVFilesPerContract() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.TVFileRepository.statsTVFilesPerContract();
	}
	
	public void deleteTVFilesFromContract (Integer contractId) {
		Collection<TVFile> TVFiles = this.getListAllByContractToDelete(contractId);
		for(TVFile file : TVFiles) {
			this.TVFileRepository.delete(file.getId());
			this.flush();
		}
	}
	
	public void flush () {
		this.TVFileRepository.flush();
	}
}
