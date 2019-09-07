
package domain;

import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class SystemConfiguration extends DomainEntity {

	/* Attributes */

	private String				systemName;
	private Map<String, String>	welcomeMessage;
	private String				banner;
	private String				countryCode;
	private Integer				timeResultsCached;
	private Integer				maxResults;
	private String				makers;
	private Map<String, String>	breachNotification;


	/* Getters&Setters */

	@ElementCollection(fetch = FetchType.EAGER)
	public Map<String, String> getBreachNotification() {
		return this.breachNotification;
	}

	public void setBreachNotification(final Map<String, String> breachNotification) {
		this.breachNotification = breachNotification;
	}

	@NotBlank
	public String getSystemName() {
		return this.systemName;
	}

	public void setSystemName(final String systemName) {
		this.systemName = systemName;
	}

	@NotNull
	@NotEmpty
	@ElementCollection
	public Map<String, String> getWelcomeMessage() {
		return this.welcomeMessage;
	}

	public void setWelcomeMessage(final Map<String, String> welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	@URL
	@NotBlank
	public String getBanner() {
		return this.banner;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	@NotBlank
	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(final String countryCode) {
		this.countryCode = countryCode;
	}

	@NotNull
	@Range(min = 1, max = 24)
	public Integer getTimeResultsCached() {
		return this.timeResultsCached;
	}

	public void setTimeResultsCached(final Integer timeResultsCached) {
		this.timeResultsCached = timeResultsCached;
	}

	@NotNull
	@Range(min = 0, max = 100)
	public Integer getMaxResults() {
		return this.maxResults;
	}

	public void setMaxResults(final Integer maxResults) {
		this.maxResults = maxResults;
	}

	@NotBlank
	public String getMakers() {
		return this.makers;
	}

	public void setMakers(final String makers) {
		this.makers = makers;
	}
}
