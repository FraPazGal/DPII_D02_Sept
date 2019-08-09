
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.File;

@Component
@Transactional
public class FileToStringConverter implements Converter<File, String> {

	@Override
	public String convert(final File entity) {
		String result;

		if (entity == null)
			result = null;
		else
			result = String.valueOf(entity.getId());
		return result;
	}

}
