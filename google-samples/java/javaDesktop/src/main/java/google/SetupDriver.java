/**
 * 
 */
package google;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * @author khanhdo
 *
 */
public class SetupDriver {
	private static WebDriver driver = null;
	// The Chrome Driver locations under the resource folder
	private static String MAC_CHROME_DRIVER = "/drivers/chromedriver";
	private static String WINDOWS_CHROME_DRIVER = "/drivers/chromedriver.exe";
	private static String WINDOWS_IE_DRIVER = "/drivers/IEDriverServer.exe";
	
	public WebDriver setupChromeDriver() {
		// OS type
		if (System.getProperty("os.name").contains("Mac")) {		
			File cDriver = new File(SetupDriver.class.getResource(MAC_CHROME_DRIVER).getFile());

			// Is it executable
			if (!cDriver.canExecute()) {
				cDriver.setExecutable(true);
			}
			System.setProperty("webdriver.chrome.driver", SetupDriver.class.getResource(MAC_CHROME_DRIVER).getFile());

			// Now checking for existence of Chrome executable.'
			if (!new File("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome").exists()) {
				throw new RuntimeException("Cannot find chromedriver file. Please download and copy to drivers folder in current project");
			}
		} else {
			// Make sure Chrome is installed on the default location on your machine.
			System.out.println(SetupDriver.class.getResource(WINDOWS_CHROME_DRIVER).getFile());
			System.setProperty("webdriver.chrome.driver", SetupDriver.class.getResource(WINDOWS_CHROME_DRIVER).getFile());
		}

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		options.addArguments("--ignore-certificate-errors");
		driver = new ChromeDriver(options);
		return driver;
	}
	
	public WebDriver setupFirefoxDriver() {
		 WebDriver driver = new FirefoxDriver();
		return driver;
	}
	
	public WebDriver setupIEDriver() {
		System.out.println(SetupDriver.class.getResource(WINDOWS_IE_DRIVER).getFile());
		System.setProperty("webdriver.ie.driver", SetupDriver.class.getResource(WINDOWS_IE_DRIVER).getFile());
		 WebDriver driver = new InternetExplorerDriver();
		return driver;
	}
}
