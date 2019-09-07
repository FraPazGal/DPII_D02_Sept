
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RadioFileRepository;
import domain.Actor;
import domain.Contract;
import domain.Manager;
import domain.RadioFile;
import domain.Request;

@Transactional
@Service
public class RadioFileService {

	@Autowired
	private RadioFileRepository	radioFileRepository;

	@Autowired
	private ContractService	contractService;
	
	@Autowired
	private RequestService	requestService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private Validator		validator;


	public RadioFile create(final Contract contract) {
		RadioFile result;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
		Assert.isTrue(contract.getRequest().getCustomer().getId() == principal.getId() || contract.getRequest().getPack().getManager().getId() == principal.getId());
		result = new RadioFile();
		result.setContract(contract);
		result.setFileType("RADIO");
		return result;
	}

	public Collection<RadioFile> getListAllByContract(final int id) {
		final Request request = this.requestService.findOne(id);
		final Contract contract = this.contractService.findOneByRequestAndOwner(request.getId());
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(contract.getRequest().getCustomer().getId() == principal.getId() || contract.getRequest().getPack().getManager().getId() == principal.getId());
		final Collection<RadioFile> radioFiles = this.radioFileRepository.findAllRadioFileByContract(contract.getId());
		return radioFiles;
	}
	
	public Collection<RadioFile> getListAllByContractToDelete(final int contractId) {
		final Contract contract = this.contractService.findOneToDelete(contractId);
		return this.radioFileRepository.findAllRadioFileByContract(contract.getId());
	}
	
	public RadioFile findOneIfOwner(final int id) {
		final RadioFile radioFile = this.radioFileRepository.findOne(id);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(radioFile.getContract().getRequest().getCustomer().getId() == principal.getId() || radioFile.getContract().getRequest().getPack().getManager().getId() == principal.getId());
		return radioFile;
	}

	public RadioFile reconstruct(final RadioFile fileF, final BindingResult binding) {
		final RadioFile result = this.create(fileF.getContract());
		final Manager principal = (Manager) this.actorService.findByPrincipal();
		this.contractService.findOne(fileF.getContract().getId());
		if (fileF.getId() != 0) {
			final RadioFile orig = this.radioFileRepository.findOne(fileF.getId());
			Assert.notNull(orig);
			Assert.isTrue(orig.getContract().getRequest().getPack().getManager().equals(principal));
			result.setId(fileF.getId());
		}
		result.setBroadcasterName(fileF.getBroadcasterName());
		result.setSchedule(fileF.getSchedule());
		result.setSound(fileF.getSound());
		this.validator.validate(result, binding);
		return result;
	}

	public RadioFile save(final RadioFile fileD) {
		final Actor principal = this.actorService.findByPrincipal();
		RadioFile result = this.create(fileD.getContract());
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"), "not.allowed");
		Assert.isTrue(fileD.getContract().getRequest().getPack().getManager().equals(principal));
		Assert.isNull(fileD.getContract().getSignedManager());
		if (fileD.getId() != 0) {
			result = this.radioFileRepository.findOne(fileD.getId());
			Assert.notNull(result);
			Assert.isTrue(result.getContract().getRequest().getPack().getManager().equals(principal));
			Assert.isNull(result.getContract().getSignedManager());
		}
		result.setBroadcasterName(fileD.getBroadcasterName());
		result.setSchedule(fileD.getSchedule());
		result.setSound(fileD.getSound());
		return this.radioFileRepository.save(result);

	}

	public Integer delete(final int id) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"), "not.allowed");
		Assert.notNull(principal);
		final RadioFile radioFile = this.radioFileRepository.findOne(id);
		Assert.isNull(radioFile.getContract().getSignedManager());
		Assert.notNull(radioFile);
		Assert.isTrue(radioFile.getId() != 0);
		Assert.isTrue(radioFile.getContract().getRequest().getPack().getManager().equals(principal));
		final Integer i = radioFile.getContract().getRequest().getId();
		this.radioFileRepository.delete(radioFile.getId());
		return i;
	}

	public void deleteAccount(final Contract c) {
		final Collection<RadioFile> radioFiles = this.radioFileRepository.findAllRadioFileByContract(c.getId());
		if (!radioFiles.isEmpty())
			this.radioFileRepository.deleteInBatch(radioFiles);
	}
	
	public Double[] StatsRadioFilesPerContract() {
		return this.radioFileRepository.statsRadioFilesPerContract();
	}
	
	public void deleteRadioFilesFromContract (Integer contractId) {
		Collection<RadioFile> radioFiles = this.getListAllByContractToDelete(contractId);
		for(RadioFile file : radioFiles) {
			this.radioFileRepository.delete(file.getId());
			this.flush();
		}
	}
	
	public void flush () {
		this.radioFileRepository.flush();
	}
}
