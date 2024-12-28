package org.tests;

import org.factory.BaseClass;
import org.json.simple.JSONObject;
import org.pages.MojHomePage;
import org.pages.SharedPost;
import org.testng.annotations.Test;

import static org.factory.BaseClass.inputData;

public class MojTests extends BaseClass {

    @Test
    public void sharedImagePost(){
        JSONObject data = (JSONObject) inputData.get("mojvideoPost");
        page.navigate((String) data.get("url"));
        MojHomePage mojHomePage = new MojHomePage(page);

        mojHomePage.validateHomePage((String) data.get("author"));
//        mojHomePage.clickInstallNow();
    }

}
