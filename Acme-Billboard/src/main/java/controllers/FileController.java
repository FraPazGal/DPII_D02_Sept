
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
import services.FileService;
import domain.Contract;
import domain.File;

@Controller
@RequestMapping("/file")
public class FileController extends AbstractController {

	// Services

	@Autowired
	private FileService		fileService;

	@Autowired
	private ContractService	contractService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int Id) {
		ModelAndView result;
		Collection<File> files;
		try {
			files = this.fileService.getListAllByContract(Id);
			result = new ModelAndView("file/list");
			result.addObject("files", files);
			result.addObject("requestURI", "file/list.do");

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
		File fileD;
		try {
			final Contract contract = this.contractService.findOne(Id);
			fileD = this.fileService.create(contract);
			result = new ModelAndView("file/edit");
			result.addObject("file", fileD);
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
		File fileD;
		try {
			fileD = this.fileService.findOneIfOwner(Id);
			result = new ModelAndView("file/display");
			result.addObject("file", fileD);
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
		File fileD;
		try {
			fileD = this.fileService.findOneIfOwner(Id);
			result = new ModelAndView("file/edit");
			result.addObject("file", fileD);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView saveFinal(final File fileF, final BindingResult binding) {
		ModelAndView result;
		File fileD;
		try {

			fileD = this.fileService.reconstruct(fileF, binding);

			if (binding.hasErrors()) {
				result = new ModelAndView("file/edit");
				result.addObject("file", fileF);
			} else {
				final File f = this.fileService.save(fileD);
				result = new ModelAndView("redirect:/file/list.do?Id=" + f.getContract().getId());
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
			final Integer i = this.fileService.delete(Id);
			result = new ModelAndView("redirect:/file/list.do?Id=" + i);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}
}
