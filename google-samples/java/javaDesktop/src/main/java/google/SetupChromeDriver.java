/**
 *
 */
package google;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * @author khanhdo
 *
 */
public class SetupChromeDriver {
	private static WebDriver driver = null;
	// The Chrome Driver locations under the resource folder
	private static String MAC_DRIVER = "/drivers/chromedriver";
	private static String WINDOWS_DRIVER = "/drivers/chromedriver.exe";

	public WebDriver setupChromeDriver() {
		// OS type
		if (System.getProperty("os.name").contains("Mac")) {		
			File cDriver = new File(SetupChromeDriver.class.getResource(MAC_DRIVER).getFile());

			// Is it executable
			if (!cDriver.canExecute()) {
				cDriver.setExecutable(true);
			}
			System.setProperty("webdriver.chrome.driver", SetupChromeDriver.class.getResource(MAC_DRIVER).getFile());

			// Now checking for existence of Chrome executable.'
			if (!new File("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome").exists()) {
				throw new RuntimeException("Cannot find chromedriver file. Please download and copy to drivers folder in current project");
			}
		} else {
			// Make sure Chrome is installed on the default location on your machine.
			System.out.println(SetupChromeDriver.class.getResource(WINDOWS_DRIVER).getFile());
			System.setProperty("webdriver.chrome.driver", SetupChromeDriver.class.getResource(WINDOWS_DRIVER).getFile());
		}

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		options.addArguments("--ignore-certificate-errors");
		driver = new ChromeDriver(options);
		return driver;
	}
}
