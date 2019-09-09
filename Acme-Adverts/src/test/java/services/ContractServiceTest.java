
package services;

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
import domain.Contract;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ContractServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 6'3
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 1.574
	 * 
	 * ################################################################
	 * 
	 * Test 1: A manager display a contract
	 * 
	 * Test 2: A manager edit a contract
	 * 
	 * Test 3: A manager/customer sign a contract
	 */
	@Autowired
	private ContractService	contractService;

	//Test 1: A manager display a contract
	//(Req.11.3) Manage his or her contracts, which includes listing, editing, and signing 
	//them. When a request is accepted, a draft contract is created automatically; such draft 
	//only con-tains a piece of text that is generated automatically from the corresponding 
	//offer and an empty file. Editing a contract means allowing the manager to change the 
	//text and to fill the corresponding file. Signing a contract means that the contract is 
	//saved in final mode so that it cannot be further edited; the system must then comçpute
	//the hash automatically. Once a contract is signed by a manager, it must be-come immediately 
	//available to the corresponding customer.
	@Test
	public void driverDisplayAudit() {
		final Object testingData[][] = {

			{
				"manager1", "manager2", IllegalArgumentException.class
			},
			//Negative test case,  a manager try display the contract from another manager
			{
				"manager1", "administrator1", IllegalArgumentException.class
			},
			//Negative test case invalid authentication
			{
				"manager1", "manager1", null
			},
		//Positive test case
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void template(final String userList, final String user, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(userList);
			final Collection<Contract> contracts = this.contractService.getListAll();
			this.unauthenticate();
			this.authenticate(user);
			this.display(contracts);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void display(final Collection<Contract> contracts) {
		for (final Contract c : contracts)
			if (c.getSignedManager() == null) {
				this.contractService.findOneByRequestAndOwner(c.getRequest().getId());
				break;
			}
	}

	//	Test 2: A manager edit a contract
	//(Req.11.3) Manage his or her contracts, which includes listing, editing, and signing 
	//them. When a request is accepted, a draft contract is created automatically; such draft 
	//only con-tains a piece of text that is generated automatically from the corresponding 
	//offer and an empty file. Editing a contract means allowing the manager to change the 
	//text and to fill the corresponding file. Signing a contract means that the contract is 
	//saved in final mode so that it cannot be further edited; the system must then comçpute
	//the hash automatically. Once a contract is signed by a manager, it must be-come immediately 
	//available to the corresponding customer.
	@Test
	public void driverEditAudit() {
		final Object testingData[][] = {

			{
				"manager1", "manager1", null, IllegalArgumentException.class
			},
			//Negative test case,  a manager try edit a contract with invalid text
			{
				"manager1", "administrator1", "TEST", IllegalArgumentException.class
			},
			//Negative test case, invalid authentication
			{
				"manager1", "manager1", "TEST", null
			},
		//Positive test case save
		};

		for (int i = 0; i < testingData.length; i++)
			this.template2((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	protected void template2(final String userList, final String user, final String text, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(userList);
			final Collection<Contract> contracts = this.contractService.getListAll();
			this.unauthenticate();
			this.authenticate(user);
			this.edit(contracts, text);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void edit(final Collection<Contract> contracts, final String text) {
		for (final Contract c : contracts)
			if (c.getSignedManager() == null) {
				final Contract con = this.contractService.create(c.getRequest());
				con.setId(c.getId());
				con.setText(text);
				final BindingResult binding = new BeanPropertyBindingResult(con, con.getClass().getName());
				final Contract co = this.contractService.reconstruct(con, binding);
				Assert.isTrue(binding.hasErrors() == false);
				this.contractService.save(co);
				break;
			}
	}

	//	//Test 3: A manager/customer sign a contract
	//(Req.10.3) Sign a contract that is pending.
	//(Req.11.3) Manage his or her contracts, which includes listing, editing, and signing 
	//them. When a request is accepted, a draft contract is created automatically; such draft 
	//only con-tains a piece of text that is generated automatically from the corresponding 
	//offer and an empty file. Editing a contract means allowing the manager to change the 
	//text and to fill the corresponding file. Signing a contract means that the contract is 
	//saved in final mode so that it cannot be further edited; the system must then comçpute
	//the hash automatically. Once a contract is signed by a manager, it must be-come immediately 
	//available to the corresponding customer.
	@Test
	public void driverSignAudit() {
		final Object testingData[][] = {
			{
				"manager1", "customer1", IllegalArgumentException.class
			},
			//Negative test case, the customer cant sign

			{
				"manager1", "manager2", IllegalArgumentException.class
			},
			//Negative test case, invalid authentication
			{
				"manager1", "manager1", null
			}
		//Positive test case
		};

		for (int i = 0; i < testingData.length; i++)
			this.template3((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void template3(final String userList, final String userFirm, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(userList);
			final Collection<Contract> contracts = this.contractService.getListAll();
			this.unauthenticate();
			this.authenticate(userFirm);
			this.sign(contracts);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void sign(final Collection<Contract> contracts) {
		for (final Contract c : contracts)
			if (c.getSignedManager() == null) {
				this.contractService.sign(c.getId());
				break;
			}
	}
}
