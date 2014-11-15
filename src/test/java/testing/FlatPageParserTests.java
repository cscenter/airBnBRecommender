package testing;

import exceptions.IncorrectQuantityTests;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FlatPageParserTests {

    private TesterOfParser tester = new TesterOfParser("flatpage");

    @Test
    public void parseTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("parse"), true);
    }

}
