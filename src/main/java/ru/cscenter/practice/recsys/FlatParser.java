package ru.cscenter.practice.recsys;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import ru.cscenter.practice.recsys.enums.PropertyType;
import ru.cscenter.practice.recsys.exceptions.NoAreasException;
import ru.cscenter.practice.recsys.exceptions.TooManyAreasException;

import java.util.Currency;
import java.util.HashSet;
import java.util.List;

public class FlatParser extends Parser {

    private static final String AMENITIES_EXPRESSION = "//div[@class='row row-condensed row-table row-space-1']/div[@class='col-10 col-middle']/span";
    private static final String DESCRIPTION_EXPRESSION = "//div[@class='expandable-content expandable-content-long']";
    private static final String LOCATION_EXPRESSION = "//a[@href='#neighborhood'][@class='link-reset']";
    private static final String PRICE_EXPRESSION = "//div[@id = 'price_amount']";
    private static final String TITLE_EXPRESSION = "//h1[@id = 'listing_name']";
    private static final String LISTING_EXPRESSION = "//div[@class='col-9']/div[@class='row']/div[@class='col-6']/div";
    private static final String ID_EXPRESSION = "//input[@id='hosting_id'][@name = 'hosting_id']";
    private static final String USER_IDS_EXPRESSION = "//div[@class='col-3']/div[@class='pull-right']/" +
            "div[@class='name text-center']/a";

    private static final String COUNT_REVIEW_EXPRESSION = "//div[@id='reviews']//div[@class='col-8']/div[@class = 'panel-body']"
            + "/h4[@class='row-space-4']";
    private static final String COUNT_RATING_EXPRESSION =
            COUNT_REVIEW_EXPRESSION + "/div[@class='star-rating']/div[@class = 'foreground']";

    private static final Logger logger = Logger.getLogger(FlatParser.class.getName());

    public FlatParser() {}

    public Flat parse(WebDriver htmlPage) {

        if (htmlPage == null) {
            throw new IllegalArgumentException("html page can't be null");
        }


        Flat.FlatBuilder flatBuilder = new Flat.FlatBuilder();

        setAmenities(getAmenities(htmlPage), flatBuilder);
        setLocation(htmlPage,flatBuilder);

        flatBuilder.setDescription(getDescription(htmlPage));
        flatBuilder.setPricePerNight(getPricePerNight(htmlPage));
        flatBuilder.setCurrency(getCurrency(htmlPage));
        flatBuilder.setTitle(getFlatTitle(htmlPage));
        flatBuilder.setBedrooms(getQuantityBedrooms(htmlPage));
        flatBuilder.setBeds(getQuantityBeds(htmlPage));
        flatBuilder.setBathrooms(getQuantityBathrooms(htmlPage));
        flatBuilder.setAccomodates(getQuantityAccommodates(htmlPage));
        flatBuilder.setTypeProperty(getPropertyType(htmlPage));
        flatBuilder.setCountReview(getCountReviews(htmlPage));
        flatBuilder.setRatingValue(getRating(htmlPage));
        flatBuilder.setId(getId(htmlPage));

        return flatBuilder.build();

    }

    public static String[] getAmenities(WebDriver htmlPage) {
        String[] result = getFeatures(htmlPage, AMENITIES_EXPRESSION);

        for (int i = 0; i < result.length; ++i) {
            result[i] = removeSkipSymbols(result[i]);
        }
        return result;
    }

    public static String getDescription(WebDriver htmlPage) {
        String description = "";

        try {
            description = removeSkipSymbols(getFeature(htmlPage,DESCRIPTION_EXPRESSION));
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + " description");
        }

        return description;
    }

    public static String getLocation(WebDriver htmlPage) {
        String location = "";

        try {
            location = getFeature(htmlPage, LOCATION_EXPRESSION);
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + " location");
        }

        return location;
    }

    public static int getPricePerNight(WebDriver htmlPage) {
        String price = "";
        try {
            price = getFeature(htmlPage, PRICE_EXPRESSION);
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + "Price");
        }

        return getNumber(price);
    }

    public static Currency getCurrency(WebDriver htmlPage) {

        String currency = "";
        try {
            currency = getFeature(htmlPage, PRICE_EXPRESSION);
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + "Currency");
        }

        if (currency.contains("$"))
            return Currency.getInstance("USD");

        return null;
    }

    public static String getFlatTitle(WebDriver htmlPage) {
        String title = "";
        try {
            title = getFeature(htmlPage, TITLE_EXPRESSION);
        } catch (TooManyAreasException | NoAreasException e) {
            logger.debug(e.getMessage() + " title");
        }

        return removeSkipSymbols(title);
    }

    public static String getListing(WebDriver htmlPage, String expression) {
        final String[] listing = getFeatures(htmlPage, LISTING_EXPRESSION);

        for (String currentListing : listing) {
            if (removeSkipSymbols(currentListing).contains(expression))
                return currentListing;
        }

        logger.debug("No listings with such expression " + expression);
        return "";
    }

    public static int getQuantityBedrooms(WebDriver htmlPage) {
        return getNumber(getListing(htmlPage, "Bedrooms:"));
    }

    public static int getQuantityBathrooms(WebDriver htmlPage) {
        return getNumber(getListing(htmlPage, "Bathrooms:"));
    }

    public static int getQuantityBeds(WebDriver htmlPage) {
        return getNumber(getListing(htmlPage, "Beds:"));
    }

    public static int getQuantityAccommodates(WebDriver htmlPage) {
        return getNumber(getListing(htmlPage,"Accommodates:"));
    }

    public static PropertyType getPropertyType(WebDriver htmlPage) {
        return PropertyType.getPropertyType(getListing(htmlPage, "Property type:").replaceAll("Property type: ", ""));
    }

    public static int getId(WebDriver htmlPage) {
        String id = null;
        try {
            id = getFeature(htmlPage, ID_EXPRESSION, "@value");
        } catch (TooManyAreasException | NoAreasException e) {
            logger.debug(e.getMessage() + " id");
        }

        return getNumber(id);
    }

    public static int getCountReviews(WebDriver htmlPage) {
        String result = "";

        try {
            result = getFeature(htmlPage, COUNT_REVIEW_EXPRESSION);
        } catch (TooManyAreasException e) {
            logger.debug(e.getMessage() + " count review");
        } catch (NoAreasException e) {
             // ignore because exist flats without reviews
        }

        return getNumber(result);
    }

    public static int getRating(WebDriver htmlPage) {
        int countStars = countFeatures(htmlPage, COUNT_RATING_EXPRESSION + "/i[@class='icon icon-pink icon-beach icon-star']");
        int countHalfStars = countFeatures(htmlPage, COUNT_RATING_EXPRESSION + "/i[@class='icon icon-pink icon-beach icon-star-half']");
        return 2 * countStars + countHalfStars;
    }

    private static void setAmenities(String[] amenities, Flat.FlatBuilder builder) {
        HashSet<String> amenitiesSet = new HashSet<>();
        for (String currentAmenities : amenities)
            amenitiesSet.add(currentAmenities.toLowerCase());

        final boolean active = true;

        if (amenitiesSet.contains("tv"))
            builder.setTv(active);
        if (amenitiesSet.contains("cable tv"))
            builder.setCableTv(active);
        if (amenitiesSet.contains("air conditioning"))
            builder.setAirConditioning(active);
        if (amenitiesSet.contains("heating"))
            builder.setHeating(active);
        if (amenitiesSet.contains("kitchen"))
            builder.setKitchen(active);
        if (amenitiesSet.contains("internet"))
            builder.setInternet(active);
        if (amenitiesSet.contains("wireless internet"))
            builder.setWirelessInternet(active);
        if (amenitiesSet.contains("hot tub"))
            builder.setHotTub(active);
        if (amenitiesSet.contains("washer"))
            builder.setWasher(active);
        if (amenitiesSet.contains("pool"))
            builder.setPool(active);
        if (amenitiesSet.contains("dryer"))
            builder.setDryer(active);
        if (amenitiesSet.contains("free parking on premises"))
            builder.setFreeParkingOnPremises(active);
        if (amenitiesSet.contains("gym"))
            builder.setGym(active);
        if (amenitiesSet.contains("elevator"))
            builder.setElevator(active);
        if (amenitiesSet.contains("indoor fireplace"))
            builder.setInDoorFireplace(active);
        if (amenitiesSet.contains("buzzer/wireless intercom"))
            builder.setBuzzerIntercom(active);
        if (amenitiesSet.contains("doorman"))
            builder.setDoorman(active);
        if (amenitiesSet.contains("shampoo"))
            builder.setShampoo(active);
        if (amenitiesSet.contains("family friendly"))
            builder.setFamilyFriendly(active);
        if (amenitiesSet.contains("smoking allowed"))
            builder.setSmokingAllowed(active);
        if (amenitiesSet.contains("suitable for events"))
            builder.setSuitableForEvents(active);
        if (amenitiesSet.contains("pets allowed"))
            builder.setPetsAllowed(active);
        if (amenitiesSet.contains("pets lived on this property"))
            builder.setPetsLivedOnThisProperty(active);
        if (amenitiesSet.contains("wheelchair accessible"))
            builder.setWheelchairAccessible(active);
        if (amenitiesSet.contains("smoke detector"))
            builder.setSmokeDetector(active);
        if (amenitiesSet.contains("carbon monooxide detector"))
            builder.setCarbonMonoxideDetector(active);
        if (amenitiesSet.contains("first aid kit"))
            builder.setFirstAidKit(active);
        if (amenitiesSet.contains("safety card"))
            builder.setSafetyCard(active);
        if (amenitiesSet.contains("fire extinguiser"))
            builder.setFireExtinguiser(active);

    }

    private  static void setLocation(WebDriver htmlPage, Flat.FlatBuilder builder) {
        String location = getLocation(htmlPage);

        if(location.equals(""))
            return;

        String[] locationParts = location.split(",");

        if(locationParts.length == 3) {
            builder.setCity(removeSkipSymbols(locationParts[0]));
            builder.setDistrict(removeSkipSymbols(locationParts[1]));
            builder.setCountry(removeSkipSymbols(locationParts[2]));
        }
        else if(locationParts.length == 2) {
            builder.setDistrict(removeSkipSymbols(locationParts[0]));
            builder.setCountry(removeSkipSymbols(locationParts[1]));
        }
        else{
            builder.setCountry(removeSkipSymbols(locationParts[0]));
        }

    }

    public List<Integer> getUserIdsFromComments(WebDriver htmlPage) {
        return getIds(htmlPage, USER_IDS_EXPRESSION, "@href");
    }

}