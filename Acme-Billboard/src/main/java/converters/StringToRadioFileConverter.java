
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.RadioFileRepository;
import domain.RadioFile;

@Component
@Transactional
public class StringToRadioFileConverter implements Converter<String, RadioFile> {

	@Autowired
	RadioFileRepository	repository;


	@Override
	public RadioFile convert(final String text) {
		RadioFile result;

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
