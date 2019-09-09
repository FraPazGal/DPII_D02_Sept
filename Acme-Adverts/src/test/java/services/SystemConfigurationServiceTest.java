
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
import domain.Manager;
import domain.SystemConfiguration;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SystemConfigurationServiceTest extends AbstractTest {

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
	 * Test 1: Edit system configuration
	 * 
	 * Test 2: Display dashboard
	 */

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private RequestService				requestService;

	@Autowired
	private ManagerService				managerService;
	
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


	//Test 1: Edit system configuration
	@Test
	public void editDriver() {
		final Object testingData[][] = {
			{
				"admin", "admin", "systemName", "http://us.es", "+035", "makers", 10, 5, null
			},//positive
			{
				"admin", "customer", "systemName", "http://us.es", "+035", "makers", 10, 5, IllegalArgumentException.class
			},//negative: invalid authenticated
			{
				"admin", "admin", null, "http://us.es", "+35", "makers", 10, 5, IllegalArgumentException.class
			}
		//negative: invalid data
		};

		for (int i = 0; i < testingData.length; i++)
			this.editTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Integer) testingData[i][6], (Integer) testingData[i][7],
				(Class<?>) testingData[i][8]);
	}

	private void editTemplate(final String userList, final String user, final String systemName, final String banner, final String countryCode, final String makers, final Integer time, final Integer max, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userList);
			final SystemConfiguration sys = this.systemConfigurationService.findMySystemConfiguration();
			this.unauthenticate();
			this.authenticate(user);
			this.edit(sys, systemName, banner, countryCode, makers, time, max);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);

	}

	public void edit(final SystemConfiguration sys, final String systemName, final String banner, final String countryCode, final String makers, final Integer time, final Integer max) {
		final SystemConfiguration s = new SystemConfiguration();
		s.setBanner(banner);
		s.setBreachNotification(sys.getBreachNotification());
		s.setCountryCode(countryCode);
		s.setId(sys.getId());
		s.setMakers(makers);
		s.setMaxResults(max);
		s.setSystemName(systemName);
		s.setTimeResultsCached(time);
		s.setWelcomeMessage(sys.getWelcomeMessage());
		final BindingResult binding = new BeanPropertyBindingResult(s, s.getClass().getName());
		final String wes = sys.getWelcomeMessage().get("SP");
		final String wen = sys.getWelcomeMessage().get("EN");
		final SystemConfiguration system = this.systemConfigurationService.reconstruct(s, wes, wen, "", "", binding);
		Assert.isTrue(binding.hasErrors() == false);
		this.systemConfigurationService.save(system);
	}
	//Test 2: Display dashboard
	//(Req.12.2 and Req.20.1) Display a dashboard with the following information
	// Req. 3.1 of Acme-Adverts
	@Test
	public void dashboardDriver() {
		final Object testingData[][] = {

			{
				"customer1", IllegalArgumentException.class
			},//negative: invalid authenticated
			{
				"owner1", IllegalArgumentException.class
			},//negative: ban a not spammer user
			{
				"admin", null
			}
		//positive
		};

		for (int i = 0; i < testingData.length; i++)
			this.dashboardTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void dashboardTemplate(final String userList, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userList);
			this.dashboardActor();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);

	}

	@SuppressWarnings("unused")
	public void dashboardActor() {
		// Files per contract
		Double [] statsBillboardFilesPerContract = this.billboardFileService.StatsBillboardFilesPerContract();
		Double [] statsRadioFilesPerContract = this.radioFileService.StatsRadioFilesPerContract();
		Double [] statsSNFilesPerContract = this.socialNetworkFileService.StatsSocialNetworkFilesPerContract();
		Double [] statsInfoFilesPerContract = this.infoFileService.StatsInfoFilesPerContract();
		Double [] statsTVFilesPerContract = this.TVFileService.StatsTVFilesPerContract();
		Double [] statsFilesPerContract = this.billboardFileService.StatsFilesPerContract();
		
		final Integer maxRequestPerManager = this.requestService.MaxRequestPerManager();
		final Integer minRequestPerManager = this.requestService.MinRequestPerManager();
		final Double avgRequestPerManager = this.requestService.AvgRequestPerManager();
		final Double sttdevRequestPerManager = this.requestService.stdevRequestPerManager();

		//request pending per manager
		final Integer maxRequestPerManagerPending = this.requestService.MaxRequestPerManagerPending();
		final Integer minRequestPerManagerPending = this.requestService.MinRequestPerManagerPending();
		final Double avgRequestPerManagerPending = this.requestService.AvgRequestPerManagerPending();
		final Double sttdevRequestPerManagerPending = this.requestService.stdevRequestPerManagerPending();

		//request per customer
		final Integer maxRequestPerCustomer = this.requestService.MaxRequestPerCustomer();
		final Integer minRequestPerCustomer = this.requestService.MinRequestPerCustomer();
		final Double avgRequestPerCustomer = this.requestService.AvgRequestPerCustomer();
		final Double sttdevRequestPerCustomer = this.requestService.stdevRequestPerCustomer();

		//request pending per customer
		final Integer maxRequestPerCustomerPending = this.requestService.MaxRequestPerCustomerPending();
		final Integer minRequestPerCustomerPending = this.requestService.MinRequestPerCustomerPending();
		final Double avgRequestPerCustomerPending = this.requestService.AvgRequestPerCustomerPending();
		final Double sttdevRequestPerCustomerPending = this.requestService.stdevRequestPerCustomerPending();

		// Top 10 managers
		final Collection<Manager> managers = this.managerService.top10();
	}

}
