
package services;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SystemConfigurationRepository;
import domain.SystemConfiguration;

@Transactional
@Service
public class SystemConfigurationService {

	@Autowired
	private SystemConfigurationRepository	systemConfigurationRepository;

	@Autowired
	private ActorService					actorService;

	@Autowired
	private Validator						validator;


	public SystemConfiguration findMySystemConfiguration() {
		return this.systemConfigurationRepository.findSystemConf();
	}
	public SystemConfiguration save(final SystemConfiguration systemConfiguration) {

		Assert.isTrue(this.actorService.checkAuthority(this.actorService.findByPrincipal(), "ADMIN"), "not.allowed");

		return this.systemConfigurationRepository.save(systemConfiguration);
	}

	public SystemConfiguration reconstruct(final SystemConfiguration systemConfiguration, final String welcomeES, final String welcomeEN, final String bES, final String bEN, final BindingResult binding) {

		try {
			Assert.isTrue(!welcomeEN.isEmpty());
			Assert.isTrue(!welcomeES.isEmpty());

		} catch (final Throwable oops) {
			binding.rejectValue("welcomeMessage", "no.both.welcome");
		}
		systemConfiguration.setWelcomeMessage(this.reconstruct(welcomeES, welcomeEN));

		if (bEN != "" || bES != "")
			try {
				Assert.isTrue(!bEN.isEmpty());
				Assert.isTrue(!bES.isEmpty());

			} catch (final Throwable oops) {
				binding.rejectValue("breachNotification", "no.both.breach");
			}

		this.validator.validate(systemConfiguration, binding);

		return systemConfiguration;
	}

	public Map<String, String> reconstruct(final String stringES, final String stringEN) {
		final Map<String, String> res = new HashMap<>();

		res.put("SP", stringES);
		res.put("EN", stringEN);

		return res;
	}

	/* Find banner */
	public String findMyBanner() {

		String result;

		result = this.findMySystemConfiguration().getBanner();

		return result;
	}

}
