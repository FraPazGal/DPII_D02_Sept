
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ContractService;
import services.TVFileService;
import domain.Contract;
import domain.TVFile;

@Controller
@RequestMapping("/TVFile")
public class TVFileController extends AbstractController {

	// Services

	@Autowired
	private TVFileService		TVFileService;

	@Autowired
	private ContractService	contractService;


	// Create
	// ----------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int Id) {
		ModelAndView result;
		try {
			final Contract contract = this.contractService.assertValidContract(Id);
			TVFile TVFile = this.TVFileService.create(contract);
			result = new ModelAndView("TVFile/edit");
			result.addObject("TVFile", TVFile);
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
		try {
			TVFile TVFile = this.TVFileService.findOneToDisplay(Id);
			result = new ModelAndView("TVFile/display");
			result.addObject("TVFile", TVFile);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}
	// Save ------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editt(@RequestParam final int Id) {
		ModelAndView result;
		try {
			TVFile TVFile = this.TVFileService.findOneIfOwnerAndDraft(Id, true);
			result = new ModelAndView("TVFile/edit");
			result.addObject("TVFile", TVFile);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView saveFinal(final TVFile TVFileF, final BindingResult binding) {
		ModelAndView result;
		try {

			TVFile TVFile = this.TVFileService.reconstruct(TVFileF, binding);

			if (binding.hasErrors()) {
				result = new ModelAndView("TVFile/edit");
				result.addObject("TVFile", TVFileF);
			} else {
				final TVFile f = this.TVFileService.save(TVFile);
				result = new ModelAndView("redirect:/contract/display.do?Id=" + f.getContract().getRequest().getId());
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int Id) {
		ModelAndView result;
		try {
			final Integer i = this.TVFileService.delete(Id);
			result = new ModelAndView("redirect:/contract/display.do?Id=" + i);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}
}
