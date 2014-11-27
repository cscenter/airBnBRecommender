package ru.cscenter.practice.recsys;

import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.logging.Level;

public class TestProgram {

    public static void main(String[] args){

        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.WARNING);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.WARNING);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setJavascriptEnabled(true);
        WebDriver webDriver = new FirefoxDriver(capabilities);

        WebSpider spider = new WebSpider(webDriver);
        spider.discoverHostUsersThenFlats(1);

    }

}
