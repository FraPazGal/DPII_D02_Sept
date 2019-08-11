
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.BillboardFileRepository;
import domain.BillboardFile;

@Component
@Transactional
public class StringToBillboardFileConverter implements Converter<String, BillboardFile> {

	@Autowired
	BillboardFileRepository	repository;


	@Override
	public BillboardFile convert(final String text) {
		BillboardFile result;

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
