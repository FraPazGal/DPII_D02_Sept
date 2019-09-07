
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class RadioFile extends File {

	private String		broadcasterName;
	private String		schedule;
	private String		sound;

	
	@NotBlank
	public String getBroadcasterName() {
		return this.broadcasterName;
	}

	public void setBroadcasterName(final String broadcasterName) {
		this.broadcasterName = broadcasterName;
	}

	@NotBlank
	public String getSchedule() {
		return this.schedule;
	}

	public void setSchedule(final String schedule) {
		this.schedule = schedule;
	}
	
	@NotBlank
	@URL
	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

}
