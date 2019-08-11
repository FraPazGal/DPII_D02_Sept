
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.TVFile;

@Component
@Transactional
public class TVFileToStringConverter implements Converter<TVFile, String> {

	@Override
	public String convert(final TVFile entity) {
		String result;

		if (entity == null)
			result = null;
		else
			result = String.valueOf(entity.getId());
		return result;
	}

}
