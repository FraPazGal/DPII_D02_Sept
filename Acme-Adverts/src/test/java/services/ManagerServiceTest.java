
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import utilities.AbstractTest;
import domain.Actor;
import domain.Manager;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ManagerServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 14'3
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 3.597
	 * ################################################################
	 * 
	 * Test 1: An admin register new manager in the system
	 * 
	 * Test 2: A manager display his personal information
	 * 
	 * Test 3: A manager edit his personal information
	 * 
	 * Test 4: A manager delete his account
	 * 
	 * Test 5: A manager export his data
	 */

	@Autowired
	private ManagerService	managerService;

	@Autowired
	private Validator		validator;

	@Autowired
	private ActorService	actorService;


	//Test 1: An admin register new manager in the system
	//(Req.13.1) Create user accounts for new administrators and managers.
	@Test
	public void driver() {
		final Object testingData[][] = {
			/* Positive case */
			{
				"admin", "managerT", "managerT", "managerT", "managerT", null, "managerT@managerT.com", "ES123456789", "c/ managerT", null
			},
			/* Negative cases: invalid authentication */
			{
				"customer1", "managerT", "managerT", "managerT", "managerT", null, "managerT@managerT.com", "ES123456789", "c/ managerT", IllegalArgumentException.class
			},
			/* Negative cases: invalid data */
			{
				"manager1", "managerT", "managerT", "", "managerT", null, "managerT@managerT.com", "ES123456789", "c/ managerT", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
				(String) testingData[i][8], (Class<?>) testingData[i][9]);

	}

	protected void template(final String user, final String username, final String password, final String name, final String surname, final String photo, final String email, final String phoneNumber, final String address, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			this.register(username, password, name, surname, photo, email, phoneNumber, address);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void register(final String username, final String password, final String name, final String surname, final String photo, final String email, final String phoneNumber, final String address) {
		final RegisterFormObject form = new RegisterFormObject();
		form.setAddress(address);
		form.setEmail(email);
		form.setName(name);
		form.setPassword(password);
		form.setPassConfirmation(password);
		form.setPhoneNumber(phoneNumber);
		form.setPhoto(photo);
		form.setSurname(surname);
		form.setTermsAndConditions(true);
		form.setUsername(username);
		final BindingResult binding = new BeanPropertyBindingResult(form, form.getClass().getName());
		this.validator.validate(form, binding);
		final Manager manager = this.managerService.reconstruct(form, binding);
		Assert.isTrue(binding.hasErrors() == false);
		this.managerService.save(manager);
	}
	//	/* ######################################################################## */
	//	//Test 2: A manager display his personal information
	//(Req.9.2)Edit his or her personal data.
	@Test
	public void driver2() {
		final Object testingData[][] = {
			/* Positive case */
			{
				"manager1", "MANAGER", null
			},
			/* Negative cases: invalid authentication */
			{
				"admin", "MANAGER", IllegalArgumentException.class
			},
			/* Negative cases: invalid authentication */
			{
				"customer1", "MANAGER", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template2((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void template2(final String user, final String authority, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);

			this.display(authority);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void display(final String authority) {
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(actor, authority));

	}
	//	/* ######################################################################## */
	//	//Test 3: A manager edit his personal information
	//(Req.9.2)Edit his or her personal data.
	@Test
	public void driver3() {
		final Object testingData[][] = {
			/* Positivv ne case */
			{
				"manager1", "manager1", "manager1", "manager1", "manager1", null, "manager1@manager1.com", "ES123456789", "c/ manager1", "manager1", null
			},
			/* Negative cases: invalid authentication */
			{
				"customer1", "manager1", "manager1", "manager1", "manager1", null, "manager1@manager1.com", "ES123456789", "manager1", "manager1", IllegalArgumentException.class
			},
			/* Negative cases: invalid data */
			{
				"manager1", "manager1", "manager1", null, "manager1", null, "manager1@manager1.com", "ES123456789", "c/ manager1", "manager1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template3((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
				(String) testingData[i][8], (String) testingData[i][9], (Class<?>) testingData[i][10]);

	}

	protected void template3(final String user, final String username, final String password, final String name, final String surname, final String photo, final String email, final String phoneNumber, final String address, final String userToEdit,
		final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			final Integer id = this.getEntityId(userToEdit);
			this.authenticate(user);
			this.edit(username, password, name, surname, photo, email, phoneNumber, address, id);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}
	public void edit(final String username, final String password, final String name, final String surname, final String photo, final String email, final String phoneNumber, final String address, final Integer id) {
		final EditionFormObject form = new EditionFormObject();
		form.setId(id);
		form.setAddress(address);
		form.setEmail(email);
		form.setName(name);
		form.setPhoneNumber(phoneNumber);
		form.setPhoto(photo);
		form.setSurname(surname);
		final BindingResult binding = new BeanPropertyBindingResult(form, form.getClass().getName());
		this.validator.validate(form, binding);
		Manager manager = new Manager();
		manager = this.managerService.reconstruct(form, binding);
		Assert.isTrue(binding.hasErrors() == false);
		Assert.isTrue(manager.getId() == id);
		this.managerService.save(manager);
	}

	//	/* ######################################################################## */
	//	//Test 4: A manager delete his account
	//(Req.9.2)Edit his or her personal data.
	@Test
	public void driver4() {
		final Object testingData[][] = {

			/* Negative cases: invalid authentication */
			{
				"customer1", IllegalArgumentException.class
			},
			/* Negative cases: invalid data */
			{
				"admin", IllegalArgumentException.class
			},/* Positive case */
			{
				"manager1", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template3((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	protected void template3(final String user, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			this.delete();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);
	}
	public void delete() {
		this.managerService.delete();
	}
	//	/* ######################################################################## */
	//	//Test 5: A manager export his data
	@Test
	public void driver5() {
		final Object testingData[][] = {

			/* Negative cases: invalid authentication */
			{
				"admin", IllegalArgumentException.class
			},
			/* Negative cases: invalid authentication */
			{
				"customer1", IllegalArgumentException.class
			},/* Positive case */
			{
				"manager1", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template5((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	protected void template5(final String user, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			this.export();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);
	}
	public void export() {
		this.managerService.export();
	}
}
