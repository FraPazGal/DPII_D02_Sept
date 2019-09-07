
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BillboardFileService;
import services.ContractService;
import services.InfoFileService;
import services.RadioFileService;
import services.SocialNetworkFileService;
import services.TVFileService;
import domain.Contract;

@Controller
@RequestMapping("/contract")
public class ContractController extends AbstractController {

	// Services

	@Autowired
	private ContractService	contractService;
	
	@Autowired
	private BillboardFileService	billboardFileService;
	
	@Autowired
	private SocialNetworkFileService	socialNetworkFileService;
	
	@Autowired
	private InfoFileService	infoFileService;
	
	@Autowired
	private TVFileService	TVFileService;
	
	@Autowired
	private RadioFileService	radioFileService;


	//List ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Contract> contracts;
		try {
			contracts = this.contractService.getListAll();
			result = new ModelAndView("contract/list");
			result.addObject("contracts", contracts);
			result.addObject("requestURI", "contract/list.do");
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
		Contract contract;
		try {
			contract = this.contractService.findOneByRequestAndOwner(Id);
			result = new ModelAndView("contract/display");
			result.addObject("contract", contract);
			result.addObject("billboardFiles", this.billboardFileService.getListAllByContract(Id));
			result.addObject("infoFiles", this.infoFileService.getListAllByContract(Id));
			result.addObject("TVFiles", this.TVFileService.getListAllByContract(Id));
			result.addObject("radioFiles", this.radioFileService.getListAllByContract(Id));
			result.addObject("socialNetworkFiles", this.socialNetworkFileService.getListAllByContract(Id));
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
		Contract contract;
		try {
			contract = this.contractService.findOneByRequestAndOwner(Id);
			result = new ModelAndView("contract/edit");
			result.addObject("contract", contract);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView saveFinal(final Contract contractF, final BindingResult binding) {
		ModelAndView result;
		Contract contract;
		try {

			contract = this.contractService.reconstruct(contractF, binding);

			if (binding.hasErrors()) {
				result = new ModelAndView("contract/edit");
				result.addObject("contract", contractF);
			} else {
				contract = this.contractService.save(contract);
				result = new ModelAndView("contract/display");
				result.addObject("contract", contract);
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}

	@RequestMapping(value = "/sign", method = RequestMethod.GET)
	public ModelAndView sign(@RequestParam final int Id) {
		ModelAndView result;
		try {
			Contract contract = this.contractService.sign(Id);
			result = new ModelAndView("redirect:display.do?Id=" + contract.getRequest().getId());
			
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

}
