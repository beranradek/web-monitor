package cz.rbe.monitor.probe.impl;

import cz.rbe.monitor.common.selenium.SeleniumSupport;
import cz.rbe.monitor.probe.Probe;
import cz.rbe.monitor.probe.ProbeResult;
import cz.rbe.monitor.probe.ResultSeverity;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Checks if number of tickets is still as expected.
 * @author Radek Beran
 */
public class NewTicketProbe implements Probe, SeleniumSupport {

    private final WebDriver webDriver;
    private final int currentNumberOfTickets;
    private final String user;
    private final String password;
    private final String loginUrl;
    private final String ticketListUrl;

    public NewTicketProbe(WebDriver webDriver, String user, String password, int currentNumberOfTickets, String loginUrl, String ticketListUrl) {
        this.user = user;
        this.password = password;
        this.currentNumberOfTickets = currentNumberOfTickets;
        this.webDriver = webDriver;
        this.loginUrl = loginUrl;
        this.ticketListUrl = ticketListUrl;
    }

    @Override
    public String getName() {
        return "New ticket check";
    }

    @Override
    public ProbeResult run() {
        ProbeResult result = new ProbeResult(getName());

        maximizeWindow();
        webDriver.get(loginUrl);
        webDriver.findElement(By.id("frm-signInForm-username")).sendKeys(user);
        webDriver.findElement(By.id("frm-signInForm-password")).sendKeys(password);
        webDriver.findElement(By.name("send")).click();
        webDriver.get(ticketListUrl);

        String textToFind = currentNumberOfTickets + " rows";

        List<WebElement> list = findElementsWithText(textToFind);
        if (list != null && list.size() > 0) {
            result.setMessage(Probe.MSG_OK);
            result.setSeverity(ResultSeverity.INFO);
        } else {
            result.setMessage("Ticketing does not contain " + currentNumberOfTickets + " tickets");
            result.setSeverity(ResultSeverity.ERROR);
        }
        return result;
    }

    @Override
    public WebDriver getWebDriver() {
        return webDriver;
    }
}
