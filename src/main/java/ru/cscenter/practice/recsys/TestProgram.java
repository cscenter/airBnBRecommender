package ru.cscenter.practice.recsys;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class TestProgram {

    public static void main(String[] args) throws IOException {

        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.WARNING);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.WARNING);

        HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver(BrowserVersion.FIREFOX_24);
        htmlUnitDriver.setJavascriptEnabled(true);
        htmlUnitDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        htmlUnitDriver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
        htmlUnitDriver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        htmlUnitDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        htmlUnitDriver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);


        WebSpider spider = new WebSpider(htmlUnitDriver);

        try {
            spider.DiscoverHostUsersThenFlats(200, 10, 10);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
