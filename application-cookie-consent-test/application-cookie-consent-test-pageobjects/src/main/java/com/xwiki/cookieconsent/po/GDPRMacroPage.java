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

import org.openqa.selenium.By;
import org.xwiki.test.ui.po.ViewPage;

/**
 * Page Object for a page that content GDPR Macros.
 *
 * @version $Id$
 * @since 3.1
 */
public class GDPRMacroPage extends ViewPage
{
    public String getRawGdprSettings()
    {
        return getLine("raw");
    }

    public boolean hasAcceptedPreferences()
    {
        return "true".equals(getLine("preferences"));
    }

    public boolean hasAcceptedStatistics()
    {
        return "true".equals(getLine("statistics"));
    }

    public boolean hasAcceptedMarketing()
    {
        return "true".equals(getLine("marketing"));
    }

    private String getLine(String label)
    {
        String content = getDriver().findElementWithoutWaiting(By.id("xwikicontent")).getText();
        for (String line : content.split("\n")) {
            String trimmed = line.trim();
            if (trimmed.startsWith(label + ":")) {
                return trimmed.substring((label + ":").length()).trim();
            }
        }
        return null;
    }
}