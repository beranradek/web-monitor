package cz.rbe.monitor.probe.impl;

import cz.rbe.monitor.common.selenium.ByAttribute;
import cz.rbe.monitor.common.selenium.SeleniumSupport;
import cz.rbe.monitor.probe.Probe;
import cz.rbe.monitor.probe.ProbeResult;
import cz.rbe.monitor.probe.ResultSeverity;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Optional;

/**
 * Checks if product is playing and ensures all actions necessary to get to secured product page.
 * @author Radek Beran
 */
public class ProductPlayingProbe implements Probe, SeleniumSupport {

    private final WebDriver webDriver;
    private final String user;
    private final String password;
    private final String deviceIdsCookieValue;
    private final String homepageUrl;
    private final String productUrl;

    public ProductPlayingProbe(WebDriver webDriver, String user, String password, String deviceIdsCookieValue, String homepageUrl, String productUrl) {
        this.webDriver = webDriver;
        this.user = user;
        this.password = password;
        this.deviceIdsCookieValue = deviceIdsCookieValue;
        this.homepageUrl = homepageUrl;
        this.productUrl = productUrl;
    }

    @Override
    public String getName() {
        return "Product playing check";
    }

    @Override
    public ProbeResult run() {
        ProbeResult result = new ProbeResult(getName());
        maximizeWindow();
        webDriver.get(homepageUrl);

        setDeviceIdsCookie();
        ensureGeoIpMismatchConfirmed();
        ensureCookiesConfirmed();
        ensureUserLoggedIn();

        // Previous actions can take some time
        waitForMs(5000);

        // Go to product page
        webDriver.get(productUrl);

        waitUntilPlayerIsPresent();
        if (playerIsPlaying()) {
            result.setMessage(Probe.MSG_OK);
            result.setSeverity(ResultSeverity.INFO);
        } else {
            result.setMessage("Player is not playing on " + productUrl);
            result.setSeverity(ResultSeverity.ERROR);
        }

        // Go to homepage so the player does not bother us with sound
        webDriver.get(homepageUrl);
        return result;
    }

    private void waitUntilPlayerIsPresent() {
        new WebDriverWait(webDriver, 10)
            .until(ExpectedConditions.presenceOfElementLocated(By.id("ott-video-player")));
    }

    private void setDeviceIdsCookie() {
        Cookie ck = new Cookie("ott_dids", deviceIdsCookieValue);
        webDriver.manage().addCookie(ck);
    }

    private void ensureCookiesConfirmed() {
        try {
            // See http://docs.seleniumhq.org/docs/04_webdriver_advanced.jsp#explicit-and-implicit-waits
            WebElement allowCookiesBtn = new WebDriverWait(webDriver, 7)
                .until(ExpectedConditions.elementToBeClickable(By.id("cookies-info-ok")));
            if (allowCookiesBtn != null && allowCookiesBtn.isDisplayed()) {
                allowCookiesBtn.click();
            }
        } catch (Exception ex) {
            getLogger().warn("Cookies already confirmed:" + ex.getMessage());
        }
    }

    private boolean playerIsPlaying() {
        return findFirstElement(By.className("vjs-playing")).isPresent();
    }

    private void ensureUserLoggedIn() {
        Optional<WebElement> loginMenuElem = findFirstElement(By.className("login"));
        if (loginMenuElem.isPresent()) {
            // Click on login menu (pull it out)
            loginMenuElem.get().click();
            Optional<WebElement> emailElem = findFirstElement(By.id("nav_email"));
            if (emailElem.isPresent()) {
                Optional<WebElement> passElem = findFirstElement(By.id("nav_password"));
                if (passElem.isPresent()) {
                    // fill login data
                    emailElem.get().sendKeys(user);
                    passElem.get().sendKeys(password);
                    // Click on login button
                    findFirstElement(new ByAttribute("data-jnp", "user.login.button")).ifPresent(elem -> elem.click());
                }
            }
        }
    }

    private void ensureGeoIpMismatchConfirmed() {
        Optional<WebElement> countryMismatchElem = findFirstElement(new ByAttribute("data-jnp", "geoip.mismatch.inCountry"));
        if (countryMismatchElem.isPresent()) {
            countryMismatchElem.get().click();
            findFirstElement(new ByAttribute("data-jnp", "g.LetMeBrowse")).ifPresent(elem -> elem.click());
        }
    }

    @Override
    public WebDriver getWebDriver() {
        return webDriver;
    }
}