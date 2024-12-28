package org.pages;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.Assert;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SharedPost {
    Page page;
    String loc_authorName = "strong[data-cy='author-name']";
    String postCaption = "div[data-cy='post-caption']";
    String share = "//button[@data-cy='share-action-btn']";
    String videowrapper = "div[role='videoWrapper']";
    String videoPlay = "div[data-cy='video-play-icon']";
    String nextVideo = "div[data-cy='play-next-video']";
    String videoProgressbar = "div[role='seekVideo']";
    String downloadLoc = "button[aria-label='Click to download']";

    public SharedPost(Page page){
        this.page = page;
    }

    @Step("Validate share image post")
    public void validateImagePost(String authorName, String caption){
        Allure.step("validate shared image post");
        Assert.assertEquals(page.locator(loc_authorName).first().textContent(),authorName,"Author name should be " + authorName);
        Assert.assertEquals(page.locator("text=" + authorName).first().textContent(),authorName,"Author name should be " + authorName);
        Assert.assertEquals(page.locator(postCaption).first().locator("span").first().textContent(),caption,"Caption  should be " + caption);
        Assert.assertTrue(page.locator(share).first().isVisible(), " SHare optio should be there");
    }

    @Step("Validate shared video post")
    public void validateVideoPost(String authorName, String caption){
        Allure.step("validate shared video post");
//        Assert.assertTrue(page.locator(loc_authorName).first().textContent().contains(authorName),"Author name should be " + authorName);
        Assert.assertEquals(page.locator(postCaption).first().locator("span").first().textContent(),caption,"Caption  should be " + caption);
        Assert.assertTrue(page.locator(share).first().isVisible(), " SHare option should be there");
        page.locator(videoPlay).nth(0).click();
        Allure.step("click on download video");
        Download download = page.waitForDownload(() -> {
            page.getByLabel("Click to download").first().click();
        });

        System.out.println(download.url());
        System.out.println(download.suggestedFilename());
        String fileName = download.path().toString(); // Replace with your desired file name

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
