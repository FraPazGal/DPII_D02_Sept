
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.InfoFileRepository;
import domain.InfoFile;

@Component
@Transactional
public class StringToInfoFileConverter implements Converter<String, InfoFile> {

	@Autowired
	InfoFileRepository	repository;


	@Override
	public InfoFile convert(final String text) {
		InfoFile result;

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
