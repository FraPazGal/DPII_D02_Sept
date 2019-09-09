
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import utilities.AbstractTest;
import domain.Contract;
import domain.Request;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RequestServiceTest extends AbstractTest {

	/*
	 * /*
	 * Total coverage of all tests
	 * 
	 * 
	 * Coverage of the total project (%): 66'0
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 16.563
	 * 
	 * ################################################################
	 * 
	 * Total coverage by exclusively executing this test class
	 * 
	 * 
	 * Coverage of the total project (%): 8'0
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 2.003
	 * 
	 * ################################################################
	 * 
	 * Test 1: A manager create a request
	 * 
	 * Test 2: A manager list and display a request
	 * 
	 * Test 3: A manager list and accept/reject a request
	 */
	@Autowired
	private RequestService	requestService;

	@Autowired
	private ContractService	contractService;

	@Autowired
	private PackageService	packageService;


	//Test 1: A manager create a request
	//(Req.10.1) Request a package. He or she may add some optional comments to the request.
	@Test
	public void driverCreateRequest() {
		final Object testingData[][] = {

			{
				"customer1", "manager1", "Test", IllegalArgumentException.class
			},
			//Negative test case

			{
				"customer1", "admin", null, IllegalArgumentException.class
			},
			//Negative test case
			{
				"customer1", "customer1", "Test", null
			}
		//Positive test case save in final mode
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	protected void template(final String userList, final String user, final String text, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(userList);
			final Collection<domain.Package> packagses = this.packageService.getListAll();
			this.unauthenticate();
			this.authenticate(user);
			this.create(packagses, text);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void create(final Collection<domain.Package> packagses, final String text) {
		final Date now = new Date();
		for (final domain.Package p : packagses)
			if (p.getEndDate().after(now)) {
				final Request form = this.requestService.create(p);
				form.setCommentsCustomer(text);
				final BindingResult binding = new BeanPropertyBindingResult(form, form.getClass().getName());
				final Request re = this.requestService.reconstruct(form, binding);
				Assert.isTrue(binding.hasErrors() == false);
				this.requestService.save(re);
				break;
			}
	}

	//Test 2: A manager list and display a request
	@Test
	public void driverDisplayRequest() {
		final Object testingData[][] = {
			{
				"customer1", "customer1", null
			},
			//Positive test case

			{
				"customer1", "customer2", IllegalArgumentException.class
			},
			//Negative test case

			{
				"customer1", "admin", IllegalArgumentException.class
			},
		//Negative test case

		};

		for (int i = 0; i < testingData.length; i++)
			this.template2((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void template2(final String userList, final String user, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(userList);
			final Collection<Request> requests = this.requestService.getListAll();
			this.unauthenticate();
			this.authenticate(user);
			this.display(requests);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void display(final Collection<Request> requests) {
		for (final Request r : requests) {
			this.requestService.findOneMode(r.getId());
			break;
		}
	}

	//Test 3: A manager list and accept/reject a request
	//(Req.11.2)Manage the requests to his or her packages, which includes listing them, 
	//accepting, and rejecting them. Note that a request may be rejected without providing 
	//a com-ment, but the system must ask for confirmation in such cases.
	//(Req.11.3) Manage his or her contracts, which includes listing, editing, and signing 
	//them. When a request is accepted, a draft contract is created automatically; such draft 
	//only con-tains a piece of text that is generated automatically from the corresponding 
	//offer and an empty file. Editing a contract means allowing the manager to change the 
	//text and to fill the corresponding file. Signing a contract means that the contract is 
	//saved in final mode so that it cannot be further edited; the system must then comçpute
	//the hash automatically. Once a contract is signed by a manager, it must be-come immediately 
	//available to the corresponding customer.
	@Test
	public void driverStatusRequest() {
		final Object testingData[][] = {

			{
				"manager2", "customer2", true/* true=aceptar,false=rechazar */, IllegalArgumentException.class
			},
			//Negative test case

			{
				"manager2", "customer1", true/* true=aceptar,false=rechazar */, IllegalArgumentException.class
			},
			//Negative test case
			{
				"manager2", "admin", true/* true=aceptar,false=rechazar */, IllegalArgumentException.class
			},
			//Negative test case
			{
				"manager2", "manager2", true/* true=aceptar,false=rechazar */, null
			},
			//Positive test case
			{
				"manager2", "manager2", false/* true=aceptar,false=rechazar */, null
			}
		//Positive test case
		};

		for (int i = 0; i < testingData.length; i++)
			this.template3((String) testingData[i][0], (String) testingData[i][1], (Boolean) testingData[i][2], (Class<?>) testingData[i][3]);

	}
	protected void template3(final String userList, final String user, final Boolean status, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(userList);
			final Collection<Request> requests = this.requestService.getListAll();
			this.unauthenticate();
			this.authenticate(user);
			this.Status(requests, status);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	@SuppressWarnings("unused")
	public void Status(final Collection<Request> requests, final Boolean status) {
		for (final Request r : requests) {
			if (r.getStatus().equals("PENDING")) {
				if (status) {
					final Request form = this.requestService.create(r.getPack());
					form.setCommentsManager(null);
					form.setId(r.getId());
					final BindingResult binding = new BeanPropertyBindingResult(form, form.getClass().getName());
					final Request re = this.requestService.reconstruct(form, binding);
					Assert.isTrue(binding.hasErrors() == false);
					final Request res = this.requestService.save(re);
					final Contract con = this.contractService.draftContract(res);
					break;
				}
				if (!status)
					this.requestService.rejected(r);
			}
			break;
		}
	}
}
