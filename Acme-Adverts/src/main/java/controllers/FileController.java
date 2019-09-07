package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ContractService;
import services.UtilityService;
import domain.Contract;
import forms.TypeForm;

@Controller
@RequestMapping("/file")
public class FileController extends AbstractController {

	// Services
	
	@Autowired
	private ContractService	contractService;
	
	@Autowired
	private UtilityService	utilityService;
	
	
	/* Display */
	@RequestMapping(value = "/selector", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int contractId) {
		ModelAndView result = new ModelAndView("file/selector");
		try {
			TypeForm typeForm = new TypeForm();
			Contract contract = this.contractService.findOne(contractId);
			typeForm.setId(contract.getId());
			result.addObject("typeForm", typeForm);
			result.addObject("backId", contract.getRequest().getId());
			result.addObject("types", this.utilityService.typeOfFile());
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	/* Selector */
	@RequestMapping(value = "/selector", method = RequestMethod.POST, params="select")
	public ModelAndView actionsEnrolments(@Valid TypeForm typeForm, BindingResult binding) {
		ModelAndView result = new ModelAndView("file/selector");
		try {
			if(!binding.hasErrors()) {
				switch (typeForm.getType()) {
				case "Billboard":
					result = new ModelAndView("redirect:../billboardFile/create.do?Id=" + typeForm.getId());
					break;
					
				case "TV":
					result = new ModelAndView("redirect:../TVFile/create.do?Id=" + typeForm.getId());
					break;
				
				case "Radio":
					result = new ModelAndView("redirect:../radioFile/create.do?Id=" + typeForm.getId());
					break;
					
				case "SocialNetwork":
					result = new ModelAndView("redirect:../socialNetworkFile/create.do?Id=" + typeForm.getId());
					break;
					
				case "Info":
					result = new ModelAndView("redirect:../infoFile/create.do?Id=" + typeForm.getId());
					break;
				}
			} else {
				result.addObject("typeForm", typeForm);
				result.addObject("types", this.utilityService.typeOfFile());
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}
}
