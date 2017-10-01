package cz.rbe.monitor.common.selenium;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @author Radek Beran
 */
public class WebDriverSetup {

    public RemoteWebDriver loadChromeDriver(String webDriverUrl, long implicitWaitSec) {
        try {
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            ChromeOptions options = new ChromeOptions();
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            RemoteWebDriver webDriver = new RemoteWebDriver(new URL(webDriverUrl), capabilities);
            webDriver.manage().timeouts().implicitlyWait(implicitWaitSec, TimeUnit.SECONDS);
            return webDriver;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
