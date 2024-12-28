package org.tests;

import com.microsoft.playwright.Page;
import org.factory.BaseClass;
import org.factory.DriverFactory;
import org.pages.HomePage;
import org.testng.annotations.Test;

public class ActionsOnPostTests extends BaseClass {


    @Test(enabled = false)
    public void likePosts(){

        homePage = new HomePage(page);
        homePage.likePosts();
    }

    @Test
    public void readComments(){

        homePage = new HomePage(page);
        homePage.readcomments();
    }

    @Test
    public void downloadPost(){

        homePage = new HomePage(page);
        homePage.downloadPost();
    }
}

