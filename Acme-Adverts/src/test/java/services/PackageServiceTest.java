
package services;

import java.util.Calendar;
import java.util.Collection;

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
import forms.PackageForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PackageServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 9'6
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 2.408
	 * 
	 * ################################################################
	 * 
	 * Test 1: A manager list and edit a package
	 * 
	 * Test 2: A manager create a package
	 * 
	 * Test 3: A manager list and display a package
	 * 
	 * Test 4: A manager list and delete a package
	 */
	@Autowired
	private PackageService	packageService;


	//Test 1: A manager list and edit a package
	//(Req.11.1)Manage his or her packages, which includes listing, creating, editing, and deleting them. 
	//A package may be edited or deleted as long as it is not saved in final mode.
	@Test
	public void driverEdit() {
		final Object testingData[][] = {
			{
				"manager1", "description", "title", 500.0, "http://us.es", true, "manager2", true, IllegalArgumentException.class
			},
			//Negative test case,  invalid final mode
			{
				"manager1", "description", "title", 500.0, "http://us.es", true, "manager2", false, IllegalArgumentException.class
			},
			//Negative test case,  invalid final mode
			{
				"manager1", "description", "title", 500.0, "http://us.es", true, "customer1", false, IllegalArgumentException.class
			},
			//Negative test case,  invalid final mode
			{
				"manager1", "description", "title", 500.0, "http://us.es", true, "admin", false, IllegalArgumentException.class
			},
			//Negative test case,  invalid final mode
			{
				"manager1", "description", null, 500.0, "http://us.es", true, "manager1", true, IllegalArgumentException.class
			},
			//Negative test case,  invalid final mode
			{
				"manager1", "description", "title", -500.0, "http://us.es", true, "manager1", true, IllegalArgumentException.class
			},
			//Negative test case,  invalid final mode

			{
				"admin", "description", "title", 500.0, "http://us.es", true, "manager1", false, IllegalArgumentException.class
			},
			//Negative test case, invalid user
			{
				"manager1", "description1", "title", 500.0, "http://us.es", false, "manager1", false, null
			},
			//Positive test case save in final mode			
			{
				"manager1", "description2", "title", 500.0, null, true, "manager1", false, null
			},
			//Positive test case save in final mode
			{
				"manager1", "description3", "title", 500.0, "http://us.es", true, "manager1", false, null
			}
		//Positive test case save in final mode
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Double) testingData[i][3], (String) testingData[i][4], (Boolean) testingData[i][5], (String) testingData[i][6], (Boolean) testingData[i][7],
				(Class<?>) testingData[i][8]);

	}

	protected void template(final String user, final String description, final String title, final Double price, final String photo, final Boolean mode, final String userList, final Boolean finalMode, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(userList);
			final Collection<domain.Package> packages = this.packageService.getListLoged();
			this.unauthenticate();

			this.authenticate(user);
			this.edit(packages, description, title, price, photo, mode, finalMode);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void edit(final Collection<domain.Package> packages, final String description, final String title, final Double price, final String photo, final Boolean mode, final Boolean finalMode) {
		for (final domain.Package p : packages)
			if (p.getFinalMode() == finalMode) {
				final PackageForm form = new PackageForm(p);
				final Calendar can = Calendar.getInstance();
				can.add(Calendar.MONTH, 1);
				form.setStartDate(can.getTime());
				can.add(Calendar.MONTH, 2);
				form.setEndDate(can.getTime());
				form.setDescription(description);
				form.setPhoto(photo);
				form.setPrice(price);
				form.setTitle(title);
				form.setFinalMode(mode);
				final BindingResult binding = new BeanPropertyBindingResult(form, form.getClass().getName());
				final domain.Package pack = this.packageService.reconstruct(form, binding);
				Assert.isTrue(binding.hasErrors() == false);
				this.packageService.save(pack);
				break;
			}
	}
	// Test 2: A manager create a package
	//(Req.11.1)Manage his or her packages, which includes listing, creating, editing, and deleting them. 
	//A package may be edited or deleted as long as it is not saved in final mode.
	@Test
	public void driverCreate() {
		final Object testingData[][] = {

			{
				"manager1", null, "title", 500.0, "http://us.es", IllegalArgumentException.class
			},
			//Negative test case,  invalid data

			{
				"admin", "description", "title", 500.0, "http://us.es", IllegalArgumentException.class
			},
			//Negative test case, invalid user
			{
				"manager1", "description", "title", 500.0, "http://us.es", null
			},
		//Positive test case save in final mode
		};

		for (int i = 0; i < testingData.length; i++)
			this.template2((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Double) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	protected void template2(final String user, final String description, final String title, final Double price, final String photo, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			this.create(description, title, price, photo);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void create(final String description, final String title, final Double price, final String photo) {
		final PackageForm form = this.packageService.createF();
		final Calendar can = Calendar.getInstance();
		can.add(Calendar.MONTH, 1);
		form.setStartDate(can.getTime());
		can.add(Calendar.MONTH, 2);
		form.setEndDate(can.getTime());
		form.setDescription(description);
		form.setPhoto(photo);
		form.setPrice(price);
		form.setTitle(title);
		final BindingResult binding = new BeanPropertyBindingResult(form, form.getClass().getName());
		final domain.Package pack = this.packageService.reconstruct(form, binding);
		Assert.isTrue(binding.hasErrors() == false);
		this.packageService.save(pack);
	}

	// Test 3: A manager list and display a package
	//(Req.11.1)Manage his or her packages, which includes listing, creating, editing, and deleting them. 
	//A package may be edited or deleted as long as it is not saved in final mode.
	@Test
	public void driverDisplay() {
		final Object testingData[][] = {
			{
				"manager1", "manager2", true/* finalMode */, null
			},
			//Positive test case

			{
				"manager1", "manager2", false/* finalMode */, IllegalArgumentException.class
			},
			//Negative test case

			{
				"manager1", "customer1", false/* finalMode */, IllegalArgumentException.class
			}
		//Negative test case

		};

		for (int i = 0; i < testingData.length; i++)
			this.template3((String) testingData[i][0], (String) testingData[i][1], (Boolean) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	protected void template3(final String userList, final String user, final Boolean finalMode, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(userList);
			final Collection<domain.Package> packages = this.packageService.getListLoged();
			this.unauthenticate();

			this.authenticate(user);
			this.display(finalMode, packages);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void display(final Boolean finalMode, final Collection<domain.Package> packages) {
		for (final domain.Package p : packages)
			if (p.getFinalMode() == finalMode) {
				this.packageService.findOneMode(p.getId());
				break;
			}
	}

	// Test 4: A manager list and delete a package
	//(Req.11.1)Manage his or her packages, which includes listing, creating, editing, and deleting them. 
	//A package may be edited or deleted as long as it is not saved in final mode.
	@Test
	public void driverDelete() {
		final Object testingData[][] = {
			{
				"manager1", "manager2", true/* finalMode */, IllegalArgumentException.class
			},
			//Positive test case

			{
				"manager1", "manager1", true/* finalMode */, IllegalArgumentException.class
			},
			//Negative test case

			{
				"manager1", "manager1", false/* finalMode */, null
			}
		//Negative test case

		};

		for (int i = 0; i < testingData.length; i++)
			this.template4((String) testingData[i][0], (String) testingData[i][1], (Boolean) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	protected void template4(final String userList, final String user, final Boolean finalMode, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(userList);
			final Collection<domain.Package> packages = this.packageService.getListLoged();
			this.unauthenticate();

			this.authenticate(user);
			this.delete(finalMode, packages);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void delete(final Boolean finalMode, final Collection<domain.Package> packages) {
		for (final domain.Package p : packages)
			if (p.getFinalMode() == finalMode) {
				this.packageService.delete(p.getId());
				break;
			}
	}
}
