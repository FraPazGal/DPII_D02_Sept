
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
import domain.Customer;
import forms.EditionCustomerFormObject;
import forms.RegisterCustomerFormObject;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CustomerServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 18'6
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 4.663
	 * ################################################################
	 * 
	 * Test 1: A customer register in the system
	 * 
	 * Test 2: A customer display his personal information
	 * 
	 * Test 3: A customer edit his personal information
	 * 
	 * Test 4: A customer delete his account
	 * 
	 * Test 5: A customer export his data
	 */

	@Autowired
	private CustomerService	customerService;

	@Autowired
	private Validator		validator;

	@Autowired
	private ActorService	actorService;


	//Test 1: A customer register in the system
	//(Req.8.1)Register to the system as a customer.
	@Test
	public void driver() {
		final Object testingData[][] = {
			/* Positive case */
			{
				"customerT", "customerT", "customerT", "customerT", "ES12345678", "https://customerT", "customerT@customerT.customerT", "666666666", "c/ customerT", "customerT", "VISA", "4111111111111111", 02, 22, 123, null
			},
			/* Negative cases: invalid authentication */
			{
				"customer1", "customerT", "customerT", "customerT", "ES12345678", "https://customerT", "customerT@customerT.customerT", "666666666", "c/ customerT", "customerT", "VISA", "4111111111111111", 02, 22, 123, IllegalArgumentException.class
			},
			/* Negative cases: invalid data */
			{
				"", "customerT", "customerT", "customerT", "ES12345678", "https://customerT", "customerT@customerT.customerT", "666666666", "c/ customerT", "customerT", "VISA", "4111111111111111", 02, 22, 123, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
				(String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (Integer) testingData[i][12], (Integer) testingData[i][13], (Integer) testingData[i][14], (Class<?>) testingData[i][15]);

	}

	protected void template(final String username, final String password, final String name, final String surname, final String VAT, final String photo, final String email, final String phoneNumber, final String address, final String holder,
		final String make, final String number, final Integer expirationMonth, final Integer expirationYear, final Integer CVV, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.register(username, password, name, surname, VAT, photo, email, phoneNumber, address, holder, make, number, expirationMonth, expirationYear, CVV);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void register(final String username, final String password, final String name, final String surname, final String VAT, final String photo, final String email, final String phoneNumber, final String address, final String holder,
		final String make, final String number, final Integer expirationMonth, final Integer expirationYear, final Integer CVV) {
		final RegisterCustomerFormObject form = new RegisterCustomerFormObject();
		form.setUsername(username);
		form.setPassword(password);
		form.setPassConfirmation(password);
		form.setTermsAndConditions(true);
		form.setVat(VAT);
		form.setName(name);
		form.setSurname(surname);
		form.setPhoto(photo);
		form.setEmail(email);
		form.setPhoneNumber(phoneNumber);
		form.setAddress(address);
		form.setHolder(holder);
		form.setMake(make);
		form.setNumber(number);
		form.setExpirationMonth(expirationMonth);
		form.setExpirationYear(expirationYear);
		form.setCVV(CVV);
		final BindingResult binding = new BeanPropertyBindingResult(form, form.getClass().getName());
		this.validator.validate(form, binding);
		final Customer customer = this.customerService.reconstruct(form, binding);
		Assert.isTrue(binding.hasErrors() == false);
		this.customerService.save(customer);
	}
	//	/* ######################################################################## */
	//	//Test 2: A customer display his personal information
	//(Req.9.2)Edit his or her personal data.
	@Test
	public void driver2() {
		final Object testingData[][] = {
			/* Positive case */
			{
				"customer1", "CUSTOMER", null
			},
			/* Negative cases: invalid authentication */
			{
				"manager1", "CUSTOMER", IllegalArgumentException.class
			},
			/* Negative cases: invalid authentication */
			{
				"admin", "CUSTOMER", IllegalArgumentException.class
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
	//	//Test 3: A customer edit his personal information
	//(Req.9.2)Edit his or her personal data.
	@Test
	public void driver3() {
		final Object testingData[][] = {
			/* Positivv ne case */
			{
				"customerT", "customerT", "customerT", "customerT", "ES12345678", "https://customerT", "customerT@customerT.customerT", "666666666", "c/ customerT", "customerT", "VISA", "4111111111111111", 02, 22, 123, "customer1", "customer1", null
			},
			/* Negative cases: invalid authentication */
			{
				"customer1", "customerT", "customerT", "customerT", "ES12345678", "https://customerT", "customerT@customerT.customerT", "666666666", "c/ customerT", "customerT", "VISA", "4111111111111111", 02, 22, 123, "customer1", "manager1",
				IllegalArgumentException.class
			},
			/* Negative cases: invalid data */
			{
				"", "customerT", "customerT", "customerT", "ES12345678", "https://customerT", "customerT@customerT.customerT", "666666666", "c/ customerT", "customerT", "VISA", "4111111111111111", 02, 22, 123, "customer1", "customer1",
				IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template3((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
				(String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (Integer) testingData[i][12], (Integer) testingData[i][13], (Integer) testingData[i][14], (String) testingData[i][15],
				(String) testingData[i][16], (Class<?>) testingData[i][17]);

	}

	protected void template3(final String username, final String password, final String name, final String surname, final String VAT, final String photo, final String email, final String phoneNumber, final String address, final String holder,
		final String make, final String number, final Integer expirationMonth, final Integer expirationYear, final Integer CVV, final String userToEdit, final String user, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			final Integer id = this.getEntityId(userToEdit);
			this.authenticate(user);
			this.edit(username, password, name, surname, VAT, photo, email, phoneNumber, address, holder, make, number, expirationMonth, expirationYear, CVV, id);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void edit(final String username, final String password, final String name, final String surname, final String vAT, final String photo, final String email, final String phoneNumber, final String address, final String holder, final String make,
		final String number, final Integer expirationMonth, final Integer expirationYear, final Integer cVV, final Integer id) {
		final EditionCustomerFormObject form = new EditionCustomerFormObject();
		form.setId(id);
		form.setUsername(username);
		form.setPassword(password);
		form.setVat(vAT);
		form.setName(name);
		form.setSurname(surname);
		form.setPhoto(photo);
		form.setEmail(email);
		form.setPhoneNumber(phoneNumber);
		form.setAddress(address);
		form.setHolder(holder);
		form.setMake(make);
		form.setNumber(number);
		form.setExpirationMonth(expirationMonth);
		form.setExpirationYear(expirationYear);
		form.setCVV(cVV);
		final BindingResult binding = new BeanPropertyBindingResult(form, form.getClass().getName());
		this.validator.validate(form, binding);
		Customer customer = new Customer();
		customer = this.customerService.reconstruct(form, binding);
		Assert.isTrue(binding.hasErrors() == false);
		Assert.isTrue(customer.getId() == id);
		this.customerService.save(customer);
	}

	//	/* ######################################################################## */
	//	//Test 4: A customer delete his account
	//(Req.9.2)Edit his or her personal data.
	@Test
	public void driver4() {
		final Object testingData[][] = {

			/* Negative cases: invalid authentication */
			{
				"admin", IllegalArgumentException.class
			},
			/* Negative cases: invalid data */
			{
				"manager1", IllegalArgumentException.class
			},/* Positive case */
			{
				"customer1", null
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
		this.customerService.delete();
	}

	//	/* ######################################################################## */
	//	//Test 5: A customer export his data
	@Test
	public void driver5() {
		final Object testingData[][] = {

			/* Negative cases: invalid authentication */
			{
				"admin", IllegalArgumentException.class
			},
			/* Negative cases: invalid authentication */
			{
				"manager1", IllegalArgumentException.class
			},/* Positive case */
			{
				"customer2", null
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
		this.customerService.export();
	}

}
