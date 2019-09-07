
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.FinderService;
import services.PackageService;
import domain.Actor;
import domain.Finder;

@Controller
@RequestMapping("/finder")
public class FinderController extends AbstractController {

	// Services
	@Autowired
	private PackageService	packageService;

	@Autowired
	private FinderService	finderService;

	@Autowired
	private ActorService	actorService;


	// Constructors

	public FinderController() {
		super();
	}
	// /list

	@RequestMapping(value = "/customer/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		try {
			final Finder finder = this.finderService.finderByCustomer();
			result = new ModelAndView("finder/search");
			result.addObject("finder", finder);
			result.addObject("packages", finder.getPackages());
			result.addObject("requestUri", "finder/customer/list.do");
			if (this.actorService.findByPrincipalIfLogged()) {
				final Actor actor = this.actorService.findByPrincipal();
				result.addObject("actor", actor);
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}
	// DELETE
	@RequestMapping(value = "/customer/search", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Finder finder, final BindingResult binding) {
		ModelAndView result;
		try {
			final Finder find = this.finderService.reconstruct(finder, binding);
			this.finderService.delete(find);
			result = new ModelAndView("redirect:search.do");
			result.addObject("requestUri", "finder/customer/list.do");
			if (this.actorService.findByPrincipalIfLogged()) {
				final Actor actor = this.actorService.findByPrincipal();
				result.addObject("actor", actor);
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}

		return result;
	}
	// search
	@RequestMapping(value = "/customer/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView result;
		try {
			final Finder finder = this.finderService.finderByCustomer();
			result = new ModelAndView("finder/search");
			result.addObject("finder", finder);
			result.addObject("packages", finder.getPackages());
			result.addObject("requestUri", "finder/customer/search.do");
			if (this.actorService.findByPrincipalIfLogged()) {
				final Actor actor = this.actorService.findByPrincipal();
				result.addObject("actor", actor);
			}
		} catch (final Throwable oopsie) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	// searchAnon
	@RequestMapping(value = "/anon/search", method = RequestMethod.GET)
	public ModelAndView searchAnon(@RequestParam(required = false) final String keyWord) {
		ModelAndView result;
		Collection<domain.Package> packages = new ArrayList<>();
		try {
			result = new ModelAndView("finder/anon/search");
			packages = this.finderService.searchAnon(keyWord);
			result.addObject("packages", packages);
			result.addObject("requestUri", "finder/anon/search.do");
			result.addObject("anon", true);
			if (this.actorService.findByPrincipalIfLogged()) {
				final Actor actor = this.actorService.findByPrincipal();
				result.addObject("actor", actor);
			}
		} catch (final Throwable oopsie) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/customer/search", method = RequestMethod.POST, params = "save")
	public ModelAndView search(final Finder finder, final BindingResult binding) {
		ModelAndView result;
		try {
			final Finder find = this.finderService.reconstruct(finder, binding);
			if (binding.hasErrors()) {
				final List<ObjectError> errors = binding.getAllErrors();
				for (final ObjectError e : errors)
					System.out.println(e.toString());
				result = new ModelAndView("finder/search");
				result.addObject("finder", finder);
				result.addObject("requestUri", "finder/customer/list.do");
				if (this.actorService.findByPrincipalIfLogged()) {
					final Actor actor = this.actorService.findByPrincipal();
					result.addObject("actor", actor);
				}

			} else {
				final Collection<domain.Package> packages = this.finderService.search(find);
				result = new ModelAndView("redirect:/finder/customer/list.do");
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}
	@RequestMapping(value = "/manager/display", method = RequestMethod.GET)
	public ModelAndView finderStatistic() {
		ModelAndView result;
		List<String> packs = new ArrayList<>();
		Collection<domain.Package> packages = new ArrayList<>();
		List<Integer> requests = new ArrayList<>();
		try {
			result = new ModelAndView("finder/display");
			packages = this.packageService.getListLoged();
			packs = this.packageService.getListLogedTitle();
			requests = this.finderService.finderStatisticRequest(packages);
			result.addObject("packages", packs.toString());
			result.addObject("requests", requests);

		} catch (final Throwable oopsie) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

}
