
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.TVFileRepository;
import domain.TVFile;

@Component
@Transactional
public class StringToTVFileConverter implements Converter<String, TVFile> {

	@Autowired
	TVFileRepository	repository;


	@Override
	public TVFile convert(final String text) {
		TVFile result;

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
