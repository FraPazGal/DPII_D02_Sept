
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Contract extends DomainEntity {

	private Date	signedCustomer;
	private Date	signedManager;
	private String	text;
	private String	hash;
	private Request	request;


	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getSignedCustomer() {
		return this.signedCustomer;
	}

	public void setSignedCustomer(final Date signedCustomer) {
		this.signedCustomer = signedCustomer;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getSignedManager() {
		return this.signedManager;
	}

	public void setSignedManager(final Date signedManager) {
		this.signedManager = signedManager;
	}

	@NotBlank
	@Column(columnDefinition = "TEXT")
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public String getHash() {
		return this.hash;
	}

	public void setHash(final String hash) {
		this.hash = hash;
	}

	@Valid
	@OneToOne(optional = true)
	public Request getRequest() {
		return this.request;
	}

	public void setRequest(final Request request) {
		this.request = request;
	}

}
