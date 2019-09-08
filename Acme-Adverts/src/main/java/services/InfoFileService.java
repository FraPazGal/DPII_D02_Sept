
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.InfoFileRepository;
import domain.Actor;
import domain.Contract;
import domain.InfoFile;
import domain.Manager;
import domain.Request;

@Transactional
@Service
public class InfoFileService {

	@Autowired
	private InfoFileRepository	infoFileRepository;

	@Autowired
	private ContractService	contractService;
	
	@Autowired
	private RequestService	requestService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private Validator		validator;


	public InfoFile create(final Contract contract) {
		InfoFile result;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
		Assert.isTrue(contract.getRequest().getCustomer().getId() == principal.getId() || contract.getRequest().getPack().getManager().getId() == principal.getId());
		result = new InfoFile();
		result.setContract(contract);
		result.setFileType("INFO");
		return result;
	}

	public Collection<InfoFile> getListAllByContract(final int id) {
		final Request request = this.requestService.findOne(id);
		final Contract contract = this.contractService.findOneByRequestAndOwner(request.getId());
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(contract.getRequest().getCustomer().getId() == principal.getId() || contract.getRequest().getPack().getManager().getId() == principal.getId());
		final Collection<InfoFile> infoFiles = this.infoFileRepository.findAllInfoFileByContract(contract.getId());
		return infoFiles;
	}
	
	public Collection<InfoFile> getListAllByContractToDelete(final int contractId) {
		final Contract contract = this.contractService.findOneToDelete(contractId);
		return this.infoFileRepository.findAllInfoFileByContract(contract.getId());
	}
	
	public InfoFile findOneIfOwner(final int id) {
		final InfoFile infoFile = this.infoFileRepository.findOne(id);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(infoFile.getContract().getRequest().getCustomer().getId() == principal.getId() || infoFile.getContract().getRequest().getPack().getManager().getId() == principal.getId());
		return infoFile;
	}
	
	public InfoFile findOneIfOwnerAndDraft(final int id) {
		final InfoFile infoFile = this.infoFileRepository.findOne(id);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(infoFile.getContract().getSignedManager() == null, "not allowed");
		Assert.isTrue(infoFile.getContract().getRequest().getCustomer().getId() == principal.getId() || infoFile.getContract().getRequest().getPack().getManager().getId() == principal.getId());
		return infoFile;
	}

	public InfoFile reconstruct(final InfoFile fileF, final BindingResult binding) {
		final InfoFile result = this.create(fileF.getContract());
		final Manager principal = (Manager) this.actorService.findByPrincipal();
		this.contractService.findOne(fileF.getContract().getId());
		if (fileF.getId() != 0) {
			final InfoFile orig = this.infoFileRepository.findOne(fileF.getId());
			Assert.notNull(orig);
			Assert.isTrue(orig.getContract().getRequest().getPack().getManager().equals(principal));
			result.setId(fileF.getId());
		}
		result.setTitle(fileF.getTitle());
		result.setText(fileF.getText());
		this.validator.validate(result, binding);
		return result;
	}

	public InfoFile save(final InfoFile fileD) {
		final Actor principal = this.actorService.findByPrincipal();
		InfoFile result = this.create(fileD.getContract());
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"), "not.allowed");
		Assert.isTrue(fileD.getContract().getRequest().getPack().getManager().equals(principal));
		Assert.isNull(fileD.getContract().getSignedManager());
		if (fileD.getId() != 0) {
			result = this.infoFileRepository.findOne(fileD.getId());
			Assert.notNull(result);
			Assert.isTrue(result.getContract().getRequest().getPack().getManager().equals(principal));
			Assert.isNull(result.getContract().getSignedManager());
		}
		result.setTitle(fileD.getTitle());
		result.setText(fileD.getText());
		return this.infoFileRepository.save(result);

	}

	public Integer delete(final int id) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"), "not.allowed");
		Assert.notNull(principal);
		final InfoFile infoFile = this.infoFileRepository.findOne(id);
		Assert.isNull(infoFile.getContract().getSignedManager());
		Assert.notNull(infoFile);
		Assert.isTrue(infoFile.getId() != 0);
		Assert.isTrue(infoFile.getContract().getRequest().getPack().getManager().equals(principal));
		final Integer i = infoFile.getContract().getRequest().getId();
		this.infoFileRepository.delete(infoFile.getId());
		return i;
	}

	public void deleteAccount(final Contract c) {
		final Collection<InfoFile> infoFiles = this.infoFileRepository.findAllInfoFileByContract(c.getId());
		if (!infoFiles.isEmpty())
			this.infoFileRepository.deleteInBatch(infoFiles);
	}
	
	public Double[] StatsInfoFilesPerContract() {
		return this.infoFileRepository.statsInfoFilesPerContract();
	}
	
	public void deleteInfoFilesFromContract (Integer contractId) {
		Collection<InfoFile> infoFiles = this.getListAllByContractToDelete(contractId);
		for(InfoFile file : infoFiles) {
			this.infoFileRepository.delete(file.getId());
			this.flush();
		}
	}
	
	public void flush () {
		this.infoFileRepository.flush();
	}
}
