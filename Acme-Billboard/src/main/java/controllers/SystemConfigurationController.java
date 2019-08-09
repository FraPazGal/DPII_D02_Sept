
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SystemConfigurationService;
import domain.Actor;
import domain.SystemConfiguration;

@Controller
@RequestMapping("/systemConfiguration")
public class SystemConfigurationController extends AbstractController {

	// Services

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private ActorService				actorService;


	// Constructor

	public SystemConfigurationController() {
		super();
	}

	// Actions

	/* Display */

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView res;
		Actor principal;
		SystemConfiguration sysConfig;
		Map<String, String> welcomeMessage;
		Map<String, String> breachNotification;
		final Boolean err;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));

			sysConfig = this.systemConfigurationService.findMySystemConfiguration();

			welcomeMessage = sysConfig.getWelcomeMessage();
			breachNotification = sysConfig.getBreachNotification();

			res = new ModelAndView("systemConfiguration/display");
			res.addObject("sysConfig", sysConfig);
			res.addObject("welcomeMessage", welcomeMessage);
			res.addObject("breachNotification", breachNotification);

		} catch (final Throwable oopsie) {
			res = new ModelAndView("redirect:/");
			res.addObject("messageCode", "commit.error");
		}

		return res;
	}

	// Editing sysConfig

	@RequestMapping(value = "/edit", method = RequestMethod.GET, params = "systemconfigurationID")
	public ModelAndView edit() {
		ModelAndView res;
		SystemConfiguration systemConfiguration;
		final Boolean err;

		try {
			systemConfiguration = this.systemConfigurationService.findMySystemConfiguration();
			Assert.isTrue(this.actorService.checkAuthority(this.actorService.findByPrincipal(), "ADMIN"));
			Assert.notNull(systemConfiguration);
			res = new ModelAndView("systemConfiguration/edit");
			res.addObject("sysConfig", systemConfiguration);
			res.addObject("welcomeMessage", systemConfiguration.getWelcomeMessage());
			res.addObject("breachNotification", systemConfiguration.getBreachNotification());
		} catch (final Throwable oopsie) {
			res = new ModelAndView("redirect:/");
			res.addObject("messageCode", "commit.error");
		}
		return res;
	}

	// Save

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final SystemConfiguration systemConfiguration, @RequestParam("nameES") final String nameES, @RequestParam("nameEN") final String nameEN, @RequestParam("nEs") final String nEs, @RequestParam("nEn") final String nEn,
		final BindingResult binding) {
		ModelAndView res;
		Collection<String> errMessages = new ArrayList<>();
		SystemConfiguration sysConfig;
		Map<SystemConfiguration, Collection<String>> wA = new HashMap<>();
		try {

			wA = this.systemConfigurationService.reconstructWA(systemConfiguration, nameES, nameEN, nEs, nEn, binding);

			sysConfig = wA.keySet().iterator().next();
			errMessages = wA.get(sysConfig);

			if (binding.hasErrors()) {
				final SystemConfiguration sys = this.systemConfigurationService.findMySystemConfiguration();
				res = new ModelAndView("systemConfiguration/edit");
				res.addObject("welcomeMessage", sys.getWelcomeMessage());
				res.addObject("breachNotification", sys.getBreachNotification());
				res.addObject("sysConfig", sys);
				res.addObject("errMessages", errMessages);
			} else

				this.systemConfigurationService.save(sysConfig);

			res = new ModelAndView("redirect:display.do");
		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:/");
			res.addObject("messageCode", "commit.error");
		}
		return res;
	}

}
