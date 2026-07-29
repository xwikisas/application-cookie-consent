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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.xwiki.test.ui.po.ViewPage;

/**
 * Page Object for configuring the GDPR Cookie Consent application.
 *
 * @version $Id$
 * @since 2.0
 */
public class CookieConsentPage extends ViewPage
{
    private static final String FIELD_PREFIX = "CookieConsent.ConfigurationClass_0_";

    @FindBy(id = FIELD_PREFIX + "active")
    private WebElement activeSelect;

    @FindBy(id = FIELD_PREFIX + "type")
    private WebElement typeSelect;

    @FindBy(id = FIELD_PREFIX + "position")
    private WebElement positionSelect;

    @FindBy(id = FIELD_PREFIX + "animation")
    private WebElement animationSelect;

    @FindBy(id = FIELD_PREFIX + "backgroundColor")
    private WebElement backgroundColorInput;

    @FindBy(id = FIELD_PREFIX + "foregroundColor")
    private WebElement foregroundColorInput;

    @FindBy(id = FIELD_PREFIX + "opacity")
    private WebElement opacitySelect;

    @FindBy(id = FIELD_PREFIX + "buttonText")
    private WebElement buttonTextArea;

    @FindBy(id = FIELD_PREFIX + "rejectButtonText")
    private WebElement rejectButtonTextArea;

    @FindBy(id = FIELD_PREFIX + "configButtonText")
    private WebElement configButtonTextArea;

    @FindBy(id = FIELD_PREFIX + "disclaimer1")
    private WebElement disclaimer1Area;

    @FindBy(id = FIELD_PREFIX + "disclaimer2")
    private WebElement disclaimer2Area;

    @FindBy(id = FIELD_PREFIX + "necessaryInfoBox")
    private WebElement necessaryInfoBoxArea;

    @FindBy(id = FIELD_PREFIX + "preferencesInfoBox")
    private WebElement preferencesInfoBoxArea;

    @FindBy(id = FIELD_PREFIX + "statisticsInfoBox")
    private WebElement statisticsInfoBoxArea;

    @FindBy(id = FIELD_PREFIX + "marketingInfoBox")
    private WebElement marketingInfoBoxArea;

    @FindBy(id = FIELD_PREFIX + "preferencesScripts")
    private WebElement preferencesScriptsArea;

    @FindBy(id = FIELD_PREFIX + "statisticsScripts")
    private WebElement statisticsScriptsArea;

    @FindBy(id = FIELD_PREFIX + "marketingScripts")
    private WebElement marketingScriptsArea;

    @FindBy(id = FIELD_PREFIX + "necessaryLabel")
    private WebElement necessaryLabelInput;

    @FindBy(id = FIELD_PREFIX + "preferencesLabel")
    private WebElement preferencesLabelInput;

    @FindBy(id = FIELD_PREFIX + "statisticsLabel")
    private WebElement statisticsLabelInput;

    @FindBy(id = FIELD_PREFIX + "marketingLabel")
    private WebElement marketingLabelInput;

    @FindBy(id = "saveButton")
    private WebElement saveButton;

    public static CookieConsentPage gotoPage()
    {
        getUtil().gotoPage("CookieConsent", "WebHome", "view");
        return new CookieConsentPage();
    }

    public CookieConsentPage setActive(boolean active)
    {
        new Select(activeSelect).selectByValue(active ? "1" : "0");
        return this;
    }

    public CookieConsentPage setType(String type)
    {
        new Select(typeSelect).selectByValue(type);
        return this;
    }

    public CookieConsentPage setPosition(String position)
    {
        new Select(positionSelect).selectByValue(position);
        return this;
    }

    public CookieConsentPage setAnimation(String animation)
    {
        new Select(animationSelect).selectByValue(animation);
        return this;
    }

    public CookieConsentPage setBackgroundColor(String hexColor)
    {
        setValue(backgroundColorInput, hexColor);
        return this;
    }

    public CookieConsentPage setForegroundColor(String hexColor)
    {
        setValue(foregroundColorInput, hexColor);
        return this;
    }

    public CookieConsentPage setButtonText(String text)
    {
        setValue(buttonTextArea, text);
        return this;
    }

    public CookieConsentPage setRejectButtonText(String text)
    {
        setValue(rejectButtonTextArea, text);
        return this;
    }

    public CookieConsentPage setConfigButtonText(String text)
    {
        setValue(configButtonTextArea, text);
        return this;
    }

    public CookieConsentPage setDisclaimer1(String text)
    {
        setValue(disclaimer1Area, text);
        return this;
    }

    public CookieConsentPage setDisclaimer2(String text)
    {
        setValue(disclaimer2Area, text);
        return this;
    }

    public CookieConsentPage setNecessaryInfoBox(String text)
    {
        setValue(necessaryInfoBoxArea, text);
        return this;
    }

    public CookieConsentPage setPreferencesInfoBox(String text)
    {
        setValue(preferencesInfoBoxArea, text);
        return this;
    }

    public CookieConsentPage setStatisticsInfoBox(String text)
    {
        setValue(statisticsInfoBoxArea, text);
        return this;
    }

    public CookieConsentPage setMarketingInfoBox(String text)
    {
        setValue(marketingInfoBoxArea, text);
        return this;
    }

    public CookieConsentPage setStatisticsScripts(String script)
    {
        setValue(statisticsScriptsArea, script);
        return this;
    }

    public CookieConsentPage setMarketingScripts(String script)
    {
        setValue(marketingScriptsArea, script);
        return this;
    }

    public CookieConsentPage setNecessaryLabel(String label)
    {
        setValue(necessaryLabelInput, label);
        return this;
    }

    public CookieConsentPage setPreferencesLabel(String label)
    {
        setValue(preferencesLabelInput, label);
        return this;
    }

    public CookieConsentPage setStatisticsLabel(String label)
    {
        setValue(statisticsLabelInput, label);
        return this;
    }

    public CookieConsentPage setMarketingLabel(String label)
    {
        setValue(marketingLabelInput, label);
        return this;
    }

    public List<String> getActiveCookieTypes()
    {
        List<String> result = new ArrayList<>();
        for (WebElement checkbox : getActiveCookieTypeCheckboxes()) {
            if (checkbox.isSelected()) {
                result.add(checkbox.getAttribute("value"));
            }
        }
        return result;
    }

    public CookieConsentPage setActiveCookieTypes(String... cookieTypes)
    {
        List<String> wanted = Arrays.asList(cookieTypes);
        for (WebElement checkbox : getActiveCookieTypeCheckboxes()) {
            String value = checkbox.getAttribute("value");
            boolean shouldBeChecked = wanted.contains(value);
            if (checkbox.isSelected() != shouldBeChecked) {
                checkbox.click();
            }
        }
        return this;
    }

    /**
     * Saves the configuration and returns the resulting view page.
     */
    public ViewPage clickSave()
    {
        saveButton.click();
        return new ViewPage();
    }

    private List<WebElement> getActiveCookieTypeCheckboxes()
    {
        return getDriver().findElements(
            By.xpath("//input[@name='" + FIELD_PREFIX + "activeCookieTypes' and @type='checkbox']"));
    }

    private void setValue(WebElement element, String value)
    {
        element.clear();
        element.sendKeys(value);
    }
}