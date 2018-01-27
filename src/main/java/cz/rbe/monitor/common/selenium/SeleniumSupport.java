package cz.rbe.monitor.common.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Auxiliary methods for Selenium.
 * @author Radek Beran
 */
public interface SeleniumSupport {

    default Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    WebDriver getWebDriver();

    default List<WebElement> findElementsWithText(String text) {
        return getWebDriver().findElements(By.xpath("//*[contains(text(),'" + text + "')]"));
    }

    default Optional<WebElement> findFirstElement(By by) {
        Optional<WebElement> elem;
        List<WebElement> elements = getWebDriver().findElements(by);
        if (elements.isEmpty()) {
            elem = Optional.empty();
        } else {
            elem = Optional.ofNullable(elements.get(0));
        }
        return elem;
    }

    default void waitForMs(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    default void maximizeWindow() {
        try {
            getWebDriver().manage().window().maximize();
        } catch (Exception ex) {
            // ignored,
            // e.g.: WebDriverException: unknown error: failed to change window state to normal, current state is maximized
            // e.g.: WebDriverException: disconnected: unable to connect to renderer
            getLogger().warn("Cannot maximize window");
        }
    }

    default boolean isElementPresent(By by) {
        return getWebDriver().findElements(by).size() > 0;
    }
}
