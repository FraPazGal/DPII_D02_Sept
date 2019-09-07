
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BillboardFileService;
import services.ContractService;
import domain.BillboardFile;
import domain.Contract;

@Controller
@RequestMapping("/billboardFile")
public class BillboardFileController extends AbstractController {

	// Services

	@Autowired
	private BillboardFileService		billboardFileService;

	@Autowired
	private ContractService	contractService;

	// Create
	// ----------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int Id) {
		ModelAndView result = new ModelAndView("billboardFile/edit");
		try {
			final Contract contract = this.contractService.findOne(Id);
			BillboardFile billboardFile = this.billboardFileService.create(contract);
			result.addObject("billboardFile", billboardFile);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	// Display ------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int Id) {
		ModelAndView result = new ModelAndView("billboardFile/display");
		try {
			BillboardFile billboardFile = this.billboardFileService.findOneIfOwner(Id);
			result.addObject("billboardFile", billboardFile);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}
	// Save ------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editt(@RequestParam final int Id) {
		ModelAndView result = new ModelAndView("billboardFile/edit");
		try {
			BillboardFile billboardFile = this.billboardFileService.findOneIfOwner(Id);
			result.addObject("billboardFile", billboardFile);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView saveFinal(final BillboardFile billboardFileF, final BindingResult binding) {
		ModelAndView result = new ModelAndView("billboardFile/edit");
		try {
			BillboardFile billboardFile = this.billboardFileService.reconstruct(billboardFileF, binding);

			if (binding.hasErrors()) {
				result.addObject("billboardFile", billboardFileF);
			} else {
				final BillboardFile f = this.billboardFileService.save(billboardFile);
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
			final Integer i = this.billboardFileService.delete(Id);
			result = new ModelAndView("redirect:/contract/display.do?Id=" + i);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}
}
