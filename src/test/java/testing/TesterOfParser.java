package testing;

import exceptions.IncorrectQuantityTests;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.cscenter.practice.recsys.FlatPageParser;
import ru.cscenter.practice.recsys.FlatParser;
import ru.cscenter.practice.recsys.Parser;
import ru.cscenter.practice.recsys.UserParser;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;


class TesterOfParser {

    private final Logger logger = Logger.getLogger(Parser.class.getName());
    private final Parser parser;
    private final WebDriver webDriver;
    private final String objectName;

    public TesterOfParser(String objectName) {
        this.objectName = objectName;

        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.WARNING);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.WARNING);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability("takesScreenshot", false);

        webDriver = new FirefoxDriver(capabilities);
        webDriver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);
        webDriver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        switch (objectName){
            case "flat" :
                parser = new FlatParser(webDriver);
                break;
            case "user" :
                parser = new UserParser(webDriver);
                break;
            case "flatpage" :
                parser = new FlatPageParser(webDriver);
                break;
            default:
                throw new IllegalStateException("Parser doesn't exist for this " + objectName + "expression");
        }



    }

    public  boolean doTest(final String methodName) throws IncorrectQuantityTests {

        try {

            Method method = parser.getClass().getMethod(methodName);

            final String[] answers = HtmlResource.getAnswers(objectName, methodName);
            final List<String> addressesOfObjects = HtmlResource.getSample(objectName);

            if (answers.length != addressesOfObjects.size()) {
                throw new IncorrectQuantityTests("Quantity of html objects and answers don't match " + answers.length + " " + addressesOfObjects.size());
            }
            int flatIdposition = 0;
            for (String currentPage : addressesOfObjects) {
                webDriver.get(currentPage);
                Object sampleAnswer = method.invoke(parser);

                logger.debug(objectName + " : " + methodName + " : " + "from airbnb: " + sampleAnswer.toString() + " from answers: " + answers[flatIdposition]);

                if (!sampleAnswer.toString().equals(answers[flatIdposition])) {

                    return false;
                }

                flatIdposition ++;
            }

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
            return true;
        }

        return true;

    }

    public boolean matchObjects(Object a, Object b) {

        if(!a.getClass().equals(b.getClass()))
            throw new IllegalArgumentException("Objects have different types " + a.toString() + " " + b.toString());

        Field[] aFields = a.getClass().getDeclaredFields();
        Field[] bFields = b.getClass().getDeclaredFields();


       for(int i = 0; i < aFields.length; ++i)
           if (!aFields[i].equals(bFields[i]))
               return false;

        return true;
    }
}
