package ru.cscenter.practice.recsys;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.apache.log4j.BasicConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;


public class TestProgram {

    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();

        //DesiredCapabilities capabilities = new DesiredCapabilities();
        //capabilities.setJavascriptEnabled(true);
        //capabilities.setCapability("webdriver.log.browser.ignore", true);
        HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver(BrowserVersion.FIREFOX_24);
        htmlUnitDriver.setJavascriptEnabled(true);
        WebDriver webDriver = htmlUnitDriver; //new FirefoxDriver(capabilities);

        /*webDriver.get("https://www.airbnb.com/rooms/136821");

        WebElement anchor = webDriver.findElement(By.xpath("//li[@class='next next_page']/a"));
        anchor.click();

        List<WebElement> users = webDriver.findElements(By.xpath("//div[@class='col-3']/div[@class='pull-right']/div[@class='name text-center']/a/"));

        for(WebElement user : users)
            System.out.println(user.getAttribute("href"));*/

        webDriver.get("https://www.airbnb.com/rooms/136821");
        String[] a = FlatParser.getAmenities(webDriver);

        WebSpider spider = new WebSpider(webDriver);

        try {
            spider.DiscoverHostUsersThenFlats(200, 10, 10);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
