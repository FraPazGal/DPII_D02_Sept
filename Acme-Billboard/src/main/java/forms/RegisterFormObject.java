
package forms;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class RegisterFormObject {

	/* Attributes */

	/* Actor attributes */
	private String	username;
	private String	password;
	private String	passConfirmation;
	private String	name;
	private String	surname;
	private String	middlename;
	private String	photo;
	private String	email;
	private String	phoneNumber;
	private String	address;

	/* Form attributes */
	private Boolean	termsAndConditions;


	public String getMiddlename() {
		return this.middlename;
	}

	public void setMiddlename(final String middlename) {
		this.middlename = middlename;
	}
	@Size(min = 5, max = 32)
	@Column(unique = true)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	@Size(min = 5, max = 32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	@Size(min = 5, max = 32)
	public String getPassConfirmation() {
		return this.passConfirmation;
	}

	public void setPassConfirmation(final String passConfirmation) {
		this.passConfirmation = passConfirmation;
	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@URL
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@NotBlank
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	@NotNull
	public Boolean getTermsAndConditions() {
		return this.termsAndConditions;
	}

	public void setTermsAndConditions(final Boolean termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}
}
