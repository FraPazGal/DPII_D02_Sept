
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
import domain.Administrator;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AdministratorServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 8'7
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 2.187
	 * ################################################################
	 * 
	 * Test 1: An admin register new admin in the system
	 * 
	 * Test 2: An admin display his personal information
	 * 
	 * Test 3: An admin edit his personal information
	 * 
	 * Test 4: An admin delete his account
	 * 
	 * Test 5: An admin export his data
	 */

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private Validator				validator;

	@Autowired
	private ActorService			actorService;


	//Test 1: An admin register new admins in the system
	//(Req.9.2)Edit his or her personal data.
	@Test
	public void driver() {
		final Object testingData[][] = {
			/* Positive case */
			{
				"admin", "adminT", "adminT", "adminT", "adminT", null, "adminT@", "ES123456789", "c/ adminT", null
			},
			/* Negative cases: invalid authentication */
			{
				"customer1", "adminT", "adminT", "adminT", "adminT", null, "adminT@", "ES123456789", "c/ adminT", IllegalArgumentException.class
			},
			/* Negative cases: invalid data */
			{
				"admin", "adminT", "adminT", "", "adminT", null, "adminT@", "ES123456789", "c/ adminT", IllegalArgumentException.class
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
		final Administrator admin = this.administratorService.reconstruct(form, binding);

		Assert.isTrue(binding.hasErrors() == false);
		this.administratorService.save(admin);
	}
	//	/* ######################################################################## */
	//	//Test 2: An admin display his personal information
	//(Req.9.2)Edit his or her personal data.
	@Test
	public void driver2() {
		final Object testingData[][] = {
			/* Positive case */
			{
				"admin", "ADMIN", null
			},
			/* Negative cases: invalid authentication */
			{
				"manager1", "ADMIN", IllegalArgumentException.class
			},
			/* Negative cases: invalid authentication */
			{
				"customer1", "ADMIN", IllegalArgumentException.class
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
	//	//Test 3: An admin edit his personal information
	//(Req.9.2)Edit his or her personal data.
	@Test
	public void driver3() {
		final Object testingData[][] = {
			/* Positivv ne case */
			{
				"admin", "adminT", "adminT", "adminT", "adminT", null, "adminT@", "ES123456789", "c/ adminT", "administrator1", null
			},
			/* Negative cases: invalid authentication */
			{
				"admin", "adminT", "adminT", "adminT", "adminT", null, "adminT@", "ES123456789", "c/ adminT", "customer1", IllegalArgumentException.class
			},
			/* Negative cases: invalid data */
			{
				"admin", "adminT", "adminT", null, "adminT", null, "adminT@", "ES123456789", "c/ adminT", "administrator1", IllegalArgumentException.class
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
		Administrator admin = new Administrator();
		admin = this.administratorService.reconstruct(form, binding);
		Assert.isTrue(binding.hasErrors() == false);
		Assert.isTrue(admin.getId() == id);
		this.administratorService.save(admin);
	}

	//	/* ######################################################################## */
	//	//Test 4: An admin delete his account
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
				"manager1", IllegalArgumentException.class
			},/* Positive case */
			{
				"admin", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template4((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	protected void template4(final String user, final Class<?> expected) {
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
		this.administratorService.delete();
	}

	//	/* ######################################################################## */
	//	//Test 4: An admin export his data
	@Test
	public void driver5() {
		final Object testingData[][] = {

			/* Negative cases: invalid authentication */
			{
				"customer1", IllegalArgumentException.class
			},
			/* Negative cases: invalid authentication */
			{
				"manager1", IllegalArgumentException.class
			},/* Positive case */
			{
				"admin", null
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
		this.administratorService.export();
	}

}
