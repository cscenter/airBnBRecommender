package ru.cscenter.practice.recsys;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.cscenter.practice.recsys.exceptions.NoAreasException;
import ru.cscenter.practice.recsys.exceptions.TooManyAreasException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class Parser<T> {

    private final WebDriver htmlPage;
    private final int TIMEOUT_FOR_FEATURES = 1; // in seconds
    private final int TIMEOUT_FOR_BUTTON = 1; // in seconds
    private final int FREQUENCY = 200; // in milliseconds
    public abstract T parse();

    public Parser(WebDriver htmlPage) {

        if(htmlPage == null)
            throw new IllegalArgumentException("driver's provided html page is null");

        this.htmlPage = htmlPage;
    }

    public List<String> getFeatures(final String expression, String attribute) {
        if (expression == null)
            return new ArrayList<>();


        List<WebElement> items;
        final List<String> result = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(htmlPage, TIMEOUT_FOR_FEATURES, FREQUENCY);

        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(expression)));
            items = htmlPage.findElements(By.xpath(expression));
        } catch (NoSuchElementException | ElementNotVisibleException | TimeoutException e) {
            return result;
        }


        for (WebElement item : items) {
            if (attribute != null)
                result.add(item.getAttribute(attribute));
            else
                result.add(item.getText());
        }

        return result;

    }

    public List<String> getFeatures(final String expression) {
        return getFeatures(expression, null);
    }


    public String getFeature(final String expression, final String attribute) throws TooManyAreasException, NoAreasException {
        final List<String> result = getFeatures(expression, attribute);
        if (result.size() > 1)
            throw new TooManyAreasException("html file contains more than one areas that fit for object");
        if (result.size() == 0)
            throw new NoAreasException("html file contains no areas that fit for object");

        return result.get(0);
    }

    public String getFeature(final String expression) throws TooManyAreasException, NoAreasException {
        return getFeature(expression, null);
    }

    public int countFeatures(final String expression, final String attribute) {

        final List<String> features = getFeatures(expression, attribute);
        return features.size();
    }

    public int countFeatures(final String expression) {

        return countFeatures(expression, null);
    }

    public static int getNumber(final String string) // if string contains more than one number then method return wrong number
    {
        if (string == null)
            return 0;
        final String number = string.replaceAll("\\D+", "");
        return number.matches("\\d+") ? Integer.parseInt(number) : 0;
    }

    public static String removeSkipSymbols(final String string) {

        if (string == null)
            return "";

        final List<String> words = Arrays.asList(string.split("\\s+"));
        final StringBuilder builder = new StringBuilder();

        for (String currentWord : words) {
            if (currentWord.matches("\\S+"))
                builder.append(currentWord).append(" ");
        }

        if (builder.length() > 0)
            builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }

    public boolean clickAndWait(String expression) {
        try {

            WebElement nextPage = new WebDriverWait(htmlPage, TIMEOUT_FOR_BUTTON, FREQUENCY)
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath(expression)));

            nextPage.click();
            htmlPage.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
            htmlPage.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
            htmlPage.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            Thread.sleep(2000);

        } catch (NoSuchElementException | ElementNotVisibleException | TimeoutException  e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
