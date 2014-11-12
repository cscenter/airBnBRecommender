import exceptions.IncorrectQuantityTests;
import org.apache.log4j.Logger;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.cscenter.practice.recsys.FlatParser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class FlatParserTests {

    private static final Logger logger = Logger.getLogger(FlatParser.class.getName());
    private static final FlatParser parser = new FlatParser();
    private static final HtmlCleaner htmlCleaner = new HtmlCleaner();
    //private WebDriver webDriver = new FirefoxDriver(new DesiredCapabilities());

    public boolean doTest(final String objectName, final String methodName) throws IncorrectQuantityTests{

        try {

            Method method = parser.getClass().getMethod(methodName, TagNode.class);

            final String[] answers = HtmlResource.getAnswers(objectName, methodName);
            final List<File> htmlObjects = HtmlResource.getSample(objectName);

            if(answers.length != htmlObjects.size()) {
                throw new IncorrectQuantityTests("Quantity of html objects and answers don't match " + answers.length + " " + htmlObjects.size());
            }

            try {
                int flatIdposition = 0;
                for (File currentPage : htmlObjects ) {
                    if(!method.invoke(parser, htmlCleaner.clean(currentPage)).toString().equals(answers[flatIdposition++]))
                        return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
                return false;
            }


        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
            return true;
        }

        return true;

    }

    @Test
    public void getPricePerNightTest() throws IncorrectQuantityTests {
        Assert.assertEquals(doTest("flat", "getPricePerNight"), true);
    }

    @Test
    public void getIdTest() throws IncorrectQuantityTests {
        Assert.assertEquals(doTest("flat", "getId"), true);
    }

    @Test
    public void getAccomodatesTest() throws IncorrectQuantityTests {
        Assert.assertEquals(doTest("flat", "getQuantityAccommodates"), true);
    }

    @Test
    public void getCountReviewsTest() throws IncorrectQuantityTests {
        Assert.assertEquals(doTest("flat","getCountReviews"),true);
    }

    @Test
    public void getRating() throws IncorrectQuantityTests {
        Assert.assertEquals(doTest("flat","getRating"),true);
    }

    @Test
    public void getPropertyType() throws IncorrectQuantityTests {
        Assert.assertEquals(doTest("flat","getPropertyType"), true);
    }

    @Test
    public void getQuantityBedsTest() throws  IncorrectQuantityTests {
        Assert.assertEquals(doTest("flat","getQuantityBeds"),true);
    }

    @Test
    public void getQuantityBathroomsTest() throws IncorrectQuantityTests {
        Assert.assertEquals(doTest("flat", "getQuantityBathrooms"), true);
    }

    @Test
    public void getQuantityBedroomsTest() throws IncorrectQuantityTests {
        Assert.assertEquals(doTest("flat", "getQuantityBedrooms"), true);
    }

    @Test
    public void getTitleTest() throws IncorrectQuantityTests {
        Assert.assertEquals(doTest("flat", "getFlatTitle"), true);
    }

    @Test
    public void getCurrencyTest() throws IncorrectQuantityTests {
        Assert.assertEquals(doTest("flat", "getCurrency"), true);
    }
}
