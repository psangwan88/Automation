package org.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.factory.BaseClass;
import org.testng.Assert;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HomePage extends BaseClass {

    Page page;
    String xpath_home = "//a[@title='home']";
    String css_explore = "a[title='explore']";
    String css_wallet = "a[title='wallet']";
    String css_video = "a[title='video']";
    String css_profile = "a[title='profile']";
    String css_trends = "a[title='trends']";
    String css_LangBtn = "a[href='/langChangeModal']";
    String css_tabs = "button[role='tab']";
    String text_categories = "text='Categories'";
    String css_phone = "input[name='phoneNo']";
    String css_languages = "img[alt^='Language']";

    String authorName = "strong[data-cy='author-name']";
    String postCaption = "div[data-cy='post-caption']";

    public HomePage(Page page){
       this.page = page;
    }
    public void validate_HomePage(){
        Allure.step("Validating home page");
        page.locator(xpath_home).first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(5000));
        Assert.assertTrue(page.locator(xpath_home).first().isVisible(), "Home should be visible");
        Assert.assertTrue(page.locator(css_explore).first().last().isVisible(), "Explore should be visible");
        Assert.assertTrue(page.locator(css_wallet).first().last().isVisible(), "wallet should be visible");
        Assert.assertTrue(page.locator(css_video).first().last().isVisible(), "Video should be visible");
//        Assert.assertTrue(page.locator(css_trends).last().isVisible(), "trends should be visible");
//        Assert.assertTrue(page.locator(css_LangBtn).last().isVisible(), "Language change should be visible");
    }

    public List<String> getAllTopTabs() {
        page.waitForSelector(css_tabs);
        Locator locators = page.locator(css_tabs);
        List<String> feeds = new ArrayList<>();
        for (int i = 0; i < locators.count(); i++) {
            feeds.add(locators.nth(i).getAttribute("data-value"));
        }
        return feeds;
    }

    public void goToHomePage(){
        page.locator(xpath_home).first().click();
    }

    public void validateHome(){
        Allure.step("validate home click");
        page.locator(xpath_home).first().click();
        Assert.assertTrue(getAllTopTabs().size() > 2, "Feed tabs on top should be there");
        page.goBack();
        goToHomePage();
    }

    public void validateExplore(){
        Allure.step("validate explore click");
        page.locator(css_explore).first().click();
        page.waitForSelector(text_categories);
        Assert.assertTrue(page.locator(text_categories).isVisible(), "Categories should be there");
        page.goBack();
        goToHomePage();
    }


    public void validateWallet(){
        Allure.step("validate wallet click");
        page.locator(css_wallet).first().click();
        page.waitForSelector(css_phone, new Page.WaitForSelectorOptions().setTimeout(5000));
        Assert.assertTrue(page.locator(css_phone).isVisible(), "Login option should be there");
        page.goBack();
        goToHomePage();
    }

    @Step("Validate Language page")
    public void validateLanguage(){
        Allure.step("validate Language click");
        page.locator(css_LangBtn).first().click();
        page.waitForSelector(css_languages);
        Assert.assertEquals(page.locator(css_languages).count() , 14, "Languages should be there");
        page.goBack();
        goToHomePage();
    }


    public void likePosts(){
        Allure.step("validate like a post click");
        String likebutton = "button[aria-label='click to like']";
        String likescount = "span:has-text('likes')";
        Locator locatorlikes = page.locator(likebutton);
        Locator locatorlikescount = page.locator(likescount);

        List<Integer> count = new ArrayList<>();
        List<Integer> aftercount = new ArrayList<>();
        List<Integer> withrefresh = new ArrayList<>();


        for(int i=0;i<locatorlikes.count();i++){
            count.add(Integer.valueOf(locatorlikescount.nth(i).textContent().split(" ")[0]));
        }

        Locator button = locatorlikes.nth(0);
        button.click();

//        for (int i = 0; i < 1; i++) {
//            Locator button = locatorlikes.nth(i);
//            button.hover();
//            wait(2000);
//            BoundingBox boundingBox = button.boundingBox();
//
//
//            int viewportHeight = (int) page.viewportSize().height;
//            if (boundingBox != null) {
//                double top = boundingBox.y;
//                double bottom = top + boundingBox.height;
//
//                // Check if the button is within the visible viewport
//                if (top >= 0 && bottom <= viewportHeight) {
//                    wait(1000);
//                    button.click(); // Click the button
//                    System.out.println("Clicked the button visible on screen.");
//                    break;
//                }
//            }
//        }

        locatorlikescount = page.locator(likescount);
        for(int i=0;i<locatorlikescount.count();i++){
            aftercount.add(Integer.valueOf(locatorlikescount.nth(i).textContent().split(" ")[0]));
        }
        Assert.assertEquals(aftercount.get(0) + 0, count.get(0) + 1,"Only one item to be like");
        Assert.assertEquals(aftercount.get(2), count.get(2),"Only one item to be like");
        System.out.println("sdfsdfs");
    }



    public void readcomments()  {
        Allure.step("validate comments click");
        String commentbutton = "button[aria-label='click to comment']";
        String commentstext = "div[role='alertdialog'] h2 span";
        String signIn = "a[aria-label='Click to sign in']";
        String closeAlert = "h2 button svg";
        String signintext = "text='Sign in'";
        String text = "text='Comments'";

        page.locator(commentbutton).nth(0).click();
        Assert.assertEquals(page.locator(commentstext).textContent().trim(),"Comments","Comments text");
        page.locator(closeAlert).nth(0).click();


        page.locator(commentbutton).nth(0).click();
        page.locator(signIn).nth(0).click();
        page.waitForSelector(signintext);
        Assert.assertTrue(page.locator(signintext).isVisible(),"sign in page should open");
        page.goBack();

    }


    public void downloadPost(){
        Allure.step("validate download click");
        String downloadLoc = "button[aria-label='click to download']";

        Download downloadItem = page.waitForDownload(() -> {
            page.locator(downloadLoc).nth(0).click();
        });

        System.out.println(downloadItem.url());
        System.out.println(downloadItem.suggestedFilename());
        String fileName = downloadItem.path().toString(); // Replace with your desired file name

        // Create the full path to the file
        Path filePath = Paths.get(fileName);

        // Check if the file exists
        if (Files.exists(filePath)) {
            Assert.assertTrue(true, "File is downloaded " + fileName);
        } else {
            Assert.assertTrue(false, "File is not downloaded with name " + fileName);
        }
    }
}
