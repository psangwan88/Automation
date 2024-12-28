package org.tests;

import com.microsoft.playwright.Page;
import org.factory.BaseClass;
import org.factory.DriverFactory;
import org.pages.HomePage;
import org.testng.annotations.Test;

public class HomePageTests extends BaseClass {


    @Test
    public void validHomepage(){

        homePage = new HomePage(page);
       homePage.validate_HomePage();
        homePage.getAllTopTabs();
    }

    @Test
    public void validLeftTabs(){
        homePage = new HomePage(page);
        homePage.validateHome();
        homePage.validateExplore();
        homePage.validateWallet();
        homePage.validateLanguage();
    }
}
