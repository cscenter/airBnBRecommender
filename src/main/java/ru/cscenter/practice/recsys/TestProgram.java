package ru.cscenter.practice.recsys;

import org.apache.log4j.BasicConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestProgram {

    public static void main(String[] args) throws IOException {
        //BasicConfigurator.configure();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setJavascriptEnabled(true);

        WebDriver dr = new FirefoxDriver();
        //dr.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);
        //dr.get("https://www.airbnb.com/s?host_id=561814");

        //FlatParser.getUserIdsFromComments(dr);
        //UserParser.getUserHostIdsFromComments(dr);
        //List<Integer> a = new FlatPageParser().parse(dr);

        //System.out.println(a.toString());

        WebSpider spider = new WebSpider(new FirefoxDriver(capabilities));

        try {
            spider.DiscoverHostUsersThenFlats(200, 10, 10);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
