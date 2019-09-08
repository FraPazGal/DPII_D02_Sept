
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BillboardFileRepository;
import domain.Actor;
import domain.BillboardFile;
import domain.Contract;
import domain.File;
import domain.Manager;
import domain.Request;

@Transactional
@Service
public class BillboardFileService {

	@Autowired
	private BillboardFileRepository	billboardFileRepository;

	@Autowired
	private ContractService	contractService;
	
	@Autowired
	private RequestService	requestService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private Validator		validator;


	public BillboardFile create(final Contract contract) {
		BillboardFile result;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
		Assert.isTrue(contract.getRequest().getCustomer().getId() == principal.getId() || contract.getRequest().getPack().getManager().getId() == principal.getId());
		result = new BillboardFile();
		result.setContract(contract);
		result.setFileType("BILLBOARD");
		return result;
	}

	public Collection<BillboardFile> getListAllByContract(final int contractId) {
		final Request request = this.requestService.findOne(contractId);
		final Contract contract = this.contractService.findOneByRequestAndOwner(request.getId());
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(contract.getRequest().getCustomer().getId() == principal.getId() || contract.getRequest().getPack().getManager().getId() == principal.getId());
		final Collection<BillboardFile> billboardFiles = this.billboardFileRepository.getListAllByContract(contract.getId());
		return billboardFiles;
	}
	
	public Collection<BillboardFile> getListAllByContractToDelete(final int contractId) {
		final Contract contract = this.contractService.findOneToDelete(contractId);
		return this.billboardFileRepository.getListAllByContract(contract.getId());
	}
	
	public Collection<File> findAllFilesByContract(final int contractId) {
		
		return this.billboardFileRepository.findAllFilesByContract(contractId);
	}
	
	public BillboardFile findOneIfOwner(final int id) {
		final BillboardFile billboardFile = this.billboardFileRepository.findOne(id);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(billboardFile.getContract().getRequest().getCustomer().getId() == principal.getId() || billboardFile.getContract().getRequest().getPack().getManager().getId() == principal.getId());
		return billboardFile;
	}
	
	public BillboardFile findOneIfOwnerAndDraft(final int id) {
		final BillboardFile billboardFile = this.billboardFileRepository.findOne(id);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(billboardFile.getContract().getSignedManager() == null, "not allowed");
		Assert.isTrue(billboardFile.getContract().getRequest().getCustomer().getId() == principal.getId() || billboardFile.getContract().getRequest().getPack().getManager().getId() == principal.getId());
		return billboardFile;
	}

	public BillboardFile reconstruct(final BillboardFile fileF, final BindingResult binding) {
		final BillboardFile result = this.create(fileF.getContract());
		final Manager principal = (Manager) this.actorService.findByPrincipal();
		this.contractService.findOne(fileF.getContract().getId());
		if (fileF.getId() != 0) {
			final BillboardFile orig = this.billboardFileRepository.findOne(fileF.getId());
			Assert.notNull(orig);
			Assert.isTrue(orig.getContract().getRequest().getPack().getManager().equals(principal));
			result.setId(fileF.getId());
		}
		result.setImage(fileF.getImage());
		result.setLocation(fileF.getLocation());
		this.validator.validate(result, binding);
		return result;
	}

	public BillboardFile save(final BillboardFile fileD) {
		final Actor principal = this.actorService.findByPrincipal();
		BillboardFile result = this.create(fileD.getContract());
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"), "not.allowed");
		Assert.isTrue(fileD.getContract().getRequest().getPack().getManager().equals(principal));
		Assert.isNull(fileD.getContract().getSignedManager());
		if (fileD.getId() != 0) {
			result = this.billboardFileRepository.findOne(fileD.getId());
			Assert.notNull(result);
			Assert.isTrue(result.getContract().getRequest().getPack().getManager().equals(principal));
			Assert.isNull(result.getContract().getSignedManager());
		}
		result.setImage(fileD.getImage());
		result.setLocation(fileD.getLocation());
		return this.billboardFileRepository.save(result);

	}

	public Integer delete(final int id) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"), "not.allowed");
		Assert.notNull(principal);
		final BillboardFile billboardFile = this.billboardFileRepository.findOne(id);
		Assert.isNull(billboardFile.getContract().getSignedManager());
		Assert.notNull(billboardFile);
		Assert.isTrue(billboardFile.getId() != 0);
		Assert.isTrue(billboardFile.getContract().getRequest().getPack().getManager().equals(principal));
		final Integer i = billboardFile.getContract().getRequest().getId();
		this.billboardFileRepository.delete(billboardFile.getId());
		return i;
	}

	public void deleteAccount(final Contract c) {
		final Collection<BillboardFile> billboardFiles = this.billboardFileRepository.getListAllByContract(c.getId());
		if (!billboardFiles.isEmpty())
			this.billboardFileRepository.deleteInBatch(billboardFiles);
	}
	
	public Double[] StatsBillboardFilesPerContract() {
		return this.billboardFileRepository.statsBillboardFilesPerContract();
	}
	
	public Double[] StatsFilesPerContract() {
		return this.billboardFileRepository.statsFilesPerContract();
	}
	
	public void deleteBillboardFilesFromContract (Integer contractId) {
		Collection<BillboardFile> billboardFiles = this.getListAllByContractToDelete(contractId);
		for(BillboardFile file : billboardFiles) {
			this.billboardFileRepository.delete(file.getId());
			this.flush();
		}
	}
	
	public void flush () {
		this.billboardFileRepository.flush();
	}
}
