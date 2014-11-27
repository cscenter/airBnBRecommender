package ru.cscenter.practice.recsys;


import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;


public class FlatPageParser extends Parser {

    private static final String FLAT_ID_EXPRESSION = "//div[@class='col-6 row-space-1']" +
            "/div[@class = 'listing']";

    private static final String LOAD_MORE_FLATS = "//li[@class='next next_page']/a";
    private static final Logger logger = org.apache.log4j.Logger.getLogger(FlatParser.class.getName());

    public FlatPageParser(WebDriver htmlPage){
        super(htmlPage);
    }

    public List<Integer> parse() {
        final List<Integer> result = new ArrayList<>();
        List<String> flatIds;

        do {
            flatIds = getFeatures(FLAT_ID_EXPRESSION, "data-id");
            for (String currentId : flatIds) {
                result.add(getNumber(currentId));
            }
        } while (clickAndWait(LOAD_MORE_FLATS));

        logger.debug("got  user's flats ids: " + result.toString());

        return result;
    }

}
