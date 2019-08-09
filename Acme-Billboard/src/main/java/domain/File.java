
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class File extends DomainEntity {

	private String		location;
	private String		image;
	private Contract	contract;


	@NotBlank
	public String getLocation() {
		return this.location;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

	@NotBlank
	@URL
	public String getImage() {
		return this.image;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	@Valid
	@NotNull
	@ManyToOne
	public Contract getContract() {
		return this.contract;
	}

	public void setContract(final Contract contract) {
		this.contract = contract;
	}

}
