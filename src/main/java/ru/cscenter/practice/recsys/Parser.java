package ru.cscenter.practice.recsys;

import org.openqa.selenium.*;
import ru.cscenter.practice.recsys.exceptions.NoAreasException;
import ru.cscenter.practice.recsys.exceptions.TooManyAreasException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Parser<T> {

    //TODO: Webdriver сделать полем класса парсер, чтобы не передавать в каждый метод
    //TODO: А методы будут не статические

    public abstract T parse(final WebDriver htmlPage);

    public static List<String> getFeatures(final WebDriver htmlPage, final String expression, String attribute) {
        if (expression == null)
            return new ArrayList<>();

        List<WebElement> items = htmlPage.findElements(By.xpath(expression));

        final List<String> result = new ArrayList<>();

        for (WebElement item : items) {
            if (attribute != null)
                result.add(item.getAttribute(attribute));
            else
                result.add(item.getText());
        }
        return result;

    }

    public static List<String> getFeatures(final WebDriver htmlPage, final String expression) {
        return getFeatures(htmlPage, expression, null);
    }


    public static String getFeature(final WebDriver htmlPage, final String expression, final String attribute) throws TooManyAreasException, NoAreasException {
        final List<String> result = getFeatures(htmlPage, expression, attribute);
        if (result.size() > 1)
            throw new TooManyAreasException("html file contains more than one areas that fit for object");
        if (result.size() == 0)
            throw new NoAreasException("html file contains no areas that fit for object");

        return result.get(0);
    }

    public static String getFeature(final WebDriver htmlPage, final String expression) throws TooManyAreasException, NoAreasException {
        return getFeature(htmlPage, expression, null);
    }

    public static int countFeatures(final WebDriver htmlPage, final String expression, final String attribute) {

        final List<String> features = getFeatures(htmlPage, expression, attribute);
        return features.size();
    }

    public static int countFeatures(final WebDriver htmlPage, final String expression) {

        return countFeatures(htmlPage, expression, null);
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

    public static boolean clickAndWait(WebDriver htmlPage, String expression) {
        try {
            WebElement nextPage = htmlPage.findElement(By.xpath(expression));
            nextPage.click();
            Thread.sleep(100);
        } catch (NoSuchElementException | ElementNotVisibleException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
