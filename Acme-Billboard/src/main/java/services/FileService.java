
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FileRepository;
import domain.Actor;
import domain.Contract;
import domain.File;
import domain.Manager;

@Transactional
@Service
public class FileService {

	@Autowired
	private FileRepository	fileRepository;

	@Autowired
	private ContractService	contractService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private Validator		validator;


	public File create(final Contract contract) {
		File result;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
		Assert.isTrue(contract.getRequest().getCustomer().getId() == principal.getId() || contract.getRequest().getPack().getManager().getId() == principal.getId());
		result = new File();
		result.setContract(contract);
		return result;
	}

	public Collection<File> getListAllByContract(final int id) {
		final Contract contract = this.contractService.findOne(id);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(contract.getRequest().getCustomer().getId() == principal.getId() || contract.getRequest().getPack().getManager().getId() == principal.getId());
		final Collection<File> files = this.fileRepository.findAllByContract(id);
		return files;
	}
	public File findOneIfOwner(final int id) {
		final File file = this.fileRepository.findOne(id);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(file.getContract().getRequest().getCustomer().getId() == principal.getId() || file.getContract().getRequest().getPack().getManager().getId() == principal.getId());
		return file;
	}

	public File reconstruct(final File fileF, final BindingResult binding) {
		final File result = this.create(fileF.getContract());
		final Manager principal = (Manager) this.actorService.findByPrincipal();
		this.contractService.findOne(fileF.getContract().getId());
		if (fileF.getId() != 0) {
			final File orig = this.fileRepository.findOne(fileF.getId());
			Assert.notNull(orig);
			Assert.isTrue(orig.getContract().getRequest().getPack().getManager().equals(principal));
			result.setId(fileF.getId());
		}
		result.setImage(fileF.getImage());
		result.setLocation(fileF.getLocation());
		this.validator.validate(result, binding);
		return result;
	}

	public File save(final File fileD) {
		final Actor principal = this.actorService.findByPrincipal();
		File result = this.create(fileD.getContract());
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"), "not.allowed");
		Assert.isTrue(fileD.getContract().getRequest().getPack().getManager().equals(principal));
		Assert.isNull(fileD.getContract().getSignedManager());
		if (fileD.getId() != 0) {
			result = this.fileRepository.findOne(fileD.getId());
			Assert.notNull(result);
			Assert.isTrue(result.getContract().getRequest().getPack().getManager().equals(principal));
			Assert.isNull(result.getContract().getSignedManager());
		}
		result.setImage(fileD.getImage());
		result.setLocation(fileD.getLocation());
		return this.fileRepository.save(result);

	}

	public Integer delete(final int id) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"), "not.allowed");
		Assert.notNull(principal);
		final File file = this.fileRepository.findOne(id);
		Assert.isNull(file.getContract().getSignedManager());
		Assert.notNull(file);
		Assert.isTrue(file.getId() != 0);
		Assert.isTrue(file.getContract().getRequest().getPack().getManager().equals(principal));
		final Integer i = file.getContract().getId();
		this.fileRepository.delete(file.getId());
		return i;
	}

	public void deleteAccount(final Contract c) {
		final Collection<File> files = this.fileRepository.findAllByContract(c.getId());
		if (!files.isEmpty())
			this.fileRepository.deleteInBatch(files);
	}
}
