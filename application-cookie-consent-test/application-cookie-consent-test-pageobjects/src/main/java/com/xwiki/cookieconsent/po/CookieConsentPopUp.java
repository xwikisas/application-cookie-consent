/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.xwiki.cookieconsent.po;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.xwiki.test.ui.po.BaseElement;

/**
 * Page Object for the Cookie Consent accept/decline/configure popup.
 *
 * @version $Id$
 * @since 2.0
 */
public class CookieConsentPopUp extends BaseElement
{
    private static final By POPUP = By.id("cookieConsent");

    private static final By CONTENT = By.id("cookieConsentContent");

    private static final By ACCEPT_ALL_BUTTON = By.id("cookieConsentHide");

    private static final By REJECT_ALL_BUTTON = By.id("cookieConsentRejectAll");

    private static final By CONFIGURE_BUTTON = By.id("cookieConsentConfig");

    private static final By OK_BUTTON = By.id("cookieConsentOK");

    private static final By COOKIE_SELECT_PANE = By.id("CookieSelectPane");

    private static final By CONFIG_JSON = By.id("cookie-consent-config");

    public boolean isDisplayed()
    {
        List<WebElement> elements = getDriver().findElementsWithoutWaiting(POPUP);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    public String getDisclaimerText()
    {
        return getDriver().findElementWithoutWaiting(CONTENT).getText();
    }

    public String getAcceptAllButtonText()
    {
        return getDriver().findElementWithoutWaiting(ACCEPT_ALL_BUTTON).getText();
    }

    public String getRejectButtonText()
    {
        return getDriver().findElementWithoutWaiting(REJECT_ALL_BUTTON).getText();
    }

    public String getConfigureButtonText()
    {
        return getDriver().findElementWithoutWaiting(CONFIGURE_BUTTON).getText();
    }

    public void clickAcceptAll()
    {
        jsClick(getDriver().findElementWithoutWaiting(ACCEPT_ALL_BUTTON));
        getDriver().waitUntilElementDisappears(POPUP);
    }

    public void clickRejectAll()
    {
        jsClick(getDriver().findElementWithoutWaiting(REJECT_ALL_BUTTON));
        getDriver().waitUntilElementDisappears(POPUP);
    }

    public CookieConsentPopUp clickConfigure()
    {
        jsClick(getDriver().findElementWithoutWaiting(CONFIGURE_BUTTON));
        getDriver().waitUntilElementIsVisible(COOKIE_SELECT_PANE);
        return this;
    }

    public boolean isCookieTypeSelected(String cookieType)
    {
        return getDriver().findElementWithoutWaiting(By.id("CookieButton_" + cookieType)).isSelected();
    }

    public void setCookieTypeSelected(String cookieType, boolean selected)
    {
        WebElement checkbox = getDriver().findElementWithoutWaiting(By.id("CookieButton_" + cookieType));
        if (checkbox.isSelected() != selected) {
            jsClick(checkbox);
        }
    }

    public List<String> getAvailableCookieTypes()
    {
        return getDriver().findElementsWithoutWaiting(By.cssSelector("#CookieSelectPane .CookieButton")).stream()
            .map(el -> el.getAttribute("id").replace("CookieButton_", "")).collect(Collectors.toList());
    }

    public void clickOk()
    {
        jsClick(getDriver().findElementWithoutWaiting(OK_BUTTON));
        getDriver().waitUntilElementDisappears(POPUP);
    }

    public boolean isActive()
    {
        Object value = getConfigurationProperty("active");
        return value != null && ((Number) value).intValue() == 1;
    }

    public String getType()
    {
        return (String) getConfigurationProperty("type");
    }

    public String getPosition()
    {
        return (String) getConfigurationProperty("position");
    }

    public String getAnimation()
    {
        return (String) getConfigurationProperty("animation");
    }

    private void jsClick(WebElement element)
    {
        (getDriver()).executeScript("arguments[0].click();", element);
    }

    private Object getConfigurationProperty(String propertyName)
    {
        WebElement script = getDriver().findElementWithoutWaiting(CONFIG_JSON);
        return getDriver().executeScript("return JSON.parse(arguments[0].textContent)[arguments[1]];", script,
            propertyName);
    }
}
