package ru.cscenter.practice.recsys;


import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import ru.cscenter.practice.recsys.Exceptions.NoAreasException;
import ru.cscenter.practice.recsys.Exceptions.TooManyAreasException;

import java.util.ArrayList;

public abstract class Parser<T> {
    private final TagNode htmlPage;

    Parser(TagNode htmlPage) {
        if (htmlPage == null) {
            throw new IllegalArgumentException("html page can't be null");
        }
        this.htmlPage = htmlPage;
    }

    public abstract T parse();

    protected String[] getFeatures(String expression) {

        if (expression == null)
            return new String[]{};

        Object[] items;

        try {
            items = htmlPage.evaluateXPath(expression);

        } catch (XPatherException e) {
            throw new IllegalArgumentException(expression + "is invalid and invokes error");
        }

        String[] result = new String[items.length];

        for (int i = 0; i < items.length; ++i) {
            if (items[i] instanceof TagNode)
                result[i] = ((TagNode) items[i]).getText().toString();
            else
                result[i] = (String) items[i];
        }
        return result;
    }

    protected String getFeature(String expression) throws TooManyAreasException, NoAreasException {
        String[] result = getFeatures(expression);
        if (result.length > 1)
            throw new TooManyAreasException("html file contains more than one areas that fit for object");
        if (result.length == 0)
            throw new NoAreasException("html file contains no areas that fit for object");

        return result[0];
    }

    protected int countFeatures(String expression) {

        String[] features = getFeatures(expression);
        return features.length;
    }


    protected int getNumber(String string) // returns the first number in string
    {
        if (string == null || string.equals(""))
            return 0;

        int position = 0, result = 0;

        while ((position < string.length()) && (string.charAt(position) > '9' || string.charAt(position) < '0'))
            ++position;

        while ((position < string.length()) && (string.charAt(position) <= '9') && (string.charAt(position) >= '0')) {
            result = result * 10 + string.charAt(position) - '0';
            ++position;
        }
        return result;
    }

    protected static String removeSkipSymbols(CharSequence string) {

        if (string == null || string == "")
            return "";

        StringBuilder builder = new StringBuilder();

        int leftPosition = 0, rightPosition = string.length() - 1;

        char currentChar = string.charAt(leftPosition);
        while (currentChar == ' ' || currentChar == '\n') {
            currentChar = string.charAt(++leftPosition);
        }

        currentChar = string.charAt(rightPosition);
        while (currentChar == ' ' || currentChar == '\n') {
            currentChar = string.charAt(--rightPosition);
        }


        for (int i = leftPosition; i <= rightPosition; ++i)
            builder.append(string.charAt(i));


        return builder.toString();
    }

    protected ArrayList<Integer> getIds(String expression) {
        final String[] userIds = getFeatures(expression);
        ArrayList<Integer> result = new ArrayList<>();

        for (String currentUserId : userIds)
            result.add(getNumber(currentUserId));

        return result;
    }

}
