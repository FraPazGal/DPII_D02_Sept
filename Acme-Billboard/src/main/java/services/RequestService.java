
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

import repositories.RequestRepository;
import domain.Actor;
import domain.Customer;
import domain.Package;
import domain.Request;

@Transactional
@Service
public class RequestService {

	@Autowired
	private BillboardFileService			billboardFileService;

	@Autowired
	private ContractService		contractService;

	@Autowired
	private RequestRepository	requestRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;


	public Request create(final domain.Package pack) {
		Request result;
		Assert.isTrue(pack.getFinalMode());
		result = new Request();
		result.setCommentsCustomer("");
		result.setCommentsManager("");
		result.setStatus("PENDING");
		result.setPack(pack);
		return result;
	}

	public Collection<Request> getListAll() {
		this.checkRequest();

		Actor principal;
		principal = this.actorService.findByPrincipal();
		Collection<Request> request = new ArrayList<Request>();
		if (this.actorService.checkAuthority(principal, "MANAGER"))
			request = this.requestRepository.findAllFinal(principal.getId());
		else if (this.actorService.checkAuthority(principal, "CUSTOMER"))
			request = this.requestRepository.findAllCustomer(principal.getId());

		return request;
	}
	public Request findOne(final int id) {
		return this.requestRepository.findOne(id);
	}

	public Request findOneMode(final int id) {
		final Request request = this.findOne(id);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(request.getPack().getManager().equals(principal) || request.getCustomer().equals(principal));
		return request;
	}

	public void delete(final int id) {
		this.checkRequest();

		final Actor principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		final Request request = this.findOne(id);
		Assert.notNull(request);
		Assert.isTrue(request.getId() != 0);
		Assert.isTrue(request.getCustomer().equals(principal));
		this.requestRepository.delete(request);
	}

	public Request reconstruct(final Request requestF, final BindingResult binding) {
		final Request result = this.create(requestF.getPack());
		final Actor principal = this.actorService.findByPrincipal();
		Request orig = null;
		if (requestF.getId() != 0) {
			orig = this.findOne(requestF.getId());
			Assert.notNull(orig);
			Assert.isTrue(orig.getPack().getManager().equals(principal));
			Assert.isTrue(orig.getStatus().equals("PENDING"));
			Assert.isTrue(requestF.getCommentsManager() != null || requestF.getCommentsManager() != "");
			result.setId(requestF.getId());
			result.setCustomer(orig.getCustomer());
			result.setCommentsCustomer(orig.getCommentsCustomer());
			result.setCommentsManager(requestF.getCommentsManager());
		} else {
			result.setCommentsCustomer(requestF.getCommentsCustomer());
			result.setCustomer((Customer) principal);
		}
		//		if (orig.getCommentsCustomer() != null) {
		//			final String text = orig.getCommentsCustomer() + "//n";
		//			result.setCommentsCustomer(text + (requestF.getCommentsCustomer()));
		//		} else
		//		if (orig.getCommentsManager() != null) {
		//			final String text = orig.getCommentsManager() + "//n";
		//			result.setCommentsManager(text + (requestF.getCommentsManager()));
		//		} else
		//			result.setCommentsManager(requestF.getCommentsManager());
		this.validator.validate(result, binding);
		return result;
	}

	public Request save(final Request request) {
		this.checkRequest();

		final Actor principal = this.actorService.findByPrincipal();
		Request result = this.create(request.getPack());
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER") || this.actorService.checkAuthority(principal, "CUSTOMER"));
		if (request.getId() != 0) {
			result = this.findOne(request.getId());
			Assert.isTrue(result.getPack().getManager().equals(principal) || result.getCustomer().equals(principal));
			Assert.notNull(result);
			result.setStatus("APPROVED");
			Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
			result.setCommentsManager(request.getCommentsManager());
		} else {
			Assert.isTrue(this.actorService.checkAuthority(principal, "CUSTOMER"));
			result = request;
		}
		result = this.requestRepository.save(result);

		return result;
	}

	public Collection<Request> getList(final int id) {
		// TODO Auto-generated method stub
		this.checkRequest();
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
		Collection<Request> requests = new ArrayList<>();
		requests = this.requestRepository.findByPackage(id);
		return requests;
	}

	//	public void approved(final int id) {
	//		final Actor principal = this.actorService.findByPrincipal();
	//		Assert.notNull(principal);
	//		final Request request = this.findOne(id);
	//		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
	//		Assert.notNull(request);
	//		Assert.isTrue(request.getId() != 0);
	//		Assert.isTrue(request.getPack().getManager().equals(principal));
	//		request.setStatus("APPROVED");
	//		this.requestRepository.save(request);
	//	}

	public void rejected(final int id) {
		this.checkRequest();
		final Actor principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		final Request request = this.findOne(id);
		Assert.isTrue(this.actorService.checkAuthority(principal, "MANAGER"));
		Assert.notNull(request);
		Assert.isTrue(request.getId() != 0);
		Assert.isTrue(request.getPack().getManager().equals(principal));
		Assert.isTrue(request.getStatus().equals("PENDING"));
		request.setStatus("REJECTED");
		this.requestRepository.save(request);
	}

	public Request toEdit(final Request request) {
		final Request res = this.create(request.getPack());
		final Actor principal = this.actorService.findByPrincipal();
		res.setCustomer(request.getCustomer());
		res.setPack(request.getPack());
		res.setId(request.getId());
		if (this.actorService.checkAuthority(principal, "MANAGER")) {
			res.setCommentsManager("");
			res.setCommentsCustomer(request.getCommentsCustomer());
		}
		if (this.actorService.checkAuthority(principal, "CUSTOMER")) {
			res.setCommentsCustomer("");
			res.setCommentsManager(request.getCommentsManager());
		}
		return res;
	}

	public String[] comments(final Request request, final String tipo) {
		String[] comments = null;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		if (tipo == "MANAGER" && request.getCommentsManager() != null)
			comments = request.getCommentsManager().split("//n");
		if (tipo == "CUSTOMER" && request.getCommentsCustomer() != null)
			comments = request.getCommentsCustomer().split("//n");
		return comments;
	}

	public void checkRequest() {
		final Date now = new Date();
		final Collection<Request> requests = this.requestRepository.getOld(now);
		if (!requests.isEmpty())
			for (final Request r : requests) {
				r.setStatus("REJECTED");
				this.requestRepository.save(r);
			}
	}

	// request per manager
	public Integer MaxRequestPerManager() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.requestRepository.MaxRequestPerManager();
	}
	public Integer MinRequestPerManager() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.requestRepository.MinRequestPerManager();
	}

	public Double AvgRequestPerManager() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.requestRepository.AvgRequestPerManager();
	}
	public Double stdevRequestPerManager() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.requestRepository.StddevRequestPerManager();

	}
	// request pending per manager
	public Integer MaxRequestPerManagerPending() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.requestRepository.MaxRequestPerManagerPending();
	}
	public Integer MinRequestPerManagerPending() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.requestRepository.MinRequestPerManagerPending();
	}

	public Double AvgRequestPerManagerPending() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.requestRepository.AvgRequestPerManagerPending();
	}
	public Double stdevRequestPerManagerPending() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.requestRepository.StddevRequestPerManagerPending();

	}
	// request per customer
	public Integer MaxRequestPerCustomer() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.requestRepository.MaxRequestPerCustomer();
	}
	public Integer MinRequestPerCustomer() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.requestRepository.MinRequestPerCustomer();
	}

	public Double AvgRequestPerCustomer() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.requestRepository.AvgRequestPerCustomer();
	}
	public Double stdevRequestPerCustomer() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.requestRepository.StddevRequestPerCustomer();

	}
	// request pending per customer 
	public Integer MaxRequestPerCustomerPending() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.requestRepository.MaxRequestPerCustomerPending();
	}
	public Integer MinRequestPerCustomerPending() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.requestRepository.MinRequestPerCustomerPending();
	}

	public Double AvgRequestPerCustomerPending() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.requestRepository.AvgRequestPerCustomerPending();
	}
	public Double stdevRequestPerCustomerPending() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		return this.requestRepository.StddevRequestPerCustomerPending();

	}

	public void deleteAccountManager(final Package p) {
		final Collection<Request> req = this.requestRepository.findByPackage(p.getId());
		if (!req.isEmpty()) {
			for (final Request r : req) {
				r.setPack(null);
				r.setCustomer(null);
				this.requestRepository.save(r);
				this.contractService.deleteAccount(r);
			}
			this.requestRepository.deleteInBatch(req);
		}

	}

	public void deleteAccountCustomer() {
		final Actor principal = this.actorService.findByPrincipal();
		final Collection<Request> request = this.requestRepository.findAllCustomer(principal.getId());
		if (!request.isEmpty()) {
			for (final Request r : request) {
				r.setPack(null);
				r.setCustomer(null);
				this.requestRepository.save(r);
				this.contractService.deleteAccount(r);
			}
			this.requestRepository.deleteInBatch(request);
		}

	}

}
