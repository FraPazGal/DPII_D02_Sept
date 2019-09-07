
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Contract;

@Component
@Transactional
public class ContractToStringConverter implements Converter<Contract, String> {

	@Override
	public String convert(final Contract entity) {
		String result;

		if (entity == null)
			result = null;
		else
			result = String.valueOf(entity.getId());
		return result;
	}

}
