
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
public class File extends DomainEntity {

	private String		fileType;
	private Contract	contract;


	@NotBlank
	@Pattern(regexp = "^BILLBOARD|INFO|RADIO|TV|SOCIALNETWORK$")
	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(final String fileType) {
		this.fileType = fileType;
	}

	@Valid
	@ManyToOne
	public Contract getContract() {
		return this.contract;
	}

	public void setContract(final Contract contract) {
		this.contract = contract;
	}

}
