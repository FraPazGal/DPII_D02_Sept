
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ContractService;
import services.PackageService;
import services.RequestService;
import domain.Request;

@Controller
@RequestMapping("/request")
public class RequestController extends AbstractController {

	// Services
	@Autowired
	private ContractService	contractService;

	@Autowired
	private RequestService	requestService;

	@Autowired
	private PackageService	packageService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Request> requests;
		try {
			requests = this.requestService.getListAll();
			result = new ModelAndView("request/list");
			result.addObject("requests", requests);
			result.addObject("requestURI", "request/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/listPackage", method = RequestMethod.GET)
	public ModelAndView listByPackage(@RequestParam final int Id) {
		ModelAndView result;
		Collection<Request> requests;
		try {
			requests = this.requestService.getList(Id);
			result = new ModelAndView("request/list");
			result.addObject("requests", requests);
			result.addObject("requestURI", "request/listPackage.do?Id=" + Id);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	// Create
	// ----------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int Id) {
		ModelAndView result;
		Request request;
		try {
			request = this.requestService.create(this.packageService.findOne(Id));
			result = new ModelAndView("request/edit");
			result.addObject("request", request);
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
		Request request;
		try {
			request = this.requestService.findOneMode(Id);
			result = new ModelAndView("request/display");
			result.addObject("request", request);
			result.addObject("commentM", this.requestService.comments(request, "MANAGER"));
			result.addObject("commentC", this.requestService.comments(request, "CUSTOMER"));
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}
	// Save ------------------------------------------------------------
	//
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editt(@RequestParam final int Id) {
		ModelAndView result;
		Request request;
		try {
			request = this.requestService.findOneMode(Id);
			request = this.requestService.toEdit(request);
			result = new ModelAndView("request/edit");
			result.addObject("request", request);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageCode", "position.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView saveFinal(final Request requestF, final BindingResult binding) {
		ModelAndView result;
		Request request;
		try {
			request = this.requestService.reconstruct(requestF, binding);
			if (binding.hasErrors()) {
				result = new ModelAndView("request/edit");
				result.addObject("request", request);
			} else {
				final Request res = this.requestService.save(request);
				if (res.getStatus().equals("APPROVED"))
					this.contractService.draftContract(res);
				result = new ModelAndView("redirect:/request/list.do");
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}

	// Delete ------------------------------------------------------------

	//	@RequestMapping(value = "/aproved", method = RequestMethod.GET)
	//	public ModelAndView approved(final int Id) {
	//		ModelAndView result;
	//		try {
	//			this.requestService.approved(Id);
	//			result = new ModelAndView("redirect:/request/list.do");
	//		} catch (final Throwable oops) {
	//			result = new ModelAndView("redirect:/welcome/index.do");
	//			result.addObject("messageCode", "commit.error");
	//		}
	//		return result;
	//	}
	@RequestMapping(value = "/manager/rejected", method = RequestMethod.GET)
	public ModelAndView rejected(@RequestParam final int Id) {
		ModelAndView result;
		try {
			this.requestService.rejected(Id);
			result = new ModelAndView("redirect:/request/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

}
