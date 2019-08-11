
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Transactional
public class UtilityService {

	@Autowired
	private PackageService	packageService;


	public boolean checkVAT(final String vat) {
		final Pattern pattern = Pattern.compile("([a-zA-Z]{2}|[a-zA-Z]{3})(\\d[0-9]{7}|\\d[0-9]{9})");
		final Matcher matcher = pattern.matcher(vat);

		return (matcher.matches() ? true : false);
	}

	public boolean checkCC(final String cc) {
		final Pattern pattern = Pattern.compile("([+]\\d{3})");
		final Matcher matcher = pattern.matcher(cc);

		return (matcher.matches() ? true : false);
	}

	public String generateTicker(final int targetStringLength, final String type) {
		final int leftNumberLimit = 48; // letter '0'
		final int rightNumberLimit = 57; // letter '9'

		final int leftCharLimit = 65; // letter 'A'
		final int rightCharLimit = 90; // letter 'Z'

		final Random random = new Random();
		final StringBuilder buffer = new StringBuilder(targetStringLength);

		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = 0;
			if (type == "number")
				randomLimitedInt = leftNumberLimit + (int) (random.nextFloat() * (rightNumberLimit - leftNumberLimit + 1));
			else
				randomLimitedInt = leftCharLimit + (int) (random.nextFloat() * (rightCharLimit - leftCharLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		final String generatedString = buffer.toString();

		return generatedString;
	}

	public String getTicker() {
		final String ticker = this.generateTicker(2, "number") + this.generateTicker(3, "letter") + this.generateTicker(2, "number") + "-" + this.generateTicker(5, "letter");
		Assert.isNull(this.packageService.getNumberOfTickers(ticker));
		final Pattern pattern = Pattern.compile("^\\d{2}[A-Z]{3}\\d{2}-[A-Z]{5}$");
		final Matcher matcher = pattern.matcher(ticker);
		Assert.isTrue((matcher.matches() ? true : false));
		return ticker;
	}

	public String getHash(final String text) {
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(text, null);
		// TODO Auto-generated method stub
		return hash;
	}
	
	public Collection<String> typeOfFile() {
		Collection<String> result = new ArrayList<>();
		result.add("Billboard");
		result.add("TV");
		result.add("Radio");
		result.add("SocialNetwork");
		result.add("Info");
		
		return result;
	}

}
