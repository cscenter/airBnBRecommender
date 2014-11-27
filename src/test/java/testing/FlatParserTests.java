package testing;

import exceptions.IncorrectQuantityTests;
import org.testng.Assert;
import org.testng.annotations.Test;



public class FlatParserTests {

    private TesterOfParser tester = new TesterOfParser("flat");

    //@Test
    public void getPricePerNightTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getPricePerNight"), true);
    }

    @Test
    public void getIdTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getId"), true);
    }

    @Test
    public void getAccomodatesTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getQuantityAccommodates"), true);
    }

    @Test
    public void getCountReviewsTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getCountReviews"), true);
    }

    @Test
    public void getRating() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getRating"), true);
    }

    @Test
    public void getPropertyType() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getPropertyType"), true);
    }

    @Test
    public void getQuantityBedsTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getQuantityBeds"), true);
    }

    @Test
    public void getQuantityBathroomsTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getQuantityBathrooms"), true);
    }

    @Test
    public void getQuantityBedroomsTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getQuantityBedrooms"), true);
    }

    @Test
    public void getTitleTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getFlatTitle"), true);
    }

    @Test
    public void getCurrencyTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getCurrency"), true);
    }

    @Test
    public void getLocationTest() throws IncorrectQuantityTests{
        Assert.assertEquals(tester.doTest("getLocation"), true);
    }

    @Test
    public void getAmenitiesTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getAmenities"), true);
    }

    //@Test
    public void getDescriptionTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getDescription"), true);
    }

    //@Test
    public void getUserIdsFromCommentsTest() throws IncorrectQuantityTests {
        Assert.assertEquals(tester.doTest("getUserIdsFromComments"), true);
    }

    @Test
    public void parseTest() throws IncorrectQuantityTests {

    }

}
