
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.BillboardFile;

@Component
@Transactional
public class BillboardFileToStringConverter implements Converter<BillboardFile, String> {

	@Override
	public String convert(final BillboardFile entity) {
		String result;

		if (entity == null)
			result = null;
		else
			result = String.valueOf(entity.getId());
		return result;
	}

}
