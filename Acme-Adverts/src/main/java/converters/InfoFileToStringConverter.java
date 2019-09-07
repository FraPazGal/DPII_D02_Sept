
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.InfoFile;

@Component
@Transactional
public class InfoFileToStringConverter implements Converter<InfoFile, String> {

	@Override
	public String convert(final InfoFile entity) {
		String result;

		if (entity == null)
			result = null;
		else
			result = String.valueOf(entity.getId());
		return result;
	}

}
