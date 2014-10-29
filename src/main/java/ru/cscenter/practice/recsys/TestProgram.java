package ru.cscenter.practice.recsys;

import org.apache.log4j.BasicConfigurator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class TestProgram {

    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();

        WebSpider spider = new WebSpider();
        try {
            spider.DiscoverHostUsersThenFlats(1, 500, 500);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
