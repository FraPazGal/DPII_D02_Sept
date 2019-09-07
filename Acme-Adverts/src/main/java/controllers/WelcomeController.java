/*
 * WelcomeController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.SystemConfigurationService;
import domain.Actor;
import domain.SystemConfiguration;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {

	@Autowired
	ActorService				aService;

	@Autowired
	SystemConfigurationService	cService;


	// Constructors -----------------------------------------------------------

	public WelcomeController() {
		super();
	}

	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/index")
	public ModelAndView index() {
		ModelAndView result;
		SimpleDateFormat formatter;
		String moment;
		String name;

		try {
			final UserAccount logged = LoginService.getPrincipal();
			if (logged == null)
				name = "";
			else {
				final Actor a = this.aService.findByPrincipal();
				name = " " + a.getName();
			}
		} catch (final Throwable oops) {
			name = "";
		}

		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		moment = formatter.format(new Date());

		result = new ModelAndView("welcome/index");
		final SystemConfiguration config = this.cService.findMySystemConfiguration();
		result.addObject("bannerURL", config.getBanner());
		result.addObject("welcomeMessage", config.getWelcomeMessage());
		result.addObject("namesystem", config.getSystemName());
		result.addObject("name", name);
		result.addObject("moment", moment);

		return result;
	}
}
