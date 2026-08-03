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
package com.xwiki.cookieconsent.ui;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.test.docker.junit5.ExtensionOverride;
import org.xwiki.test.docker.junit5.UITest;
import org.xwiki.test.ui.TestUtils;
import org.xwiki.test.ui.XWikiWebDriver;
import org.xwiki.user.test.po.PreferencesEditPage;
import org.xwiki.user.test.po.PreferencesUserProfilePage;
import org.xwiki.user.test.po.ProfileUserProfilePage;

import com.xwiki.cookieconsent.po.CookieConsentPage;
import com.xwiki.cookieconsent.po.CookieConsentPopUp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UI tests for the GDPR Cookie Consent Macro.
 *
 * @version $Id$
 * @since 2.0
 */
@UITest(properties = { "xwikiCfgPlugins=com.xpn.xwiki.plugin.skinx.JsResourceSkinExtensionPlugin,"
    + "com.xpn.xwiki.plugin.skinx.CssResourceSkinExtensionPlugin",
    "xwikiPropertiesAdditionalProperties=test.prchecker.excludePattern=.*:Main\\.testPage" }, extensionOverrides = {
    @ExtensionOverride(extensionId = "com.google.code.findbugs:jsr305", overrides = {
        "features=com.google.code.findbugs:annotations" }),
    @ExtensionOverride(extensionId = "org.bouncycastle:bcprov-jdk18on", overrides = {
        "features=org.bouncycastle:bcprov-jdk15on" }),
    @ExtensionOverride(extensionId = "org.bouncycastle:bcpkix-jdk18on", overrides = {
        "features=org.bouncycastle:bcpkix-jdk15on" }),
    @ExtensionOverride(extensionId = "org.bouncycastle:bcmail-jdk18on", overrides = {
        "features=org.bouncycastle:bcmail-jdk15on" }) })
public class CookieConsentIT
{
    private final DocumentReference testPage = new DocumentReference("xwiki", "Main", "testPage");

    @BeforeAll
    void setup(TestUtils testUtils) throws Exception
    {
        testUtils.createAdminUser();
        testUtils.loginAsSuperAdmin();

        testUtils.setGlobalRights("XWiki.XWikiAdminGroup", "", "admin", true);
        ProfileUserProfilePage userProfilePage = ProfileUserProfilePage.gotoPage("Admin");

        PreferencesUserProfilePage preferencesPage = userProfilePage.switchToPreferences();
        PreferencesEditPage preferencesEditPage = preferencesPage.editPreferences();
        preferencesEditPage.setAdvancedUserType();
        preferencesEditPage.clickSaveAndView();
        testUtils.setWikiPreference("multilingual", "true");
        testUtils.setWikiPreference("languages", "de,en,fr,it");
        testUtils.setWikiPreference("default_language", "en");

        testUtils.createPage(testPage, "This is a test page.");

        testUtils.updateObject("XWiki", "XWikiPreferences", "XWiki.XWikiPreferences", 0, "meta",
            "#foreach($uix in $services.uix.getExtensions(\"org.xwiki.platform.html.head\", "
                + "{'sortByParameter' : 'order'}))\n" + "$services.rendering.render($uix.execute(), 'xhtml/1.0')\n"
                + "#end");
        testUtils.forceGuestUser();
    }

    @Test
    @Order(1)
    void configureGeneralSettingsTest(TestUtils testUtils)
    {
        testUtils.loginAsAdmin();
        CookieConsentPage page = CookieConsentPage.gotoPage();
        page.setActive(true).setType("box").setPosition("top").setAnimation("fade").setBackgroundColor("#000000")
            .setForegroundColor("#FFFFFF");

        page.clickSave();
        testUtils.gotoPage(testPage);

        CookieConsentPopUp popup = new CookieConsentPopUp();

        assertTrue(popup.isDisplayed(),
            "The cookie consent popup should be displayed for a guest/new " + "user who hasn't answered yet");

        assertTrue(popup.isActive(), "Configuration should be active");
        assertEquals("box", popup.getType());
        assertEquals("top", popup.getPosition());
        assertEquals("fade", popup.getAnimation());

        assertEquals("Accept all cookies", popup.getAcceptAllButtonText());
        assertEquals("Reject", popup.getRejectButtonText());
        assertEquals("Configure", popup.getConfigureButtonText());

        assertEquals("rgba(0, 0, 0, 0)", popup.getBackgroundColor());
        assertEquals("rgb(34, 34, 34)", popup.getTextColor());
    }

    @Test
    @Order(2)
    void configureTextsTest(TestUtils testUtils)
    {
        testUtils.loginAsAdmin();
        CookieConsentPage page = CookieConsentPage.gotoPage();

        page.setButtonText("Accept everything").setRejectButtonText("Decline").setConfigButtonText("Manage preferences")
            .setDisclaimer1("Updated disclaimer 1 text for testing purposes.")
            .setDisclaimer2("Updated disclaimer 2 text for testing purposes.")
            .setNecessaryInfoBox("Updated necessary info box.").setPreferencesInfoBox("Updated preferences info box.")
            .setStatisticsInfoBox("Updated statistics info box.").setMarketingInfoBox("Updated marketing info box.");

        page.clickSave();

        testUtils.forceGuestUser();
        testUtils.gotoPage(testPage);

        CookieConsentPopUp popup = new CookieConsentPopUp();

        assertTrue(popup.isDisplayed());
        assertEquals("Accept everything", popup.getAcceptAllButtonText());
        assertEquals("Decline", popup.getRejectButtonText());
        assertEquals("Manage preferences", popup.getConfigureButtonText());
        assertTrue(popup.getDisclaimerText().contains("Updated disclaimer 1 text for testing purposes."));
    }

    @Test
    @Order(3)
    void configureLabelsAndActiveCookieTypesTest(TestUtils testUtils)
    {
        testUtils.loginAsAdmin();
        CookieConsentPage page = CookieConsentPage.gotoPage();

        page.setNecessaryLabel("Essential").setPreferencesLabel("Personalization").setStatisticsLabel("Analytics")
            .setMarketingLabel("Advertising").setActiveCookieTypes("necessary", "statistics");

        page.clickSave();

        testUtils.forceGuestUser();
        testUtils.gotoPage(testPage);

        CookieConsentPopUp popup = new CookieConsentPopUp();
        assertTrue(popup.isDisplayed());

        popup.clickConfigure();

        assertEquals(2, popup.getAvailableCookieTypes().size(),
            "Only necessary and statistics were set as active cookie types");

        assertTrue(popup.getAvailableCookieTypes().contains("necessary"));
        assertTrue(popup.getAvailableCookieTypes().contains("statistics"));
        assertFalse(popup.getAvailableCookieTypes().contains("preferences"));
        assertFalse(popup.getAvailableCookieTypes().contains("marketing"));

        assertTrue(popup.isCookieTypeSelected("necessary"), "Necessary should always be checked and disabled");
    }

    @Test
    @Order(4)
    void acceptAllHidesPopupAndPersistsChoiceTest(TestUtils testUtils)
    {
        testUtils.forceGuestUser();
        testUtils.gotoPage(testPage);

        CookieConsentPopUp popup = new CookieConsentPopUp();
        assertTrue(popup.isDisplayed());

        popup.clickAcceptAll();
        assertFalse(popup.isDisplayed());

        // Reloading the page should not show the popup again, since the "cookieConsent" cookie now records that the
        // user already answered.
        testUtils.gotoPage(testPage);
        assertFalse(new CookieConsentPopUp().isDisplayed(),
            "The popup should stay hidden after the user already accepted");
    }

    @Test
    @Order(5)
    void rejectAllHidesPopupTest(TestUtils testUtils)
    {
        testUtils.forceGuestUser();
        testUtils.gotoPage(testPage);

        CookieConsentPopUp popup = new CookieConsentPopUp();
        assertTrue(popup.isDisplayed());

        popup.clickRejectAll();
        assertFalse(popup.isDisplayed());
    }

    @Test
    @Order(6)
    void configureAllCookieTypesTest(TestUtils testUtils)
    {
        testUtils.loginAsAdmin();
        CookieConsentPage.gotoPage().setActiveCookieTypes("necessary", "preferences", "statistics", "marketing")
            .clickSave();

        testUtils.forceGuestUser();
        testUtils.gotoPage(testPage);

        CookieConsentPopUp popup = new CookieConsentPopUp();
        assertTrue(popup.isDisplayed());
        popup.clickConfigure();

        assertEquals(4, popup.getAvailableCookieTypes().size());

        assertTrue(popup.isCookieTypeSelected("necessary"));
        assertFalse(popup.isCookieTypeSelected("preferences"));
        assertFalse(popup.isCookieTypeSelected("statistics"));
        assertFalse(popup.isCookieTypeSelected("marketing"));

        // All three non-mandatory categories must be togglable.
        popup.setCookieTypeSelected("preferences", true);
        popup.setCookieTypeSelected("statistics", true);
        popup.setCookieTypeSelected("marketing", true);
        assertTrue(popup.isCookieTypeSelected("preferences"));
        assertTrue(popup.isCookieTypeSelected("statistics"));
        assertTrue(popup.isCookieTypeSelected("marketing"));
    }

    @Test
    @Order(7)
    void configureTypePositionAnimationValuesTest(TestUtils testUtils)
    {
        testUtils.loginAsAdmin();
        CookieConsentPage.gotoPage().setType("bar").setPosition("bottom").setAnimation("slide").clickSave();

        testUtils.forceGuestUser();
        testUtils.gotoPage(testPage);

        CookieConsentPopUp popup = new CookieConsentPopUp();
        assertEquals("bar", popup.getType());
        assertEquals("bottom", popup.getPosition());
        assertEquals("slide", popup.getAnimation());

        testUtils.loginAsAdmin();
        CookieConsentPage.gotoPage().setAnimation("none").clickSave();

        testUtils.forceGuestUser();
        testUtils.gotoPage(testPage);
        assertEquals("none", new CookieConsentPopUp().getAnimation());
    }

    @Test
    @Order(8)
    void partialConsentTest(TestUtils testUtils)
    {
        testUtils.loginAsAdmin();
        CookieConsentPage.gotoPage().setActiveCookieTypes("necessary", "preferences", "statistics", "marketing")
            .clickSave();

        testUtils.forceGuestUser();
        testUtils.gotoPage(testPage);

        CookieConsentPopUp popup = new CookieConsentPopUp();
        assertTrue(popup.isDisplayed());
        popup.clickConfigure();

        popup.setCookieTypeSelected("preferences", false);
        popup.setCookieTypeSelected("statistics", false);
        popup.setCookieTypeSelected("marketing", true);
        popup.clickOk();

        assertFalse(popup.isDisplayed(), "The popup should hide after confirming with OK");

        String accepted = popup.getAcceptedCategoriesCookieValue();
        assertTrue(accepted != null && accepted.contains("necessary"));
        assertTrue(accepted.contains("marketing"));
        assertFalse(accepted.contains("preferences"));
        assertFalse(accepted.contains("statistics"));

        testUtils.gotoPage(testPage);
        assertFalse(new CookieConsentPopUp().isDisplayed(),
            "The popup should stay hidden after the partial choice was persisted");
    }

    @Test
    @Order(9)
    void trackerScriptsTest(TestUtils testUtils, XWikiWebDriver webDriver)
    {
        String statisticsMarker = "<div id=\"statsTrackerMarker\">stats-loaded</div>";
        String marketingMarker = "<div id=\"marketingTrackerMarker\">marketing-loaded</div>";

        testUtils.loginAsAdmin();
        CookieConsentPage.gotoPage().setActiveCookieTypes("necessary", "preferences", "statistics", "marketing")
            .setStatisticsScripts(statisticsMarker).setMarketingScripts(marketingMarker).clickSave();

        testUtils.forceGuestUser();
        testUtils.gotoPage(testPage);

        assertTrue(webDriver.findElementsWithoutWaiting(By.id("statsTrackerMarker")).isEmpty(),
            "Statistics tracker must not be injected before consent");
        assertTrue(webDriver.findElementsWithoutWaiting(By.id("marketingTrackerMarker")).isEmpty(),
            "Marketing tracker must not be injected before consent");

        CookieConsentPopUp popup = new CookieConsentPopUp();
        popup.clickConfigure();
        popup.setCookieTypeSelected("statistics", true);
        popup.setCookieTypeSelected("marketing", false);
        popup.clickOk();

        // TrackersUIX (org.xwiki.platform.template.header.after) re-evaluates "gdprSettings" on the next render, so
        // reload before checking.
        testUtils.gotoPage(testPage);

        assertFalse(webDriver.findElementsWithoutWaiting(By.id("statsTrackerMarker")).isEmpty(),
            "Statistics tracker should now be injected");
        assertTrue(webDriver.findElementsWithoutWaiting(By.id("marketingTrackerMarker")).isEmpty(),
            "Marketing tracker should stay out since marketing was not accepted");
    }
}
