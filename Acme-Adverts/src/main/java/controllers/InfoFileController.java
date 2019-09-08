
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ContractService;
import services.InfoFileService;
import domain.Contract;
import domain.InfoFile;

@Controller
@RequestMapping("/infoFile")
public class InfoFileController extends AbstractController {

	// Services

	@Autowired
	private InfoFileService		infoFileService;

	@Autowired
	private ContractService	contractService;


	// Create
	// ----------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int Id) {
		ModelAndView result = new ModelAndView("infoFile/edit");
		try {
			final Contract contract = this.contractService.assertValidContract(Id);
			InfoFile infoFile = this.infoFileService.create(contract);
			result.addObject("infoFile", infoFile);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	// Display ------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int Id) {
		ModelAndView result = new ModelAndView("infoFile/display");
		try {
			InfoFile infoFile = this.infoFileService.findOneIfOwner(Id);
			result.addObject("infoFile", infoFile);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}
	// Save ------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editt(@RequestParam final int Id) {
		ModelAndView result = new ModelAndView("infoFile/edit");
		try {
			InfoFile infoFile = this.infoFileService.findOneIfOwnerAndDraft(Id);
			result = new ModelAndView("infoFile/edit");
			result.addObject("infoFile", infoFile);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView saveFinal(final InfoFile infoFileF, final BindingResult binding) {
		ModelAndView result = new ModelAndView("infoFile/edit");
		try {
			InfoFile infoFile = this.infoFileService.reconstruct(infoFileF, binding);

			if (binding.hasErrors()) {
				result.addObject("infoFile", infoFileF);
			} else {
				final InfoFile f = this.infoFileService.save(infoFile);
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
			final Integer i = this.infoFileService.delete(Id);
			result = new ModelAndView("redirect:/contract/display.do?Id=" + i);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}
}
