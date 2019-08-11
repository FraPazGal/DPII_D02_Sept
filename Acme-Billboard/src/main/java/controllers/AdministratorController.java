/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

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
import services.AdministratorService;
import services.BillboardFileService;
import services.FinderService;
import services.InfoFileService;
import services.ManagerService;
import services.RadioFileService;
import services.RequestService;
import services.SocialNetworkFileService;
import services.TVFileService;
import domain.Administrator;
import domain.Manager;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private RequestService			requestService;

	@Autowired
	private FinderService			finderService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ActorService			actorService;
	
	@Autowired
	private BillboardFileService	billboardFileService;
	
	@Autowired
	private InfoFileService			infoFileService;
	
	@Autowired
	private RadioFileService		radioFileService;
	
	@Autowired
	private TVFileService		TVFileService;
	
	@Autowired
	private SocialNetworkFileService	socialNetworkFileService;


	// Constructors -----------------------------------------------------------

	public AdministratorController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer id) {
		ModelAndView result;
		Administrator administrator;
		try {
			if (id != null)
				administrator = this.administratorService.findOneNot(id);
			else
				administrator = (Administrator) this.actorService.findByPrincipal();
			result = new ModelAndView("administrator/display");
			result.addObject("administrator", administrator);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	//edit

	@RequestMapping(value = "/administrator/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Administrator administrator;
		try {
			administrator = (Administrator) this.actorService.findByPrincipal();
			result = new ModelAndView("administrator/edit");
			final EditionFormObject editionFormObject = new EditionFormObject(administrator);
			result.addObject("editionFormObject", editionFormObject);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/administrator/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final EditionFormObject editionFormObject, final BindingResult binding) {

		ModelAndView result;

		try {
			Administrator administrator;

			administrator = this.administratorService.reconstruct(editionFormObject, binding);

			if (binding.hasErrors()) {
				result = new ModelAndView("administrator/edit");
				result.addObject("editionFormObject", editionFormObject);
			} else {
				final Administrator admin = this.administratorService.save(administrator);
				result = new ModelAndView("administrator/display");
				result.addObject("administrator", admin);
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	//register
	@RequestMapping(value = "/administrator/register", method = RequestMethod.GET)
	public ModelAndView registerNew() {
		ModelAndView result;

		try {
			final RegisterFormObject registerFormObject = new RegisterFormObject();
			result = new ModelAndView("administrator/register");
			result.addObject("registerFormObject", registerFormObject);
			registerFormObject.setTermsAndConditions(false);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");

		}

		return result;
	}

	@RequestMapping(value = "/administrator/register", method = RequestMethod.POST, params = "save")
	public ModelAndView register(@Valid final RegisterFormObject registerFormObject, final BindingResult binding) {

		ModelAndView result;

		Administrator administrator;
		try {

			administrator = this.administratorService.reconstruct(registerFormObject, binding);

			if (binding.hasErrors()) {
				result = new ModelAndView("administrator/register");
				registerFormObject.setPassword(null);
				registerFormObject.setTermsAndConditions(false);
				result.addObject("registerFormObject", registerFormObject);
			} else {
				this.administratorService.save(administrator);
				result = new ModelAndView("redirect:/");
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}

	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public ModelAndView statistics() {
		ModelAndView result;
		try {
			result = new ModelAndView("administrator/statistics");
			result.addObject("requestURI", "administrator/statistics.do");

			// Files per contract
			Double [] statsBillboardFilesPerContract = this.billboardFileService.StatsBillboardFilesPerContract();
			Double [] statsRadioFilesPerContract = this.radioFileService.StatsRadioFilesPerContract();
			Double [] statsSNFilesPerContract = this.socialNetworkFileService.StatsSocialNetworkFilesPerContract();
			Double [] statsInfoFilesPerContract = this.infoFileService.StatsInfoFilesPerContract();
			Double [] statsTVFilesPerContract = this.TVFileService.StatsTVFilesPerContract();
			Double [] statsFilesPerContract = this.billboardFileService.StatsFilesPerContract();
			result.addObject("statsBillboardFilesPerContract", statsBillboardFilesPerContract);
			result.addObject("statsRadioFilesPerContract", statsRadioFilesPerContract);
			result.addObject("statsSNFilesPerContract", statsSNFilesPerContract);
			result.addObject("statsInfoFilesPerContract", statsInfoFilesPerContract);
			result.addObject("statsTVFilesPerContract", statsTVFilesPerContract);
			result.addObject("statsFilesPerContract", statsFilesPerContract);
			
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
			this.administratorService.delete();
			session.invalidate();
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}
}
