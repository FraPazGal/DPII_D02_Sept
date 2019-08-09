
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.PackageService;
import forms.PackageForm;

@Controller
@RequestMapping("/package")
public class PackageController extends AbstractController {

	// Services

	@Autowired
	private PackageService	packageService;
	@Autowired
	private ActorService	actorService;


	@RequestMapping(value = "/manager/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<domain.Package> packages;
		try {
			packages = this.packageService.getListLoged();
			result = new ModelAndView("package/list");
			result.addObject("packages", packages);
			result.addObject("requestURI", "package/manager/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView listAll() {
		ModelAndView result;
		Collection<domain.Package> packages;
		try {
			packages = this.packageService.getListAll();
			result = new ModelAndView("package/list");
			result.addObject("packages", packages);
			result.addObject("requestURI", "package/listAll.do");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	// Create
	// ----------------------------------------------------------------------

	@RequestMapping(value = "/manager/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		PackageForm packageD;
		try {
			packageD = this.packageService.createF();
			result = new ModelAndView("package/edit");
			result.addObject("packageForm", packageD);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	// Display ------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int Id) {
		ModelAndView result;
		domain.Package packageD;
		try {
			packageD = this.packageService.findOneMode(Id);
			result = new ModelAndView("package/display");
			result.addObject("pack", packageD);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}
	// Save ------------------------------------------------------------

	@RequestMapping(value = "/manager/edit", method = RequestMethod.GET)
	public ModelAndView editt(@RequestParam final int Id) {
		ModelAndView result;
		domain.Package packageD;
		try {
			packageD = this.packageService.findOneMode(Id);
			Assert.isTrue(this.actorService.findByPrincipal().equals(packageD.getManager()));
			result = new ModelAndView("package/edit");
			final PackageForm packageF = new PackageForm(packageD);
			result.addObject("packageForm", packageF);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/package/manager/list.do");
			result.addObject("messageCode", "position.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/manager/edit", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView saveFinal(final PackageForm packageF, final BindingResult binding) {
		ModelAndView result;
		domain.Package packageD;
		try {

			packageD = this.packageService.reconstruct(packageF, binding);

			if (binding.hasErrors()) {
				result = new ModelAndView("package/edit");
				result.addObject("packageForm", packageF);
			} else {
				packageD.setFinalMode(true);
				this.packageService.save(packageD);
				result = new ModelAndView("redirect:/package/manager/list.do");
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}

	@RequestMapping(value = "/manager/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveDraft(final PackageForm packageF, final BindingResult binding) {
		ModelAndView result;
		domain.Package packageD;
		try {

			packageD = this.packageService.reconstruct(packageF, binding);

			if (binding.hasErrors()) {
				result = new ModelAndView("package/edit");
				result.addObject("packageForm", packageF);
			} else {
				packageD.setFinalMode(false);
				this.packageService.save(packageD);
				result = new ModelAndView("redirect:/package/manager/list.do");
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}

	// Delete ------------------------------------------------------------

	@RequestMapping(value = "/manager/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int Id) {
		ModelAndView result;
		try {
			this.packageService.delete(Id);
			result = new ModelAndView("redirect:/package/manager/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

}
