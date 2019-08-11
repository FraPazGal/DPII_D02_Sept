
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.SocialNetworkFile;

@Component
@Transactional
public class SocialNetworkFileToStringConverter implements Converter<SocialNetworkFile, String> {

	@Override
	public String convert(final SocialNetworkFile entity) {
		String result;

		if (entity == null)
			result = null;
		else
			result = String.valueOf(entity.getId());
		return result;
	}

}
