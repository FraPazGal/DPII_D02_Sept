
package forms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import domain.Manager;

public class PackageForm {

	private int		id;
	private int		version;
	private String	ticker;
	private String	title;
	private String	description;
	private Date	startDate;
	private Date	endDate;
	private String	photo;
	private Double	price;
	private Boolean	finalMode;
	private Manager	manager;


	public PackageForm() {

	}

	public PackageForm(final domain.Package pack) {
		this.id = pack.getId();
		this.version = pack.getVersion();
		this.ticker = pack.getTicker();
		this.title = pack.getTitle();
		this.description = pack.getDescription();
		this.startDate = pack.getStartDate();
		this.endDate = pack.getEndDate();
		this.photo = pack.getPhoto();
		this.price = pack.getPrice();
		this.finalMode = pack.getFinalMode();
		this.manager = pack.getManager();

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

	@NotBlank
	@Column(unique = true)
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	@URL
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@NotNull
	@Range(min = 0, max = 1000000000)
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(final Double price) {
		this.price = price;
	}

	@NotNull
	public Boolean getFinalMode() {
		return this.finalMode;
	}

	public void setFinalMode(final Boolean finalMode) {
		this.finalMode = finalMode;
	}

	@Valid
	@ManyToOne(optional = false)
	public Manager getManager() {
		return this.manager;
	}

	public void setManager(final Manager manager) {
		this.manager = manager;
	}

}
