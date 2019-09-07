
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

@Embeddable
@Access(AccessType.PROPERTY)
public class CreditCard {

	// Atributes

	private String	holder;
	private String	make;
	private String	number;
	private Integer	expirationMonth;
	private Integer	expirationYear;
	private Integer	CVV;


	// Getters and Setters

	@NotBlank
	public String getHolder() {
		return this.holder;
	}

	public void setHolder(final String holder) {
		this.holder = holder;
	}

	@NotBlank
	public String getMake() {
		return this.make;
	}

	public void setMake(final String make) {
		this.make = make;
	}

	@NotBlank
	public String getNumber() {
		return this.number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	@NotNull
	@Range(min = 1, max = 12)
	public Integer getExpirationMonth() {
		return this.expirationMonth;
	}

	public void setExpirationMonth(final Integer expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	@NotNull
	@Range(min = 0, max = 99)
	public Integer getExpirationYear() {
		return this.expirationYear;
	}

	public void setExpirationYear(final Integer expirationYear) {
		this.expirationYear = expirationYear;
	}

	@NotNull
	@Range(min = 0, max = 999)
	public Integer getCVV() {
		return this.CVV;
	}

	public void setCVV(final Integer cVV) {
		this.CVV = cVV;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + this.CVV;
		result = prime * result + ((this.expirationMonth == null) ? 0 : this.expirationMonth.hashCode());
		result = prime * result + ((this.expirationYear == null) ? 0 : this.expirationYear.hashCode());
		result = prime * result + ((this.holder == null) ? 0 : this.holder.hashCode());
		result = prime * result + ((this.make == null) ? 0 : this.make.hashCode());
		result = prime * result + ((this.number == null) ? 0 : this.number.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final CreditCard other = (CreditCard) obj;
		if (this.CVV != other.CVV)
			return false;
		if (this.expirationMonth == null) {
			if (other.expirationMonth != null)
				return false;
		} else if (!this.expirationMonth.equals(other.expirationMonth))
			return false;
		if (this.expirationYear == null) {
			if (other.expirationYear != null)
				return false;
		} else if (!this.expirationYear.equals(other.expirationYear))
			return false;
		if (this.holder == null) {
			if (other.holder != null)
				return false;
		} else if (!this.holder.equals(other.holder))
			return false;
		if (this.make == null) {
			if (other.make != null)
				return false;
		} else if (!this.make.equals(other.make))
			return false;
		if (this.number == null) {
			if (other.number != null)
				return false;
		} else if (!this.number.equals(other.number))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CreditCard [holder=" + this.holder + ", make=" + this.make + ", number=" + this.number + ", expirationMonth=" + this.expirationMonth + ", expirationYear=" + this.expirationYear + ", CVV=" + this.CVV + "]";
	}

}
