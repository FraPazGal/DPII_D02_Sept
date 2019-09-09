/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import services.AdministratorService;
import services.CustomerService;
import services.ManagerService;

@Controller
public class ExportDataController extends AbstractController {

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private CustomerService			customerService;

	@Autowired
	private ManagerService			managerService;


	@RequestMapping(value = "administrator/export.do", method = RequestMethod.GET)
	public @ResponseBody
	void downloadFileAdministrator(final HttpServletResponse resp) {
		final String downloadFileName = "dataUser";
		String res;
		try {
			res = this.administratorService.export();
			
			final String downloadStringContent = res; // implement this
			final OutputStream out = resp.getOutputStream();
			resp.setContentType("text/plain; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename=\"" + downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (final IOException e) {
		}

	}

	@RequestMapping(value = "manager/export.do", method = RequestMethod.GET)
	public @ResponseBody
	void downloadFileManager(final HttpServletResponse resp) {
		final String downloadFileName = "dataUser";
		String res;
		try {
			res = this.managerService.export();
			
			final String downloadStringContent = res;
			final OutputStream out = resp.getOutputStream();
			resp.setContentType("text/plain; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename=\"" + downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (final IOException e) {
		}

	}

	@RequestMapping(value = "customer/export.do", method = RequestMethod.GET)
	public @ResponseBody
	void downloadFileCompany(final HttpServletResponse resp) {
		final String downloadFileName = "dataUser";
		String res;
		try {
			res = this.customerService.export();
			
			final String downloadStringContent = res;
			final OutputStream out = resp.getOutputStream();
			resp.setContentType("text/plain; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename=\"" + downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (final IOException e) {
		}

	}

}
