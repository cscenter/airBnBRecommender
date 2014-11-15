package ru.cscenter.practice.recsys;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import ru.cscenter.practice.recsys.enums.PropertyType;
import ru.cscenter.practice.recsys.exceptions.NoAreasException;
import ru.cscenter.practice.recsys.exceptions.TooManyAreasException;

import java.util.*;

public class FlatParser extends Parser {

    private static final String AMENITIES_EXPRESSION = "//div[@class='expandable-content-full']//div[@class='col-10 col-middle']/span[strong]";
    private static final String DESCRIPTION_EXPRESSION = "//div[@class='expandable-content expandable-content-long']";
    private static final String LOCATION_EXPRESSION = "//a[@href='#neighborhood'][@class='link-reset']";
    private static final String PRICE_EXPRESSION = "//div[@id = 'price_amount']";
    private static final String TITLE_EXPRESSION = "//h1[@id = 'listing_name']";
    private static final String LISTING_EXPRESSION = "//div[@class='col-9']/div[@class='row']/div[@class='col-6']/div";
    private static final String ID_EXPRESSION = "//input[@id='hosting_id'][@name = 'hosting_id']";
    private static final String USER_IDS_EXPRESSION = "//div[@class='col-3']/div[@class='pull-right']/" +
            "div[@class='name text-center']/a";

    private static final String COUNT_REVIEW_EXPRESSION = "//a[@href='#reviews']/span";

    private static final String COUNT_RATING_EXPRESSION =
            "//div[@id='reviews']//div[@class='col-8']/div[@class = 'panel-body']"
                    + "/h4[@class='row-space-4']/div[@class='star-rating']/div[@class = 'foreground']";

    private static final String LOAD_MORE_COMMENTS = "//li[@class='next next_page']/a";
    private static final String LOAD_AMENITIES = "//div[@class='expandable-content-summary']//a[@class='expandable-trigger-more']";
    private static final Logger logger = Logger.getLogger(FlatParser.class.getName());


    public FlatParser() {
    }

    public Flat parse(final WebDriver htmlPage) {

        if (htmlPage == null) {
            throw new IllegalArgumentException("html page can't be null");
        }


        Flat.FlatBuilder flatBuilder = new Flat.FlatBuilder();

        setAmenities(getAmenities(htmlPage), flatBuilder);
        setLocation(htmlPage, flatBuilder);

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

    public static List<String> getAmenities(final WebDriver htmlPage) {
        clickAndWait(htmlPage, LOAD_AMENITIES);

        List<String> amenities = getFeatures(htmlPage, AMENITIES_EXPRESSION);
        List<String> result = new ArrayList<>();

        for (String amenity : amenities) {
            result.add(removeSkipSymbols(amenity));
        }
        return result;
    }

    public static String getDescription(final WebDriver htmlPage) {
        String description = "";

        try {
            description = removeSkipSymbols(getFeature(htmlPage, DESCRIPTION_EXPRESSION));
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + " description");
        }

        return description;
    }

    public static String getLocation(final WebDriver htmlPage) {
        String location = "";

        try {
            location = getFeature(htmlPage, LOCATION_EXPRESSION);
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + " location");
        }

        return location;
    }

    public static int getPricePerNight(final WebDriver htmlPage) {
        String price = "";
        try {
            price = getFeature(htmlPage, PRICE_EXPRESSION);
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + "Price");
        }

        return getNumber(price);
    }

    public static Currency getCurrency(final WebDriver htmlPage) {

        String currency = "";
        try {
            currency = getFeature(htmlPage, PRICE_EXPRESSION);
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + "Currency");
        }

        if (currency.contains("$"))
            return Currency.getInstance("USD");
        else if (currency.contains("p") || currency.contains("р")) //the second p is in Russian
            return Currency.getInstance("RUB");

        return null;
    }

    public static String getFlatTitle(final WebDriver htmlPage) {
        String title = "";
        try {
            title = getFeature(htmlPage, TITLE_EXPRESSION);
        } catch (TooManyAreasException | NoAreasException e) {
            logger.debug(e.getMessage() + " title");
        }

        return removeSkipSymbols(title);
    }

    private static String getListing(final WebDriver htmlPage, final String expression) {
        final List<String> listing = getFeatures(htmlPage, LISTING_EXPRESSION);

        for (String currentListing : listing) {
            if (removeSkipSymbols(currentListing).contains(expression))
                return currentListing;
        }

        logger.debug("No listings with such expression " + expression);
        return "";
    }

    public static int getQuantityBedrooms(final WebDriver htmlPage) {
        return getNumber(getListing(htmlPage, "Bedrooms:"));
    }

    public static int getQuantityBathrooms(final WebDriver htmlPage) {
        return getNumber(getListing(htmlPage, "Bathrooms:"));
    }

    public static int getQuantityBeds(final WebDriver htmlPage) {
        return getNumber(getListing(htmlPage, "Beds:"));
    }

    public static int getQuantityAccommodates(final WebDriver htmlPage) {
        return getNumber(getListing(htmlPage, "Accommodates:"));
    }

    public static PropertyType getPropertyType(final WebDriver htmlPage) {
        return PropertyType.getPropertyType(getListing(htmlPage, "Property type:").replaceAll("Property type: ", ""));
    }

    public static int getId(final WebDriver htmlPage) {
        String id = null;
        try {
            id = getFeature(htmlPage, ID_EXPRESSION, "value");
        } catch (TooManyAreasException | NoAreasException e) {
            logger.debug(e.getMessage() + " id");
        }

        return getNumber(id);
    }

    public static int getCountReviews(final WebDriver htmlPage) {
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
        final int countStars = countFeatures(htmlPage, COUNT_RATING_EXPRESSION + "/i[@class='icon icon-pink icon-beach icon-star']");
        final int countHalfStars = countFeatures(htmlPage, COUNT_RATING_EXPRESSION + "/i[@class='icon icon-pink icon-beach icon-star-half']");
        return 2 * countStars + countHalfStars;
    }

    private static void setAmenities(final List<String> amenities, final Flat.FlatBuilder builder) {
        final HashSet<String> amenitiesSet = new HashSet<>();
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

    private static void setLocation(final WebDriver htmlPage, final Flat.FlatBuilder builder) {
        final String location = getLocation(htmlPage);

        if (location.equals(""))
            return;

        List<String> locationParts = Arrays.asList(location.split(","));

        if (locationParts.size() == 3) {
            builder.setCity(removeSkipSymbols(locationParts.get(0)));
            builder.setDistrict(removeSkipSymbols(locationParts.get(1)));
            builder.setCountry(removeSkipSymbols(locationParts.get(2)));
        } else if (locationParts.size() == 2) {
            builder.setDistrict(removeSkipSymbols(locationParts.get(0)));
            builder.setCountry(removeSkipSymbols(locationParts.get(1)));
        } else {
            builder.setCountry(removeSkipSymbols(locationParts.get(1)));
        }

    }

    public static List<Integer> getUserIdsFromComments(final WebDriver htmlPage) {

        final List<Integer> result = new ArrayList<>();

        List<String> userIds;
        do {
            userIds = getFeatures(htmlPage, USER_IDS_EXPRESSION, "href");
            for (String currentUserId : userIds)
                result.add(getNumber(currentUserId));
        } while (Parser.clickAndWait(htmlPage, LOAD_MORE_COMMENTS));

        return result;
    }

}