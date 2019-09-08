
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
import domain.BillboardFile;
import domain.Contract;
import domain.InfoFile;
import domain.RadioFile;
import domain.SocialNetworkFile;
import domain.TVFile;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FileServiceTest extends AbstractTest {

	/*
	 * Total coverage of all tests
	 * 
	 * 
	 * Coverage of the total project (%):
	 * 
	 * 
	 * Coverage of the total project (lines of codes):
	 * 
	 * ################################################################
	 * 
	 * Total coverage by exclusively executing this test class
	 * 
	 * 
	 * Coverage of the total project (%):
	 * 
	 * 
	 * Coverage of the total project (lines of codes):
	 * 
	 * ################################################################
	 * 
	 * Test 1: A manager displays a contract, list their files and edit a file
	 * 
	 * Test 2: A manager displays a contract and creates a file
	 * 
	 * Test 3: A manager displays a contract, list their files and deletes a file
	 * 
	 * Test 4: A manager displays a contract, list their files and displays a file
	 */
	
	@Autowired
	private ContractService	contractService;
	
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


	//Test 1: A manager displays a contract, list their files and edit a file
	@Test
	public void driverEditFile() {
		final Object testingData[][] = {

			{
				"manager1", "manager2", "Text 1 for testing", "Text 2 for testing", "http://www.url1.com", "http://www.url2.com", "DRAFT", "RADIO", IllegalArgumentException.class
			},
			//Negative test case, a manager try to edit a file of another manager

			{
				"manager1", "admin", "Text 1 for testing", "Text 2 for testing", "http://www.url1.com", "http://www.url2.com", "DRAFT", "INFO", IllegalArgumentException.class
			},
			//Negative test case, an admin try to edit a file
			
			{
				"manager1", "customer1", "Text 1 for testing", "Text 2 for testing", "http://www.url1.com", "http://www.url2.com", "DRAFT", "INFO", IllegalArgumentException.class
			},
			//Negative test case, a customer try to to edit a file

			{
				"manager1", "manager1", null, "Text 2 for testing", "http://www.url1.com", "http://www.url2.com", "DRAFT", "INFO", IllegalArgumentException.class
			},
			//Negative test case, a manager try to save a tv file with a blank atribute
			
			{
				"manager1", "manager1", "Text 1 for testing", "Text 2 for testing", "not a url", "http://www.url2.com", "DRAFT", "RADIO", IllegalArgumentException.class
			},
			//Negative test case, a manager try to save a simple string as an url in a radio file

			{
				"manager1", "manager1", "Text 1 for testing", "Text 2 for testing", "http://www.url1.com", "not a url", "DRAFT", "SOCIALNETWORK", IllegalArgumentException.class
			},
			//Negative test case, a manager try to save a simple string as an url in a social network file
			
			{
				"manager1", "manager1", "Text 1 for testing", "Text 2 for testing", "http://www.url1.com", "http://www.url2.com", "FINAL", "BILLBOARD", IllegalArgumentException.class
			},
			//Negative test case, a manager try to edit a final billboard file
			
			{
				"manager1", "manager1", "Text 1 for testing", "Text 2 for testing", "http://www.url1.com", "http://www.url2.com", "FINAL", "TV", IllegalArgumentException.class
			},
			//Negative test case, a manager try to edit a final tv file
			
			{
				"manager1", "manager1", "Text 1 for testing", "Text 2 for testing", "http://www.url1.com", "http://www.url2.com", "DRAFT", "INFO", null
			},
			//Positive test case, a manager edits a info file
			
			{
				"manager1", "manager1", "Text 1 for testing", "Text 2 for testing", "http://www.url1.com", "http://www.url2.com", "DRAFT", "RADIO", null
			},
			//Positive test case, a manager edits a radio file
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], 
					(String) testingData[i][4],(String) testingData[i][5], (String) testingData[i][6], 
					(String) testingData[i][7], (Class<?>) testingData[i][8]);
		}
	}

	protected void template(final String user_1, final String user_2, final String text_1, final String text_2, 
			final String url_1, final String url_2, final String mode, final String type, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			final Contract contract = this.contractService.getContractInMode(mode);
			this.unauthenticate();
			this.authenticate(user_2);
			this.editFile(contract, text_1, text_2, url_1, url_2, type);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void editFile(Contract contract, final String text_1, final String text_2, final String url_1, final String url_2, 
			final String type) {
		
		Collection<BillboardFile> billboardFiles = this.billboardFileService.getListAllByContractToDelete(contract.getId());
		Collection<InfoFile> infoFiles = this.infoFileService.getListAllByContractToDelete(contract.getId());
		Collection<TVFile> TVFiles = this.TVFileService.getListAllByContractToDelete(contract.getId());
		Collection<RadioFile> radioFiles = this.radioFileService.getListAllByContractToDelete(contract.getId());
		Collection<SocialNetworkFile> socialNetworkFiles = this.socialNetworkFileService.getListAllByContractToDelete(contract.getId());
		
		switch (type) {
		case "BILLBOARD":
			for(BillboardFile f : billboardFiles) {
				BillboardFile b = this.billboardFileService.create(contract);
				b.setLocation(text_1);
				b.setImage(url_1);
				b.setId(f.getId());
				
				final BindingResult binding = new BeanPropertyBindingResult(b, b.getClass().getName());
				final BillboardFile bill = this.billboardFileService.reconstruct(b, binding);
				Assert.isTrue(!binding.hasErrors());
				this.billboardFileService.save(bill);
				
				break;
			}
			break;
			
		case "INFO":
			for(InfoFile f : infoFiles) {
				InfoFile i = this.infoFileService.create(contract);
				i.setTitle(text_1);
				i.setText(text_2);
				i.setId(f.getId());
				
				final BindingResult binding = new BeanPropertyBindingResult(i, i.getClass().getName());
				final InfoFile info = this.infoFileService.reconstruct(i, binding);
				Assert.isTrue(!binding.hasErrors());
				this.infoFileService.save(info);
				
				break;
			}
			break;
		
		case "TV": 
			for(TVFile f : TVFiles) {
				TVFile t = this.TVFileService.create(contract);
				t.setBroadcasterName(text_1);
				t.setSchedule(text_2);
				t.setVideo(url_1);
				t.setId(f.getId());

				final BindingResult binding = new BeanPropertyBindingResult(t, t.getClass().getName());
				final TVFile tv = this.TVFileService.reconstruct(t, binding);
				Assert.isTrue(!binding.hasErrors());
				this.TVFileService.save(tv);
				
				break;
			}
			break;
			
		case "RADIO":
			for(RadioFile f : radioFiles) {
				RadioFile r = this.radioFileService.create(contract);
				r.setBroadcasterName(text_1);
				r.setSchedule(text_2);
				r.setSound(url_1);
				r.setId(f.getId());
				
				final BindingResult binding = new BeanPropertyBindingResult(r, r.getClass().getName());
				final RadioFile radio = this.radioFileService.reconstruct(r, binding);
				Assert.isTrue(!binding.hasErrors());
				this.radioFileService.save(radio);
				
				break;
			}
			break;
			
		case "SOCIALNETWORK":
			for(SocialNetworkFile f : socialNetworkFiles) {
				SocialNetworkFile s = this.socialNetworkFileService.create(contract);
				s.setBanner(url_1);
				s.setTarget(url_2);
				s.setId(f.getId());
				
				final BindingResult binding = new BeanPropertyBindingResult(s, s.getClass().getName());
				final SocialNetworkFile social = this.socialNetworkFileService.reconstruct(s, binding);
				Assert.isTrue(!binding.hasErrors());
				this.socialNetworkFileService.save(social);
				
				break;
			}
			break;
		}
		
	}
	
	//Test 2: A manager displays a contract and creates a file
	@Test
	public void driverCreateFile() {
		final Object testingData[][] = {

			{
				"manager1", "manager2", "Text 1 for testing", "Text 2 for testing", "http://www.url1.com", "http://www.url2.com", "DRAFT", "BILLBOARD", IllegalArgumentException.class
			},
			//Negative test case, a manager try to create a file on a contract of another manager

			{
				"manager1", "manager1", "Text 1 for testing", "Text 2 for testing", "not a url", "http://www.url2.com", "DRAFT", "SOCIALNETWORK", IllegalArgumentException.class
			},
			//Negative test case, a manager try to save a simple string as an url in a social network file
			
			{
				"manager1", "manager1", "Text 1 for testing", "Text 2 for testing", "http://www.url1.com", "http://www.url2.com", "DRAFT", "RADIO", null
			},
			//Positive test case, a manager creates a radio file
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template2((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], 
					(String) testingData[i][4],(String) testingData[i][5], (String) testingData[i][6], 
					(String) testingData[i][7], (Class<?>) testingData[i][8]);
		}
	}

	protected void template2(final String user_1, final String user_2, final String text_1, final String text_2, 
			final String url_1, final String url_2, final String mode, final String type, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			final Contract contract = this.contractService.getContractInMode(mode);
			this.unauthenticate();
			this.authenticate(user_2);
			this.createFile(contract, text_1, text_2, url_1, url_2, type);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void createFile(Contract contract, final String text_1, final String text_2, final String url_1, final String url_2, 
			final String type) {
		
		switch (type) {
		case "BILLBOARD":
			BillboardFile b = this.billboardFileService.create(contract);
			b.setLocation(text_1);
			b.setImage(url_1);
			
			final BindingResult binding = new BeanPropertyBindingResult(b, b.getClass().getName());
			final BillboardFile bill = this.billboardFileService.reconstruct(b, binding);
			Assert.isTrue(!binding.hasErrors());
			this.billboardFileService.save(bill);
			
			break;
			
		case "INFO":
			InfoFile i = this.infoFileService.create(contract);
			i.setTitle(text_1);
			i.setText(text_2);
			
			final BindingResult binding2 = new BeanPropertyBindingResult(i, i.getClass().getName());
			final InfoFile info = this.infoFileService.reconstruct(i, binding2);
			Assert.isTrue(!binding2.hasErrors());
			this.infoFileService.save(info);
			
			break;
		
		case "TV": 
			TVFile t = this.TVFileService.create(contract);
			t.setBroadcasterName(text_1);
			t.setSchedule(text_2);
			t.setVideo(url_1);
			
			final BindingResult binding3 = new BeanPropertyBindingResult(t, t.getClass().getName());
			final TVFile tv = this.TVFileService.reconstruct(t, binding3);
			Assert.isTrue(!binding3.hasErrors());
			this.TVFileService.save(tv);
			
			break;
			
		case "RADIO":
			RadioFile r = this.radioFileService.create(contract);
			r.setBroadcasterName(text_1);
			r.setSchedule(text_2);
			r.setSound(url_1);
			
			final BindingResult binding4 = new BeanPropertyBindingResult(r, r.getClass().getName());
			final RadioFile radio = this.radioFileService.reconstruct(r, binding4);
			Assert.isTrue(!binding4.hasErrors());
			this.radioFileService.save(radio);
			
			break;
			
		case "SOCIALNETWORK":
			SocialNetworkFile s = this.socialNetworkFileService.create(contract);
			s.setBanner(url_1);
			s.setTarget(url_2);
			
			final BindingResult binding5 = new BeanPropertyBindingResult(s, s.getClass().getName());
			final SocialNetworkFile social = this.socialNetworkFileService.reconstruct(s, binding5);
			Assert.isTrue(!binding5.hasErrors());
			this.socialNetworkFileService.save(social);
			
			break;
		}
		
	}
	
	//Test 3: A manager displays a contract, list their files and deletes a file
	@Test
	public void driverDeleteFile() {
		final Object testingData[][] = {

			{
				"manager1", "manager2", "DRAFT", "RADIO", IllegalArgumentException.class
			},
			//Negative test case, a manager try to delete a file of another manager
			
			{
				"manager1", "manager1", "FINAL", "BILLBOARD", IllegalArgumentException.class
			},
			//Negative test case, a manager try to delete a final billboard file
			
			{
				"manager1", "manager1", "DRAFT", "RADIO", null
			},
			//Positive test case, a manager deletes a radio file
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template3((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
		}
	}

	protected void template3(final String user_1, final String user_2, final String mode, final String type, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			final Contract contract = this.contractService.getContractInMode(mode);
			this.unauthenticate();
			this.authenticate(user_2);
			this.deleteFile(contract, type);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void deleteFile(Contract contract, final String type) {
		
		Collection<BillboardFile> billboardFiles = this.billboardFileService.getListAllByContractToDelete(contract.getId());
		Collection<InfoFile> infoFiles = this.infoFileService.getListAllByContractToDelete(contract.getId());
		Collection<TVFile> TVFiles = this.TVFileService.getListAllByContractToDelete(contract.getId());
		Collection<RadioFile> radioFiles = this.radioFileService.getListAllByContractToDelete(contract.getId());
		Collection<SocialNetworkFile> socialNetworkFiles = this.socialNetworkFileService.getListAllByContractToDelete(contract.getId());
		
		switch (type) {
		case "BILLBOARD":
			for(BillboardFile f : billboardFiles) {
				this.billboardFileService.delete(f.getId());
				break;
			}
			break;
			
		case "INFO":
			for(InfoFile f : infoFiles) {
				this.infoFileService.delete(f.getId());
				break;
			}
			break;
		
		case "TV": 
			for(TVFile f : TVFiles) {
				this.TVFileService.delete(f.getId());
				break;
			}
			break;
			
		case "RADIO":
			for(RadioFile f : radioFiles) {
				this.radioFileService.delete(f.getId());
				break;
			}
			break;
			
		case "SOCIALNETWORK":
			for(SocialNetworkFile f : socialNetworkFiles) {
				this.socialNetworkFileService.delete(f.getId());
				break;
			}
			break;
		}
		
	}


	//Test 4: A manager displays a contract, list their files and displays a file
	@Test
	public void driverDisplayFile() {
		final Object testingData[][] = {

			{
				"manager1", "manager2", "DRAFT", "RADIO", IllegalArgumentException.class
			},
			//Negative test case, a manager try to display a file of another manager
			
			{
				"manager1", "customer1", "DRAFT", "INFO", IllegalArgumentException.class
			},
			//Negative test case, a customer try to display a draft info file
			
			{
				"manager1", "customer2", "FINAL", "INFO", null
			},
			//Positive test case, a customer displays a file of one of theirs contracts
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template4((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
		}
	}

	protected void template4(final String user_1, final String user_2, final String mode, final String type, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			final Contract contract = this.contractService.getContractInMode(mode);
			this.unauthenticate();
			this.authenticate(user_2);
			this.displayFile(contract, type);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void displayFile(Contract contract, final String type) {
		
		Collection<BillboardFile> billboardFiles = this.billboardFileService.getListAllByContractToDelete(contract.getId());
		Collection<InfoFile> infoFiles = this.infoFileService.getListAllByContractToDelete(contract.getId());
		Collection<TVFile> TVFiles = this.TVFileService.getListAllByContractToDelete(contract.getId());
		Collection<RadioFile> radioFiles = this.radioFileService.getListAllByContractToDelete(contract.getId());
		Collection<SocialNetworkFile> socialNetworkFiles = this.socialNetworkFileService.getListAllByContractToDelete(contract.getId());
		
		switch (type) {
		case "BILLBOARD":
			for(BillboardFile f : billboardFiles) {
				this.billboardFileService.findOneIfOwner(f.getId());
				break;
			}
			break;
			
		case "INFO":
			for(InfoFile f : infoFiles) {
				this.infoFileService.findOneIfOwner(f.getId());
				break;
			}
			break;
		
		case "TV": 
			for(TVFile f : TVFiles) {
				this.TVFileService.findOneIfOwner(f.getId());
				break;
			}
			break;
			
		case "RADIO":
			for(RadioFile f : radioFiles) {
				this.radioFileService.findOneIfOwner(f.getId());
				break;
			}
			break;
			
		case "SOCIALNETWORK":
			for(SocialNetworkFile f : socialNetworkFiles) {
				this.socialNetworkFileService.findOneIfOwner(f.getId());
				break;
			}
			break;
		}
	}
}
