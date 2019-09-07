
package forms;

import javax.persistence.Column;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import domain.Actor;

public class EditionFormObject {

	/* Attributes */

	/* Actor attributes */
	private String	username;
	private String	password;

	private int		id;
	private int		version;
	private String	name;
	private String	surname;
	private String	middlename;
	private String	photo;
	private String	email;
	private String	phoneNumber;
	private String	address;


	public EditionFormObject() {

	}

	public EditionFormObject(final Actor actor) {
		this.id = actor.getId();
		this.version = actor.getVersion();
		this.username = actor.getUserAccount().getUsername();
		this.password = actor.getUserAccount().getPassword();
		this.name = actor.getName();
		this.surname = actor.getSurname();
		this.photo = actor.getPhoto();
		this.email = actor.getEmail();
		this.phoneNumber = actor.getPhoneNumber();
		this.address = actor.getAddress();

	}

	public String getMiddlename() {
		return this.middlename;
	}

	public void setMiddlename(final String middlename) {
		this.middlename = middlename;
	}
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
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

}
