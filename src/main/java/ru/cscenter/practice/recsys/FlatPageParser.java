package ru.cscenter.practice.recsys;


import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;


public class FlatPageParser extends Parser {

    private static final String FLAT_ID_EXPRESSION = "//div[@class='col-6 row-space-1']" +
            "/div[@class = 'listing']";

    private static final String LOAD_MORE_FLATS = "//li[@class='next next_page']/a";

    public List<Integer> parse(final WebDriver htmlPage) {
        final List<Integer> result = new ArrayList<>();
        String[] flatIds;

        do {
            flatIds = getFeatures(htmlPage, FLAT_ID_EXPRESSION, "data-id");
            for(String currentId : flatIds) {
                result.add(getNumber(currentId));
            }

            try {
                WebElement nextPage = htmlPage.findElement(By.xpath(LOAD_MORE_FLATS));
                nextPage.click();
            } catch (NoSuchElementException e) {
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }while(true);

        return result;
    }

}
