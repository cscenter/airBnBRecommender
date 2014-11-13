package ru.cscenter.practice.recsys;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.cscenter.practice.recsys.exceptions.NoAreasException;
import ru.cscenter.practice.recsys.exceptions.TooManyAreasException;

import java.util.List;

public abstract class Parser<T> {

    public abstract T parse(final WebDriver htmlPage);

    public static String[] getFeatures(final WebDriver htmlPage, final String expression, String attribute) {
        if (expression == null)
            return new String[]{};
        
        List<WebElement> items = htmlPage.findElements(By.xpath(expression));

        final String[] result = new String[items.size()];
        
        for (int i = 0; i < items.size(); ++i) {
            if(attribute != null)
                result[i] = items.get(i).getAttribute(attribute);
            else
                result[i] = items.get(i).getText();
        }
        return result;
        
    }

    public static String[] getFeatures(final WebDriver htmlPage, final String expression) {
        return getFeatures(htmlPage, expression, null);
    }


    public static String getFeature(final WebDriver htmlPage, final String expression, final String attribute) throws TooManyAreasException, NoAreasException {
        final String[] result = getFeatures(htmlPage, expression, attribute);
        if (result.length > 1)
            throw new TooManyAreasException("html file contains more than one areas that fit for object");
        if (result.length == 0)
            throw new NoAreasException("html file contains no areas that fit for object");

        return result[0];
    }
    public static String getFeature(final WebDriver htmlPage, final String expression) throws TooManyAreasException, NoAreasException  {
        return getFeature(htmlPage, expression, null);
    }

    public static int countFeatures(final WebDriver htmlPage, final String expression, final String attribute) {

        final String[] features = getFeatures(htmlPage, expression, attribute);
        return features.length;
    }
    public static int countFeatures(final WebDriver htmlPage, final String expression) {

        return countFeatures(htmlPage, expression, null);
    }

    public static int getNumber(final String string) // if string contains more than one number then method return wrong number
    {
        if (string == null)
            return 0;
        final String number = string.replaceAll("\\D+","");
        return number.matches("\\d+") ? Integer.parseInt(number) : 0 ;
    }

    public static String removeSkipSymbols(final String string) {

        if (string == null)
            return "";

        final String[] words = string.split("\\s+");
        final StringBuilder builder = new StringBuilder();

        for(String currentWord : words) {
            if(currentWord.matches("\\S+"))
                builder.append(currentWord).append(" ");
        }

        if(builder.length() > 0)
            builder.deleteCharAt(builder.length()-1);

        return builder.toString();
    }

}
