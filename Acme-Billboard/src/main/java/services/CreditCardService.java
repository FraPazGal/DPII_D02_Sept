
package services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class CreditCardService {

	public boolean checkIfExpired(final Integer expirationMonth, final Integer expirationYear) throws ParseException {

		boolean res = false;

		final String expiration = ((expirationMonth.toString().length() == 1) ? "0" + expirationMonth.toString() : expirationMonth.toString()) + ((expirationYear.toString().length() == 1) ? "0" + expirationYear.toString() : expirationYear.toString());

		final DateFormat date = new SimpleDateFormat("MMyy");

		final Date expiryDate = date.parse(expiration);
		final Date currentDate = new Date();

		if (currentDate.after(expiryDate))
			res = true;

		return res;
	}

	public boolean checkCreditCardNumber(final String number) {
		int sum = 0;
		boolean alternate = false;
		for (int i = number.length() - 1; i >= 0; i--) {
			int n = Integer.parseInt(number.substring(i, i + 1));
			if (alternate) {
				n *= 2;
				if (n > 9)
					n = (n % 10) + 1;
			}
			sum += n;
			alternate = !alternate;
		}
		return (sum % 10 == 0);
	}

}
