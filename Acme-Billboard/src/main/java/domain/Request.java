
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Request extends DomainEntity {

	private String			status;
	private String			commentsCustomer;
	private String			commentsManager;
	private Customer		customer;
	private domain.Package	pack;


	@Valid
	@ManyToOne(optional = true)
	public domain.Package getPack() {
		return this.pack;
	}

	public void setPack(final domain.Package pack) {
		this.pack = pack;
	}

	@NotBlank
	@Pattern(regexp = "\\b(APPROVED|REJECTED|PENDING)\\b")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	@NotBlank
	public String getCommentsCustomer() {
		return this.commentsCustomer;
	}

	public void setCommentsCustomer(final String commentsCustomer) {
		this.commentsCustomer = commentsCustomer;
	}

	public String getCommentsManager() {
		return this.commentsManager;
	}

	public void setCommentsManager(final String commentsManager) {
		this.commentsManager = commentsManager;
	}

	@Valid
	@ManyToOne(optional = true)
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(final Customer customer) {
		this.customer = customer;
	}

}
