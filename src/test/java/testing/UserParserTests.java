package testing;

import exceptions.IncorrectQuantityTests;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserParserTests {

    private TesterOfParser tester = new TesterOfParser("user");

    @Test
    public void getIdTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getId"), true);
    }

    @Test
    public void getLanguagesTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getLanguages"), true);
    }

   @Test
    public void getCountReviewsFromHostTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getCountReviewsFromHost"), true);
    }

    @Test
    public void getUserHostIdsFromCommentsTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getUserHostIdsFromComments"), true);
    }

    @Test
    public void hasFlatsTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("hasFlats"), true);
    }

    @Test
    public void parseTest() throws IncorrectQuantityTests {

    }



}
