
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.RadioFile;

@Component
@Transactional
public class RadioFileToStringConverter implements Converter<RadioFile, String> {

	@Override
	public String convert(final RadioFile entity) {
		String result;

		if (entity == null)
			result = null;
		else
			result = String.valueOf(entity.getId());
		return result;
	}

}
