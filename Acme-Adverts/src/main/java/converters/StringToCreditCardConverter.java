
package converters;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.CreditCard;

@Component
@Transactional
public class StringToCreditCardConverter implements Converter<String, CreditCard> {

	@Override
	public CreditCard convert(final String text) {
		CreditCard result;
		String parts[];

		if (text == null)
			result = null;
		else
			try {
				parts = text.split("\\|");
				result = new CreditCard();
				// HolderName
				result.setHolder(this.decodeUTF8(parts[0]));
				// BrandName
				result.setMake(this.decodeUTF8(parts[1]));
				// Number
				result.setNumber(this.decodeUTF8(parts[2]));
				// ExpirationMonth
				result.setExpirationMonth(Integer.valueOf(this.decodeUTF8(parts[3])));
				// ExpirationYear
				result.setExpirationYear(Integer.valueOf(this.decodeUTF8(parts[4])));
				// CodeCVV
				result.setCVV(Integer.valueOf(this.decodeUTF8(parts[5])));
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}
		return result;
	}
	private String decodeUTF8(final String text) throws UnsupportedEncodingException {
		return URLDecoder.decode(text, "UTF-8");
	}
}
