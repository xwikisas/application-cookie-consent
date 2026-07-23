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
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.test.docker.junit5.ExtensionOverride;
import org.xwiki.test.docker.junit5.UITest;
import org.xwiki.test.ui.TestUtils;
import org.xwiki.user.test.po.PreferencesEditPage;
import org.xwiki.user.test.po.PreferencesUserProfilePage;
import org.xwiki.user.test.po.ProfileUserProfilePage;

import com.xwiki.cookieconsent.po.CookieConsentPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * UI tests for the GDPR Cookie Consent Macro.
 *
 * @version $Id$
 * @since 2.7
 */
@UITest(properties = { "com.xpn.xwiki.plugin.skinx.JsResourceSkinExtensionPlugin, "
    + "com.xpn.xwiki.plugin.skinx.CssResourceSkinExtensionPlugin", }, extensionOverrides = {
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
        testUtils.setWikiPreference("default_language", "en");

        testUtils.createUser("UserTest", "UserTest", "");

        testUtils.loginAsAdmin();

    }

    @Test
    @Order(1)
    void configureGeneralSettings(TestUtils testUtils)
    {
        CookieConsentPage page = CookieConsentPage.gotoPage();

        page.setActive(true).setType("box").setPosition("top").setAnimation("fade").setBackgroundColor("#000000")
            .setForegroundColor("#FFFFFF").setOpacity("0.8");

        page.clickSave();

        testUtils.login("UserTest", "UserTest");
        testUtils.gotoPage(testPage);
        assertEquals(2, 3);
    }

    //@Test
    @Order(2)
    void configureConfigurableTexts()
    {

        CookieConsentPage page = CookieConsentPage.gotoPage();

        page.setButtonText("Accept everything").setRejectButtonText("Decline").setConfigButtonText("Manage preferences")
            .setDisclaimer1("Updated disclaimer 1 text for testing purposes.")
            .setDisclaimer2("Updated disclaimer 2 text for testing purposes.")
            .setNecessaryInfoBox("Updated necessary info box.").setPreferencesInfoBox("Updated preferences info box.")
            .setStatisticsInfoBox("Updated statistics info box.").setMarketingInfoBox("Updated marketing info box.");

        page.clickSave();
    }

    //@Test
    @Order(3)
    void configureTrackersAndScripts()
    {
        CookieConsentPage page = CookieConsentPage.gotoPage();

        page.setPreferencesScripts("console.log('preferences script');")
            .setStatisticsScripts("console.log('statistics script');")
            .setMarketingScripts("console.log('marketing script');");

        page.clickSave();
    }

    //@Test
    @Order(4)
    void configureLabelsAndActiveCookieTypes()
    {
        CookieConsentPage page = CookieConsentPage.gotoPage();

        page.setNecessaryLabel("Essential").setPreferencesLabel("Personalization").setStatisticsLabel("Analytics")
            .setMarketingLabel("Advertising").setActiveCookieTypes("necessary", "statistics");

        page.clickSave();
    }
}
