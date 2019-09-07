
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ContractService;
import services.RadioFileService;
import domain.Contract;
import domain.RadioFile;

@Controller
@RequestMapping("/radioFile")
public class RadioFileController extends AbstractController {

	// Services

	@Autowired
	private RadioFileService		radioFileService;

	@Autowired
	private ContractService	contractService;


	// Create
	// ----------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int Id) {
		ModelAndView result = new ModelAndView("radioFile/edit");
		try {
			final Contract contract = this.contractService.findOne(Id);
			RadioFile radioFile = this.radioFileService.create(contract);
			result.addObject("radioFile", radioFile);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	// Display ------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int Id) {
		ModelAndView result = new ModelAndView("radioFile/display");
		try {
			RadioFile radioFile = this.radioFileService.findOneIfOwner(Id);
			result.addObject("radioFile", radioFile);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}
	// Save ------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editt(@RequestParam final int Id) {
		ModelAndView result = new ModelAndView("radioFile/edit");
		try {
			RadioFile radioFile = this.radioFileService.findOneIfOwner(Id);
			result.addObject("radioFile", radioFile);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView saveFinal(final RadioFile radioFileF, final BindingResult binding) {
		ModelAndView result = new ModelAndView("radioFile/edit");
		try {
			RadioFile radioFile = this.radioFileService.reconstruct(radioFileF, binding);

			if (binding.hasErrors()) {
				result.addObject("radioFile", radioFileF);
			} else {
				final RadioFile f = this.radioFileService.save(radioFile);
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
			final Integer i = this.radioFileService.delete(Id);
			result = new ModelAndView("redirect:/contract/display.do?Id=" + i);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}
}
