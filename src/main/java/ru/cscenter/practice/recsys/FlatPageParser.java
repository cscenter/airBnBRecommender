package ru.cscenter.practice.recsys;


import org.openqa.selenium.WebDriver;

import java.util.List;


public class FlatPageParser extends Parser {

    private static final String FLAT_ID_EXPRESSION = "//div[@class='col-6 row-space-1']" +
            "/div[@class = 'listing']";

    public List<Integer> parse(final WebDriver htmlPage) {
        return getIds(htmlPage, FLAT_ID_EXPRESSION, "@data-id");
    }

}
