
package controllers;

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
import domain.SystemConfiguration;

@Controller
@RequestMapping("/systemConfiguration")
public class SystemConfigurationController extends AbstractController {

	@Autowired
	private SystemConfigurationService	sysconfigService;

	@Autowired
	private ActorService				actorService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView result = new ModelAndView("systemConfiguration/display");

		try {
			Assert.isTrue(this.actorService.checkAuthority(this.actorService.findByPrincipal(), "ADMIN"), "not.allowed");

			final SystemConfiguration config = this.sysconfigService.findMySystemConfiguration();

			result.addObject("config", config);
			result.addObject("welcome", config.getWelcomeMessage());
			result.addObject("breach", config.getBreachNotification());

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView result = new ModelAndView("systemConfiguration/edit");

		try {
			Assert.isTrue(this.actorService.checkAuthority(this.actorService.findByPrincipal(), "ADMIN"), "not.allowed");

			final SystemConfiguration config = this.sysconfigService.findMySystemConfiguration();

			result.addObject("systemConfiguration", config);
			result.addObject("welcome", config.getWelcomeMessage());
			result.addObject("breach", config.getBreachNotification());

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final SystemConfiguration config, @RequestParam(value = "welcomeES") final String welcomeES, @RequestParam(value = "welcomeEN") final String welcomeEN, @RequestParam(value = "breachES") final String breachES, @RequestParam(
		value = "breachEN") final String breachEN, final BindingResult binding) {

		ModelAndView result = new ModelAndView("systemConfiguration/edit");

		try {
			final SystemConfiguration reconstructed = this.sysconfigService.reconstruct(config, welcomeES, welcomeEN, breachES, breachEN, binding);

			if (binding.hasErrors()) {

				result.addObject("systemConfiguration", config);
				result.addObject("welcome", reconstructed.getWelcomeMessage());
				result.addObject("breach", reconstructed.getBreachNotification());

			} else {
				this.sysconfigService.save(reconstructed);

				result = new ModelAndView("redirect:display.do");
			}
		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

}
