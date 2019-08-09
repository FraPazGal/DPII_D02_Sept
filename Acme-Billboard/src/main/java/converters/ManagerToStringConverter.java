
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Manager;

@Component
@Transactional
public class ManagerToStringConverter implements Converter<Manager, String> {

	@Override
	public String convert(final Manager entity) {
		String result;

		if (entity == null)
			result = null;
		else
			result = String.valueOf(entity.getId());
		return result;
	}

}
