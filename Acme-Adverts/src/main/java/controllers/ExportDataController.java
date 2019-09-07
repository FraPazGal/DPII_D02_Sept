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
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import services.ActorService;
import services.BillboardFileService;
import services.ContractService;
import services.FinderService;
import services.InfoFileService;
import services.PackageService;
import services.RadioFileService;
import services.RequestService;
import services.SocialNetworkFileService;
import services.TVFileService;
import domain.Administrator;
import domain.BillboardFile;
import domain.Contract;
import domain.Customer;
import domain.InfoFile;
import domain.Manager;
import domain.RadioFile;
import domain.Request;
import domain.SocialNetworkFile;
import domain.TVFile;

@Controller
public class ExportDataController extends AbstractController {

	@Autowired
	private ActorService	actorService;
	
	@Autowired
	private PackageService	packageService;
	
	@Autowired
	private ContractService	contractService;
	
	@Autowired
	private FinderService	finderService;
	
	@Autowired
	private RequestService	requestService;
	
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


	@RequestMapping(value = "administrator/export.do", method = RequestMethod.GET)
	public @ResponseBody
	void downloadFileAdministrator(final HttpServletResponse resp) {
		final String downloadFileName = "dataUser";
		String res;
		try {

			final Administrator actor = (Administrator) this.actorService.findByPrincipal();

			res = "Data of your user account:";
			res += "\r\n\r\n";
			res += "Name: " + actor.getName() + " \r\n" + "Surname: " + actor.getSurname() + " \r\n" + "MiddleName:" + actor.getMiddleName() + " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: " + actor.getEmail() + " \r\n" + "Phone Number: "
				+ actor.getPhoneNumber() + " \r\n" + "Address: " + actor.getAddress() + " \r\n" + " \r\n" + "\r\n";
			res += "\r\n\r\n";
			res += "----------------------------------------";
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

			final Manager actor = (Manager) this.actorService.findByPrincipal();

			res = "Data of your user account:";
			res += "\r\n\r\n";
			res += "Name: " + actor.getName() + " \r\n" + "Surname: " + actor.getSurname() + " \r\n" + "VAT:" + actor.getMiddleName() + " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: " + actor.getEmail() + " \r\n" + "Phone Number: "
				+ actor.getPhoneNumber() + " \r\n" + "Address: " + actor.getAddress() + " \r\n" + " \r\n" + "\r\n";
			res += "\r\n\r\n";
			res += "----------------------------------------";
			res += "\r\n\r\n";
			res += "Packages:";
			res += "\r\n\r\n";
			final Collection<domain.Package> packs = this.packageService.getListLoged();

			for (final domain.Package p : packs) {
				res += "----------------------------------------";
				res += "\r\n\r\n";
				res += "Package: " + "\r\n\r\n";
				res += "Ticker: " + p.getTicker() + "\r\n\r\n" + "Title: " + p.getTitle() + "\r\n\r\n" + "Description: " + p.getDescription() + "\r\n\r\n" + "Start Date: " + p.getStartDate() + "\r\n\r\n" + "End Date: " + p.getEndDate() + "\r\n\r\n"
					+ "Photo: " + p.getPhoto() + "Price: " + p.getPrice() + "\r\n\r\n";
				res += "\r\n\r\n";

			}
			final Collection<Request> requests = this.requestService.getListAll();

			for (final Request r : requests) {
				res += "----------------------------------------";
				res += "\r\n\r\n";
				res += "Request: " + "\r\n\r\n";
				res += "Status: " + r.getStatus() + "\r\n\r\n";
				res += "Customer comments:  " + r.getCommentsCustomer() + "\r\n\r\n";
				res += "Manager Comments: " + r.getCommentsManager() + "\r\n\r\n";
				res += "Pack: " + r.getPack().getTitle() + "\r\n\r\n";
				res += "-----------";
			}
			final Collection<Contract> contracts = this.contractService.getListAll();

			for (final Contract c : contracts) {
				final Collection<BillboardFile> billboardFiles = this.billboardFileService.getListAllByContractToDelete(c.getId());
				final Collection<InfoFile> infoFiles = this.infoFileService.getListAllByContractToDelete(c.getId());
				final Collection<RadioFile> radioFiles = this.radioFileService.getListAllByContractToDelete(c.getId());
				final Collection<TVFile> TVFiles = this.TVFileService.getListAllByContractToDelete(c.getId());
				final Collection<SocialNetworkFile> socialNetworkFiles = this.socialNetworkFileService.getListAllByContractToDelete(c.getId());
				res += "----------------------------------------";
				res += "\r\n\r\n";
				res += "Contract: " + "\r\n\r\n";
				res += "Customer signed date: " + c.getSignedCustomer() + "\r\n\r\n";
				res += "Manager signed date: " + c.getSignedManager() + "\r\n\r\n";
				res += "Text:  " + c.getText() + "\r\n\r\n";
				res += "Pack: " + c.getRequest().getPack().getTitle() + "\r\n\r\n\r\n";
				res += "Files: " + "\r\n\r\n\r\n";
				res += "Billboard Files: " + "\r\n\r\n";
				for (final BillboardFile f : billboardFiles) {
					res += "Location: " + f.getLocation() + "\r\n\r\n";
					res += "Image: " + f.getImage() + "\r\n\r\n";
					res += "-----";
					res += "\r\n";
				}
				res += "-----------" + "\r\n\r\n";
				res += "Info Files: " + "\r\n\r\n";
				for (final InfoFile f : infoFiles) {
					res += "Title: " + f.getTitle() + "\r\n\r\n";
					res += "Text: " + f.getText() + "\r\n\r\n";
					res += "-----";
					res += "\r\n";
				}
				res += "-----------" + "\r\n\r\n";
				res += "Radio Files: " + "\r\n\r\n";
				for (final RadioFile f : radioFiles) {
					res += "Broadcaster Name: " + f.getBroadcasterName() + "\r\n\r\n";
					res += "Schedule: " + f.getSchedule() + "\r\n\r\n";
					res += "Sound: " + f.getSound() + "\r\n\r\n";
					res += "-----";
					res += "\r\n";
				}
				res += "-----------" + "\r\n\r\n";
				res += "TV Files: " + "\r\n\r\n";
				for (final TVFile f : TVFiles) {
					res += "Broadcaster Name: " + f.getBroadcasterName() + "\r\n\r\n";
					res += "Schedule: " + f.getSchedule() + "\r\n\r\n";
					res += "Video: " + f.getVideo() + "\r\n\r\n";
					res += "-----";
					res += "\r\n";
				}
				res += "-----------" + "\r\n\r\n";
				res += "Social Network Files: " + "\r\n\r\n";
				for (final SocialNetworkFile f : socialNetworkFiles) {
					res += "Banner: " + f.getBanner() + "\r\n\r\n";
					res += "Target: " + f.getTarget() + "\r\n\r\n";
					res += "-----";
					res += "\r\n";
				}
				res += "-----------";
				
			}

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

			final Customer actor = (Customer) this.actorService.findByPrincipal();

			res = "Data of your user account:";
			res += "\r\n\r\n";
			res += "Name: " + actor.getName() + " \r\n" + "Surname: " + actor.getSurname() + " \r\n" + "MiddleName:" + actor.getMiddleName() + " \r\n" + "VAT:" + actor.getVat() + " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: "
				+ actor.getEmail() + " \r\n" + "Phone Number: " + actor.getPhoneNumber() + " \r\n" + "Address: " + actor.getAddress() + "\r\n" + " \r\n" + " \r\n" + "\r\n" + "Credit Card:" + "\r\n" + "Holder:" + actor.getCreditCard().getHolder() + "\r\n" +

				"Make:" + actor.getCreditCard().getMake() + "\r\n" + "Number:" + actor.getCreditCard().getNumber() + "\r\n" + "Date expiration:" + actor.getCreditCard().getExpirationMonth() + "/" + actor.getCreditCard().getExpirationYear() + "\r\n"
				+ "CVV:" + actor.getCreditCard().getCVV();
			res += "\r\n\r\n";
			res += "----------------------------------------";
			res += "\r\n\r\n";
			final Collection<domain.Package> packs = this.packageService.getListLoged();

			for (final domain.Package p : packs) {
				res += "----------------------------------------";
				res += "\r\n\r\n";
				res += "Package: " + "\r\n\r\n";
				res += "Ticker: " + p.getTicker() + "\r\n\r\n" + "Title: " + p.getTitle() + "\r\n\r\n" + "Description: " + p.getDescription() + "\r\n\r\n" + "Start Date: " + p.getStartDate() + "\r\n\r\n" + "End Date: " + p.getEndDate() + "\r\n\r\n"
					+ "Photo: " + p.getPhoto() + "Price: " + p.getPrice() + "\r\n\r\n";
				res += "\r\n\r\n";

			}
			final Collection<Request> requests = this.requestService.getListAll();

			for (final Request r : requests) {
				res += "----------------------------------------";
				res += "\r\n\r\n";
				res += "Request: " + "\r\n\r\n";
				res += "Status: " + r.getStatus() + "\r\n\r\n";
				res += "Customer comments:  " + r.getCommentsCustomer() + "\r\n\r\n";
				res += "Manager Comments: " + r.getCommentsManager() + "\r\n\r\n";
				res += "Pack: " + r.getPack().getTitle() + "\r\n\r\n";
				res += "-----------";
			}
			final Collection<Contract> contracts = this.contractService.getListAll();

			for (final Contract c : contracts) {
				final Collection<BillboardFile> billboardFiles = this.billboardFileService.getListAllByContractToDelete(c.getId());
				final Collection<InfoFile> infoFiles = this.infoFileService.getListAllByContractToDelete(c.getId());
				final Collection<RadioFile> radioFiles = this.radioFileService.getListAllByContractToDelete(c.getId());
				final Collection<TVFile> TVFiles = this.TVFileService.getListAllByContractToDelete(c.getId());
				final Collection<SocialNetworkFile> socialNetworkFiles = this.socialNetworkFileService.getListAllByContractToDelete(c.getId());
				res += "----------------------------------------";
				res += "\r\n\r\n";
				res += "Contract: " + "\r\n\r\n";
				res += "Customer signed date: " + c.getSignedCustomer() + "\r\n\r\n";
				res += "Manager signed date: " + c.getSignedManager() + "\r\n\r\n";
				res += "Text:  " + c.getText() + "\r\n\r\n";
				res += "Pack: " + c.getRequest().getPack().getTitle() + "\r\n\r\n\r\n";
				res += "Files: " + "\r\n\r\n\r\n";
				res += "Billboard Files: " + "\r\n\r\n";
				for (final BillboardFile f : billboardFiles) {
					res += "Location: " + f.getLocation() + "\r\n\r\n";
					res += "Image: " + f.getImage() + "\r\n\r\n";
					res += "-----";
					res += "\r\n";
				}
				res += "-----------" + "\r\n\r\n";
				res += "Info Files: " + "\r\n\r\n";
				for (final InfoFile f : infoFiles) {
					res += "Title: " + f.getTitle() + "\r\n\r\n";
					res += "Text: " + f.getText() + "\r\n\r\n";
					res += "-----";
					res += "\r\n";
				}
				res += "-----------" + "\r\n\r\n";
				res += "Radio Files: " + "\r\n\r\n";
				for (final RadioFile f : radioFiles) {
					res += "Broadcaster Name: " + f.getBroadcasterName() + "\r\n\r\n";
					res += "Schedule: " + f.getSchedule() + "\r\n\r\n";
					res += "Sound: " + f.getSound() + "\r\n\r\n";
					res += "-----";
					res += "\r\n";
				}
				res += "-----------" + "\r\n\r\n";
				res += "TV Files: " + "\r\n\r\n";
				for (final TVFile f : TVFiles) {
					res += "Broadcaster Name: " + f.getBroadcasterName() + "\r\n\r\n";
					res += "Schedule: " + f.getSchedule() + "\r\n\r\n";
					res += "Video: " + f.getVideo() + "\r\n\r\n";
					res += "-----";
					res += "\r\n";
				}
				res += "-----------" + "\r\n\r\n";
				res += "Social Network Files: " + "\r\n\r\n";
				for (final SocialNetworkFile f : socialNetworkFiles) {
					res += "Banner: " + f.getBanner() + "\r\n\r\n";
					res += "Target: " + f.getTarget() + "\r\n\r\n";
					res += "-----";
					res += "\r\n";
				}
				res += "-----------";
				
			}

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
