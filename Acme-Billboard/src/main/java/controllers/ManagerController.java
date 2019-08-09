
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
import services.ManagerService;
import domain.Manager;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@Controller
@RequestMapping("/manager")
public class ManagerController extends AbstractController {

	@Autowired
	private ManagerService	managerService;

	@Autowired
	private ActorService	actorService;


	// Constructors -----------------------------------------------------------

	public ManagerController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer id) {
		ModelAndView result;
		Manager manager;
		try {
			if (id != null)
				manager = this.managerService.findOneNot(id);
			else
				manager = (Manager) this.actorService.findByPrincipal();
			result = new ModelAndView("manager/display");
			result.addObject("manager", manager);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	//edit

	@RequestMapping(value = "/manager/edit", method = RequestMethod.GET)
	public ModelAndView editAuditor() {
		ModelAndView result;
		Manager manager;
		try {
			manager = (Manager) this.actorService.findByPrincipal();
			final EditionFormObject editionFormObject = new EditionFormObject(manager);
			result = new ModelAndView("manager/edit");
			result.addObject("editionFormObject", editionFormObject);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/manager/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final EditionFormObject editionFormObject, final BindingResult binding) {

		ModelAndView result;

		try {
			Manager manager;

			manager = this.managerService.reconstruct(editionFormObject, binding);

			if (binding.hasErrors()) {
				result = new ModelAndView("manager/edit");
				result.addObject("editionFormObject", editionFormObject);
			} else {
				final Manager manag = this.managerService.save(manager);
				result = new ModelAndView("manager/display");
				result.addObject("manager", manag);
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	//register
	@RequestMapping(value = "/administrator/register", method = RequestMethod.GET)
	public ModelAndView registerNewAuditor() {
		ModelAndView result;

		try {
			final RegisterFormObject registerFormObject = new RegisterFormObject();
			result = new ModelAndView("manager/register");
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

		Manager manager;
		try {

			manager = this.managerService.reconstruct(registerFormObject, binding);

			if (binding.hasErrors()) {
				result = new ModelAndView("manager/register");
				registerFormObject.setPassword(null);
				registerFormObject.setTermsAndConditions(false);
				result.addObject("registerFormObject", registerFormObject);
			} else {
				this.managerService.save(manager);
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
			this.managerService.delete();
			session.invalidate();
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}
}
