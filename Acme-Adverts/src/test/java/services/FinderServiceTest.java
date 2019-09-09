
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
import domain.Finder;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FinderServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 6'7
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 1.676
	 * 
	 * ################################################################
	 * 
	 * Test 1: A customer search for packages to cached
	 * 
	 * Test 2: A customer delete his cached finder
	 */

	@Autowired
	private FinderService	finderService;


	//Test 1: A customer search for rooms to cached
	//(Req.18.1)Manage his or her finder, which involves updating the
	//search criteria, listing its con-tents, and clearing it.
	@Test
	public void finderDriver() {
		final Object testingData[][] = {
			{
				"customer1", "customer1", "billboard", 600.0, 675.0, null
			},//positive
			{
				"customer1", "admin", "billboard", 600.0, 675.0, IllegalArgumentException.class
			},//negative: invalid authenticated
			{
				"customer1", "owner1", "billboard", 600.0, 675.0, IllegalArgumentException.class
			}
		//negative: invalid access
		};

		for (int i = 0; i < testingData.length; i++)
			this.finderTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Double) testingData[i][3], (Double) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	private void finderTemplate(final String userList, final String user, final String keyWord, final Double minPrice, final Double maxPrice, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userList);
			final Finder finder = this.finderService.finderByCustomer();
			this.unauthenticate();
			this.authenticate(user);
			this.finderPackage(finder, keyWord, minPrice, maxPrice);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);

	}

	private void finderPackage(final Finder finder, final String keyWord, final Double minPrice, final Double maxPrice) {
		final Finder f = this.finderService.create();
		f.setMaxPrice(maxPrice);
		f.setId(finder.getId());
		f.setKeyWord(keyWord);
		f.setMinPrice(minPrice);
		final BindingResult binding = new BeanPropertyBindingResult(f, f.getClass().getName());
		final Finder fin = this.finderService.reconstruct(f, binding);
		Assert.isTrue(binding.hasErrors() == false);
		final Collection<domain.Package> find = this.finderService.search(fin);
		Assert.isTrue(!find.isEmpty());
	}
	//Test 2: A customer delete his cached finder
	//(Req.18.1)Manage his or her finder, which involves updating the
	//search criteria, listing its con-tents, and clearing it.
	@Test
	public void finderDeleteDriver() {
		final Object testingData[][] = {
			{
				"customer1", "customer1", null
			},//positive
			{
				"customer1", "admin", IllegalArgumentException.class
			},//negative: invalid authenticated
			{
				"customer1", "customer2", IllegalArgumentException.class
			}
		//negative: invalid data
		};

		for (int i = 0; i < testingData.length; i++)
			this.finderDeleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void finderDeleteTemplate(final String userList, final String user, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userList);
			final Finder finder = this.finderService.finderByCustomer();
			this.unauthenticate();
			this.authenticate(user);
			this.finderDeleteRoom(finder);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);

	}

	public void finderDeleteRoom(final Finder finder) {
		final BindingResult binding = new BeanPropertyBindingResult(finder, finder.getClass().getName());
		final Finder fin = this.finderService.reconstruct(finder, binding);
		Assert.isTrue(binding.hasErrors() == false);
		Assert.isTrue(finder.getCustomer().equals(fin.getCustomer()));
		this.finderService.delete(fin);
	}
}
