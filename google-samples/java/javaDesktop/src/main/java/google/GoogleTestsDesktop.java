package google;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class GoogleTestsDesktop {
	private RemoteWebDriver driver = null;

	final static String GOOGLE_URL = "https://mail.google.com";
	final static String GOOGLE_URL_HOMEPAGE = "https://mail.google.com/mail/u/0/#inbox";
	final static String TRASH_URL = "https://mail.google.com/mail/u/0/#trash";
	final static String SENT_URL = "https://mail.google.com/mail/u/0/#sent";

	static String EMAIL_ADDRESS1 = "dinhducthanh91@gmail.com";
	static String EMAIL_ADDRESS2 = "jslucifer.photography@gmail.com";
	static String PASSWORDEMAILSEND = "ngoctrinh";
	static String PASSWORDEMAILTO = "DucThanh1991";
	final static String SUBJECT1 = "It is a subject";
	final static String BODY1 = "It is a body";
	final static String MSG_NOEMAIL = "The conversation has been deleted.";
	final static String MSG_NOEMAIL_PRIMARY = "You have no mail.\nPlease enjoy your day!";
	String getSubject;
	String getBody;
	String getSentEmail;
	String getToEmail;

	@Parameters({ "browserName", "emailSend", "passwordEmailSend", "emailTo", "passwordEmailTo" })
	@BeforeTest
	public void Setup(String browserName, String emailSend, String passwordEmailSend, String emailTo,
			String passwordEmailTo) {

		EMAIL_ADDRESS1 = emailSend;
		EMAIL_ADDRESS2 = emailTo;
		PASSWORDEMAILSEND = passwordEmailSend;
		PASSWORDEMAILTO = passwordEmailTo;
		SetupDriver setup = new SetupDriver();

		if (browserName.toLowerCase().equals("chrome")) {
			driver = (ChromeDriver) setup.setupChromeDriver();
		} else if (browserName.toLowerCase().equals("ie") || browserName.toLowerCase().equals("internetexplorer")) {
			driver = (InternetExplorerDriver) setup.setupIEDriver();
		} else {
			driver = (FirefoxDriver) setup.setupFirefoxDriver();
		}

		driver.manage().deleteAllCookies();
		driver.navigate().refresh();
	}

	@AfterTest
	public void Teardown() {
		try {
			if (driver != null)
				driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(priority = 1, description = "should not accept empty email or invalid email")
	public void GoogleLogin_Invalid() {
		driver.get(GOOGLE_URL);
		Sleep(5000);
		driver.findElementById("next").click();
		Sleep(2000);
		String errorMsg = driver.findElementById("errormsg_0_Email").getText();
		Assert.assertEquals(errorMsg, "Please enter your email.");
		driver.findElementById("Email").sendKeys("invalid_email@where.about");
		driver.findElementById("next").click();
		Sleep(2000);
		String errorMsg2 = driver.findElementById("errormsg_0_Email").getText();
		Assert.assertEquals(errorMsg2, "Sorry, Google doesn't recognize that email.");
	}

	@Test(priority = 2, description = "should accept valid credentials")
	public void GoogleLogin_Valid() {
		Login(EMAIL_ADDRESS1, PASSWORDEMAILSEND);
		Sleep(5000);
		String getURL = driver.getCurrentUrl();
		Assert.assertEquals(true, getURL.contains("https://mail.google.com/mail"));
	}

	@Test(priority = 3, description = "should compose email successfully by Gmail1")
	public void GoogleComposeEmail() {
		// should compose email successfully by Gmail1
		driver.findElementByXPath("//div[text()='COMPOSE']").click();
		Sleep(1000);
		driver.findElementByXPath("//textarea[@aria-label='To']").sendKeys(EMAIL_ADDRESS2);
		driver.findElementByXPath("//input[@name='subjectbox']").sendKeys(SUBJECT1);
		driver.findElementByXPath("//div[@aria-label='Message Body']").sendKeys(BODY1);
		Sleep(1000);
		driver.findElementByXPath("//div[text()='Send']").click();
		Sleep(1000);
		driver.get(SENT_URL);
		Sleep(5000);

		// verify the new email exists on Sent Email folder on Gmail1
		String getText = driver.findElementByXPath("//div[@class='aeF']").getText();
		Assert.assertEquals(true, getText.contains(SUBJECT1));
		Assert.assertEquals(true, getText.contains(BODY1));
		driver.findElementByXPath("//div[@class='BltHke nH oy8Mbf' and @role='main']//div[@class='xT']").click();
		Sleep(3000);
		getSubject = driver.findElementByXPath("//h2[@class='hP']").getText();
		getBody = driver.findElementByXPath("//div[@class='a3s']").getText();
		getSentEmail = driver.findElementByXPath("//h3[@class='iw']/span").getAttribute("email");
		getToEmail = driver.findElementByXPath("//div[@class='iw ajw']/span/span").getAttribute("email");

		Assert.assertEquals(SUBJECT1, getSubject);
		Assert.assertEquals(BODY1, getBody);
		Assert.assertEquals(EMAIL_ADDRESS1, getSentEmail);
		Assert.assertEquals(EMAIL_ADDRESS2, getToEmail);

		// delete email on Sent Email and Trash folder on Gmail1
		Delete_Sent_Msg();
		Delete_Trash_Msg();

		// Sign out
		driver.findElementByXPath("//span[@class='gb_Za gbii']").click();
		Sleep(1000);
		driver.findElementByXPath("//a[@id='gb_71' and text()='Sign out']").click();
		Sleep(5000);

		driver.get(GOOGLE_URL);
		driver.manage().deleteAllCookies();
		driver.navigate().refresh();

		// verify Gmail2 received a new email from Gmail1
		Login(EMAIL_ADDRESS2, PASSWORDEMAILTO);
		driver.get(GOOGLE_URL_HOMEPAGE);
		driver.navigate().refresh();
		Sleep(5000);

		getText = driver.findElementByXPath("//div[@class='xT']").getText();
		Assert.assertEquals(true, getText.contains(SUBJECT1));
		Assert.assertEquals(true, getText.contains(BODY1));

		driver.findElementByXPath("//div[@class='xT'][1]").click();
		Sleep(2000);
		getSubject = driver.findElementByXPath("//div[@class='ha']/h2").getText();
		getBody = driver.findElementByXPath("//div[@class='gs']/div[7]/div/div[1]").getText();
		getSentEmail = driver.findElementByXPath("//h3[@class='iw']/span[1]").getAttribute("email");
		driver.findElementByXPath("//div[@class='ajy']").click();
		Sleep(2000);
		getToEmail = driver.findElementByXPath("//table[@class='ajC']/tbody/tr[2]/td[2]/span/span").getAttribute("email");

		Assert.assertEquals(SUBJECT1, getSubject);
		Assert.assertEquals(BODY1, getBody);
		Assert.assertEquals(EMAIL_ADDRESS1, getSentEmail);
		Assert.assertEquals(EMAIL_ADDRESS2, getToEmail);

		// delete email on Primary Email and Trash folder on Gmail2
		Delete_Inbox_Msg();
		Delete_Trash1_Msg();
	}

	private void Login(String username, String password) {
		driver.get(GOOGLE_URL);
		Sleep(5000);
		driver.findElementById("Email").sendKeys(username);
		driver.findElementById("next").click();
		Sleep(1000);
		driver.findElementById("Passwd").sendKeys(password);
		driver.findElementById("signIn").click();
		Sleep(10000);
	}

	private void Delete_Sent_Msg() {
		driver.get(SENT_URL);
		Sleep(3000);
		driver.findElementByXPath("//div[contains(@class,'oZ-jc T-Jo J-J5-Ji') and @role='checkbox'][1]").click();
		Sleep(4000);
		driver.findElementByXPath(
				".//*[@id=':5']/div[2]/div[1]/div[1]/div/div/div[2]/div[3]")
				.click();
		Sleep(3000);
		driver.findElementByXPath("//button[@name='ok' and text()='OK']").click();
		Sleep(2000);
		String getNoEmailMsg = driver.findElementByXPath("//div[@id=':2']").getText();

		Assert.assertEquals(true, getNoEmailMsg.contains("No sent messages!"));
	}

	private void executeScript(String script) {
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript(script);
		}
	}

	private void Delete_Trash_Msg() {
		driver.get(TRASH_URL);
		Sleep(5000);
		driver.findElementByXPath("//div[contains(@class,'oZ-jc T-Jo J-J5-Ji') and @role='checkbox'][1]").click();
		Sleep(2000);
		driver.findElementByXPath(".//*[@id=':5']/div[3]/div[1]/div[1]/div/div/div[2]/div").click();
		Sleep(2000);
		String getNoEmailMsg = driver.findElementByXPath("html/body/div[7]/div[3]/div/div[1]/div[5]/div[1]/div[2]/div[3]/div/div/div[2]/span").getText();

		Assert.assertEquals(MSG_NOEMAIL, getNoEmailMsg);
	}
	
	private void Delete_Trash1_Msg() {
		driver.get(TRASH_URL);
		Sleep(5000);
		driver.findElementByXPath("//div[contains(@class,'oZ-jc T-Jo J-J5-Ji') and @role='checkbox'][1]").click();
		Sleep(2000);
		driver.findElementByXPath(".//*[@id=':5']/div[2]/div[1]/div[1]/div/div/div[2]/div").click();
		Sleep(2000);
		String getNoEmailMsg = driver.findElementByXPath("html/body/div[7]/div[3]/div/div[1]/div[5]/div[1]/div[2]/div[3]/div/div/div[2]/span").getText();

		Assert.assertEquals(MSG_NOEMAIL, getNoEmailMsg);
	}

	private void Delete_Inbox_Msg() {
		driver.get(GOOGLE_URL_HOMEPAGE);
		Sleep(5000);
		driver.findElementByXPath("//div[@id=':3j'][1]").click();
		Sleep(1000);
		driver.findElementByXPath(".//*[@id=':5']/div/div[1]/div[1]/div/div/div[2]/div[3]").click();

		String getNoEmailMsg = driver.findElementByXPath("//div[@class='aRv']").getText();

		Assert.assertEquals(true, getNoEmailMsg.contains("Your Primary tab is empty."));
	}

	private void Sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
