package org.factory;

import com.microsoft.playwright.*;

import java.nio.file.Paths;

public class DriverFactory {

    Playwright playwright;
    public Browser initBrowser(String browserType, String downloadPath, boolean headless){
        Playwright playwright = Playwright.create();
        Browser browser;
        BrowserType.LaunchOptions launchOption = new BrowserType.LaunchOptions();
        launchOption.setHeadless(headless)
                .setSlowMo(300)
                .setDownloadsPath(Paths.get(Paths.get(downloadPath).toAbsolutePath().toString()));

        switch (browserType.toLowerCase()) {
            case "chrome":
            case "chromium": // Playwright uses "chromium" for Chrome-based browsers
                browser = Playwright.create().chromium().launch(launchOption);
                System.out.println("Launched Chromium browser.");
                break;

            case "firefox":
                browser = Playwright.create().firefox().launch(launchOption);
                System.out.println("Launched Firefox browser.");
                break;

            case "safari":
            case "webkit": // Playwright uses "webkit" for Safari-like browser
                browser = Playwright.create().webkit().launch(launchOption);
                System.out.println("Launched Webkit browser.");
                break;

            default:
                throw new IllegalArgumentException("Invalid browser type: " + browserType);
        }
        return browser;
    }

    public Browser initBrowser(String browserType){

        return initBrowser(browserType, "downloads",false);
    }

}
