
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ContractService;
import services.SocialNetworkFileService;
import domain.Contract;
import domain.SocialNetworkFile;

@Controller
@RequestMapping("/socialNetworkFile")
public class SocialNetworkFileController extends AbstractController {

	// Services

	@Autowired
	private SocialNetworkFileService		socialNetworkFileService;

	@Autowired
	private ContractService	contractService;


	// Create
	// ----------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int Id) {
		ModelAndView result = new ModelAndView("socialNetworkFile/edit");
		try {
			final Contract contract = this.contractService.assertValidContract(Id);
			SocialNetworkFile socialNetworkFile = this.socialNetworkFileService.create(contract);
			result.addObject("socialNetworkFile", socialNetworkFile);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	// Display ------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int Id) {
		ModelAndView result = new ModelAndView("socialNetworkFile/display");
		try {
			SocialNetworkFile socialNetworkFile = this.socialNetworkFileService.findOneToDisplay(Id);
			result.addObject("socialNetworkFile", socialNetworkFile);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}
	// Save ------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editt(@RequestParam final int Id) {
		ModelAndView result = new ModelAndView("socialNetworkFile/edit");
		try {
			SocialNetworkFile socialNetworkFile = this.socialNetworkFileService.findOneIfOwnerAndDraft(Id, true);
			result.addObject("socialNetworkFile", socialNetworkFile);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView saveFinal(final SocialNetworkFile socialNetworkFileF, final BindingResult binding) {
		ModelAndView result = new ModelAndView("socialNetworkFile/edit");
		try {
			SocialNetworkFile socialNetworkFile = this.socialNetworkFileService.reconstruct(socialNetworkFileF, binding);

			if (binding.hasErrors()) {
				result.addObject("socialNetworkFile", socialNetworkFileF);
			} else {
				final SocialNetworkFile f = this.socialNetworkFileService.save(socialNetworkFile);
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
			final Integer i = this.socialNetworkFileService.delete(Id);
			result = new ModelAndView("redirect:/contract/display.do?Id=" + i);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}
}
