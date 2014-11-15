package testing;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import exceptions.IncorrectQuantityTests;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
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

        switch (objectName){
            case "flat" :
                parser = new FlatParser();
                break;
            case "user" :
                parser = new UserParser();
                break;
            case "flatpage" :
                parser = new FlatPageParser();
                break;
            default:
                throw new IllegalStateException("Parser doesn't exist for this " + objectName + "expression");
        }


        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.WARNING);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.WARNING);

        HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver(BrowserVersion.FIREFOX_24);
        htmlUnitDriver.setJavascriptEnabled(true);
        webDriver = htmlUnitDriver;
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        webDriver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
        webDriver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        webDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        webDriver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);

    }

    public  boolean doTest(final String methodName) throws IncorrectQuantityTests {

        try {

            Method method = parser.getClass().getMethod(methodName, WebDriver.class);

            final String[] answers = HtmlResource.getAnswers(objectName, methodName);
            final List<String> addressesOfObjects = HtmlResource.getSample(objectName);

            if (answers.length != addressesOfObjects.size()) {
                throw new IncorrectQuantityTests("Quantity of html objects and answers don't match " + answers.length + " " + addressesOfObjects.size());
            }
            int flatIdposition = 0;
            for (String currentPage : addressesOfObjects) {
                webDriver.get(currentPage);
                //Thread.sleep(5000);
                Object sampleAnswer = method.invoke(parser,webDriver);

                if (!sampleAnswer.toString().equals(answers[flatIdposition])) {
                    System.out.println(objectName + " : " + methodName + " : " + sampleAnswer.toString() + " " + answers[flatIdposition]);
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
