/*
 * CustomerController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CustomerService;
import services.SystemConfigurationService;
import domain.Customer;
import domain.SystemConfiguration;
import forms.EditionCustomerFormObject;
import forms.RegisterCustomerFormObject;

@Controller
@RequestMapping("/customer")
public class CustomerController extends AbstractController {

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private CustomerService				customerService;

	@Autowired
	private ActorService				actorService;


	// Constructors -----------------------------------------------------------

	public CustomerController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer id) {
		ModelAndView result;
		Customer customer;
		try {
			if (id != null)
				customer = this.customerService.findOneNot(id);
			else
				customer = (Customer) this.actorService.findByPrincipal();
			result = new ModelAndView("customer/display");
			result.addObject("customer", customer);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	//edit

	@RequestMapping(value = "/customer/edit", method = RequestMethod.GET)
	public ModelAndView editAuditor() {
		ModelAndView result;
		Customer customer;

		try {
			customer = (Customer) this.actorService.findByPrincipal();
			final EditionCustomerFormObject editionCustomerFormObject = new EditionCustomerFormObject(customer);
			result = new ModelAndView("customer/edit");
			result.addObject("editionCustomerFormObject", editionCustomerFormObject);
			final SystemConfiguration sys = this.systemConfigurationService.findMySystemConfiguration();
			result.addObject("makers", sys.getMakers().split(","));
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/customer/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final EditionCustomerFormObject editionCustomerFormObject, final BindingResult binding) {

		ModelAndView result;

		try {
			Customer customer;

			customer = this.customerService.reconstruct(editionCustomerFormObject, binding);

			if (binding.hasErrors()) {
				result = new ModelAndView("customer/edit");
				result.addObject("editionCustomerFormObject", editionCustomerFormObject);
				final SystemConfiguration sys = this.systemConfigurationService.findMySystemConfiguration();
				result.addObject("makers", sys.getMakers().split(","));
			} else {
				final Customer cutom = this.customerService.save(customer);
				result = new ModelAndView("customer/display");
				result.addObject("customer", cutom);

			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	//register
	@RequestMapping(value = "/customer/register", method = RequestMethod.GET)
	public ModelAndView registerNewCustomer() {
		ModelAndView result;

		try {
			final RegisterCustomerFormObject registerCustomerFormObject = new RegisterCustomerFormObject();
			result = new ModelAndView("customer/register");
			result.addObject("registerCustomerFormObject", registerCustomerFormObject);
			registerCustomerFormObject.setTermsAndConditions(false);
			final SystemConfiguration sys = this.systemConfigurationService.findMySystemConfiguration();
			result.addObject("makers", sys.getMakers().split(","));
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");

		}

		return result;
	}

	@RequestMapping(value = "/customer/register", method = RequestMethod.POST, params = "save")
	public ModelAndView register(@Valid final RegisterCustomerFormObject registerCustomerFormObject, final BindingResult binding) {

		ModelAndView result;

		Customer customer;
		try {

			customer = this.customerService.reconstruct(registerCustomerFormObject, binding);

			if (binding.hasErrors()) {
				result = new ModelAndView("customer/register");
				registerCustomerFormObject.setPassword(null);
				registerCustomerFormObject.setTermsAndConditions(false);
				result.addObject("registerFormObject", registerCustomerFormObject);
				final SystemConfiguration sys = this.systemConfigurationService.findMySystemConfiguration();
				result.addObject("makers", sys.getMakers().split(","));
			} else {
				this.customerService.save(customer);
				result = new ModelAndView("redirect:/");
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}
	// Delete ------------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final HttpSession session) {
		ModelAndView result;
		try {
			this.customerService.delete();
			session.invalidate();
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}
}
