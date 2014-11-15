package testing;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;


import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

class HtmlResource {

    private HtmlResource() {
    }

    private static final String RESOURCES_ADDRESS = "/Users/antonkozmirchuk/IdeaProjects/Recomended System Airbnb/src/test/resources";
    private static final HtmlCleaner htmlCleaner = new HtmlCleaner();
    private static final File resourcesFile = new File(RESOURCES_ADDRESS + "/resources.xml");

    public static List<String> getSample(final String sampleType) {
        final List<String> result = new ArrayList<>();

        try {
            final TagNode resources = htmlCleaner.clean(new File(RESOURCES_ADDRESS + "/resources.xml"));
            final String[] sampleFiles = getFeaturesFromResource(resources, "//resources/samples/" + sampleType + "s/name");
            Collections.addAll(result, sampleFiles);
        } catch (IOException ignored) {
        }

        return result;

    }


    public static String[] getAnswers(final String objectName, final String methodName) {

        if (objectName == null || methodName == null)
            return new String[]{};

        TagNode answers = null;

        final String patternExpression = "//resources/answers/" + objectName + "s/" + objectName + "/" + methodName.replace("get", "").toLowerCase();

        try {
            answers = htmlCleaner.clean(resourcesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getFeaturesFromResource(answers, patternExpression);
    }

    public static String[] getFeaturesFromResource(final TagNode htmlPage, final String expression) {

        if (expression == null)
            return new String[]{};

        Object[] items;

        try {
            items = htmlPage.evaluateXPath(expression);
        } catch (XPatherException e) {
            throw new IllegalArgumentException(expression + " is invalid and invokes error");
        }

        final String[] result = new String[items.length];

        for (int i = 0; i < items.length; ++i) {
            if (items[i] instanceof TagNode)
                result[i] = ((TagNode) items[i]).getText().toString();
            else
                result[i] = (String) items[i];
        }
        return result;
    }

}
