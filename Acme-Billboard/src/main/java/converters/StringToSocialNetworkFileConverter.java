
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.SocialNetworkFileRepository;
import domain.SocialNetworkFile;

@Component
@Transactional
public class StringToSocialNetworkFileConverter implements Converter<String, SocialNetworkFile> {

	@Autowired
	SocialNetworkFileRepository	repository;


	@Override
	public SocialNetworkFile convert(final String text) {
		SocialNetworkFile result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.repository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}

}
