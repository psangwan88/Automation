package org.factory;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.json.simple.JSONObject;
import org.pages.HomePage;
import org.pages.SharedPost;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.utils.generic.GenericLib;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class BaseClass implements Constants {


    public static JSONObject inputData;
    public Page page;
    public HomePage homePage;
    public SharedPost sharedPost;
    public Browser browser;
    public Path videoDir = Paths.get(videopath1);
    public Path testvideoDir = Paths.get(videopath2);
    public static BrowserContext context;
    public static String[] folders = new String[] {videopath1,downloadPath,videopath2,traceViewPath};
    public static Properties property;
    public static String browserType;
    public static boolean headless = true;
    public static boolean videoRec = false;
    public static boolean traceView = false;
    public static boolean screenshotFail = false;
    public static boolean screenshotPass = false;
    public static int retryCount = 0;
    public static String app = "sc";
    public static String view = "desktop";

    @BeforeSuite
    public void beforeSuite(){
        property = GenericLib.configReader("config/config.properties");
        inputData = readInputJson();
        setConfigParams();
        clearData();
    }


    public void setConfigParams(){
        browserType = System.getProperty("browser", property.getProperty("browser"));
        headless = System.getProperty("headless", property.getProperty("headless")).toLowerCase().equals("true") ? true : false;
        app = System.getProperty("app", property.getProperty("app")).toLowerCase().equals("sc") ? "sc" : "moj";
        view = System.getProperty("view", property.getProperty("view")).toLowerCase().equals("desktop") ? "desktop" : "mobile";
        videoRec = System.getProperty("videoRecording", property.getProperty("videoRecording")).toLowerCase().equals("true") ? true : false;
        traceView = System.getProperty("traceViewer", property.getProperty("traceViewer")).toLowerCase().equals("true") ? true : false;
        screenshotPass  = System.getProperty("screenshotonPass", property.getProperty("screenshotonPass")).toLowerCase().equals("true") ? true : false;
        screenshotFail = System.getProperty("screenshotonFail", property.getProperty("screenshotonFail")).toLowerCase().equals("true") ? true : false;
        retryCount = Integer.parseInt(System.getProperty("retryCount", property.getProperty("retryCount")));

    }


    @BeforeMethod
    public void beforeMethod(){
        DriverFactory factory = new DriverFactory();
        browser = factory.initBrowser(browserType, downloadPath, headless);
        context = browser.newContext(getBrowserContextOptions());
        context.setDefaultTimeout(wait);
        if(traceView)
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));

        page = context.newPage();
        if(app.equals("sc"))
            page.navigate(sc_url);
        else
            page.navigate(moj_url);
    }

    public Browser.NewContextOptions getBrowserContextOptions(){
        Browser.NewContextOptions contextOption = new Browser.NewContextOptions();
        if(videoRec)
            contextOption.setRecordVideoDir(videoDir);
        if(view.equals("mobile"))
            contextOption.setDeviceScaleFactor(3)
                    .setHasTouch(true)
                    .setIsMobile(true)
                    .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.0 Mobile/15E148 Safari/604.1")
                    .setViewportSize(390, 664);

        return contextOption;
    }
    @AfterMethod
    public void afterMethod(Method method,ITestResult result){
        if(videoRec)
            System.out.println(method.getName() + "  " + page.video().path().toString());
        if (result.getStatus() == ITestResult.FAILURE && screenshotFail) {
            // Capture screenshot if the test failed
            captureScreenshot();
//            Allure.addAttachment("Failure screenshot", );
        }
        if(traceView)
            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(Paths.get(traceViewPath + method.getName()+".zip")));
        context.close();
        browser.close();
        if(videoRec)
            moveVideoFile(method.getName());
    }


    public void moveVideoFile(String fileName){
        File[] files = videoDir.toFile().listFiles();
        File videoFile = files[0];
        File renamedFile = new File(testvideoDir.toFile(), fileName + ".webm");
        try {
            Files.move(videoFile.toPath(), renamedFile.toPath());
        }
        catch (Exception e){
            System.out.println(e.fillInStackTrace());
        }
    }
    public JSONObject readInputJson(){
        return GenericLib.readJson(dataFile);
    }
    public boolean clearDownloadVideos(File folder){
        try {
            if (folder.isDirectory()) {
                for (File file : folder.listFiles()) {
                    clearDownloadVideos(file); // Recursive call for subdirectories and files
                }
            }
            return folder.delete(); // Delete the folder or file
        }
        catch(Exception e){
            System.out.println(e.fillInStackTrace());
        }
        return true;
    }

    public void clearData(){
        for (String fol: folders
             ) {
            clearDownloadVideos(new File(fol));
            File folder = new File(fol);
            folder.mkdir();
        }
    }

    public void wait(int millisec){
        try {
            page.wait(millisec);
        }
        catch (Exception e) {}
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public InputStream captureScreenshot() {
        Allure.step("Taking screenshot");
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setPath(null));

        // Convert byte array to InputStream
        InputStream inputStream = new ByteArrayInputStream(screenshot);

        // Attach the screenshot to Allure report
        Allure.addAttachment("Screenshot on Failure", inputStream);
        return inputStream;
    }
}
