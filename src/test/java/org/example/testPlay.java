package org.example;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.*;
import com.microsoft.playwright.Playwright;

//import static com.microsoft.playwright.Playwright.create;

public class testPlay {

    public static void main(String[] args) {

        Playwright playwright = Playwright.create();
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

            BrowserContext context = browser.newContext();
            Page page1 = context.newPage();
            page1.navigate("https://ads.staging.sharechat.com/v2/login");

            page1.close();
            context.close();
            playwright.close();

    }
}
