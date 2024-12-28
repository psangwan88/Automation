package org.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.factory.BaseClass;
import org.testng.Assert;

public class MojHomePage extends BaseClass {
    Page page;
    public MojHomePage(Page page){
        this.page = page;
    }
    String authorName = "a[data-testid='author-name']";
    String profilePic = "div[data-testid='author-profile-pic']";
    String installNow = "//a[contains(text(),'Install Now')]";

    @Step("Validate home page MOJ")
    public void validateHomePage(String name){
        Allure.step("validate MOJ home page");
        Assert.assertTrue(page.locator(authorName).first().isVisible(), "Author should be correct");
        Assert.assertTrue(page.locator(profilePic).first().isVisible(), "Author pic should be correct");
        Assert.assertEquals(page.locator(authorName).first().textContent(), name, "Author should be correct");
    }

    public void clickInstallNow(){
        page.onDialog(dialog -> dialog.accept());

        Page newPage = page.waitForPopup(()-> {
            page.locator(installNow).last().click();
        });
//        Page newPage = context.waitForPage(() -> {
//
//        });
// Interact with the new page normally
        System.out.println(newPage.url());
        System.out.println(newPage.title());

    }

    public void swipetoNextVideo(){
        page.locator(authorName);
    }
}
