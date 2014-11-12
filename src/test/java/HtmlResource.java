import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.cscenter.practice.recsys.Parser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class HtmlResource {

    private HtmlResource() {}

    private static final String RESOURCES_ADDRESS = "/Users/antonkozmirchuk/IdeaProjects/Recomended System Airbnb/src/test/resources";
    private static HtmlCleaner htmlCleaner = new HtmlCleaner();
    private static File resourcesFile = new File(RESOURCES_ADDRESS + "/resources.xml");

    public static List<File> getSample(final String sampleType) {
        final ArrayList<File> result = new ArrayList<>();
        final File[] files = new File(RESOURCES_ADDRESS).listFiles();

        try {
            final TagNode resources = htmlCleaner.clean(new File(RESOURCES_ADDRESS + "/resources.xml"));
            final String[] sampleFiles = Parser.getFeatures0(resources, "//resources/samples/" + sampleType + "s/name");

            if (files == null)
                return result;

            for (File currentFile : files)
                for (String currentSampleFile : sampleFiles)
                    if (currentFile.getName().equals(currentSampleFile + ".html"))
                        result.add(currentFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }


    public static String[] getAnswers(final String objectName, final String methodName) {

        if (objectName == null || methodName == null)
            return new String[]{};

        TagNode answers = null;

        final String patternExpression = "//resources/answers/" + objectName + "s/" + objectName + "/" + methodName.replace("get","").toLowerCase();

        try {
            answers = htmlCleaner.clean(resourcesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
       return Parser.getFeatures0(answers, patternExpression);
    }

}
