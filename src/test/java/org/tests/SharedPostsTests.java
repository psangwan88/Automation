package org.tests;

import com.microsoft.playwright.Page;
import org.factory.BaseClass;
import org.factory.DriverFactory;
import org.json.simple.JSONObject;
import org.pages.HomePage;
import org.pages.SharedPost;
import org.testng.annotations.Test;

import java.io.FileReader;

public class SharedPostsTests extends BaseClass {



    @Test
    public void sharedImagePost(){
        JSONObject data = (JSONObject) inputData.get("imagePost");
        page.navigate((String) data.get("url"));
        sharedPost = new SharedPost(page);

        System.out.println("Dfsfsf");
        sharedPost.validateImagePost((String)data.get("author"),(String)data.get("caption"));
    }

    @Test
    public void sharedVideoPost(){
        JSONObject data = (JSONObject) inputData.get("videoPost");
        page.navigate((String) data.get("url"));
        sharedPost = new SharedPost(page);

        System.out.println("Dfsfsf");
        sharedPost.validateVideoPost((String)data.get("author"),(String)data.get("caption"));

    }
}
