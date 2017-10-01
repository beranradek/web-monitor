package cz.rbe.monitor.common.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByXPath;

import java.io.Serializable;
import java.util.List;

/**
 * Location of element by attribute name and value.
 * @author Radek Beran
 */
public class ByAttribute extends By implements Serializable {

    private static final long serialVersionUID = 5341968046120372169L;

    private final String attrName;
    private final String attrValue;

    public ByAttribute(String attrName, String attrValue) {
        this.attrName = attrName;
        this.attrValue = attrValue;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        return ((FindsByXPath) context).findElementsByXPath(".//*[@" + attrName + " = '" + attrValue + "']");
    }

    @Override
    public WebElement findElement(SearchContext context) {
        return ((FindsByXPath) context).findElementByXPath(".//*[@" + attrName + " = '" + attrValue + "']");
    }

    @Override
    public String toString() {
        return "ByAttributeValue: " + attrName + "='" + attrValue + "'";
    }
}
