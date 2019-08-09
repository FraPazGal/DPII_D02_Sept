
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Request;

@Component
@Transactional
public class RequestToStringConverter implements Converter<Request, String> {

	@Override
	public String convert(final Request entity) {
		String result;

		if (entity == null)
			result = null;
		else
			result = String.valueOf(entity.getId());
		return result;
	}

}
