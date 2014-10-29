package ru.cscenter.practice.recsys;

import org.apache.log4j.Logger;
import org.htmlcleaner.TagNode;
import ru.cscenter.practice.recsys.Enums.PropertyType;
import ru.cscenter.practice.recsys.Exceptions.NoAreasException;
import ru.cscenter.practice.recsys.Exceptions.TooManyAreasException;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashSet;

public class FlatParser extends Parser {

    private static final String AMENITIES_EXPRESSION = "//div[@class='col-10 col-middle']/span/strong";
    private static final String DESCRIPTION_EXPRESSION = "//div[@class='expandable-content expandable-content-long']";
    private static final String LOCATION_EXPRESSION = "//a[@href='#neighborhood'][@class='link-reset']";
    private static final String PRICE_EXPRESSION = "//div[@id = 'price_amount']";
    private static final String TITLE_EXPRESSION = "//h1[@id = 'listing_name']";
    private static final String LISTING_EXPRESSION = "//div[@class='col-9']/div[@class='row']/div[@class='col-6']/div";
    private static final String ID_EXPRESSION = "//input[@id='hosting_id'][@name = 'hosting_id']/@value";
    private static final String USER_IDS_EXPRESSION = "//div[@class='col-3']/div[@class='pull-right']/" +
            "div[@class='name text-center']/a/@href";

    private static final String COUNT_REVIEW_EXPRESSION = "//div[@id='reviews']//div[@class='col-8']/div[@class = 'panel-body']"
            + "/h4[@class='row-space-4']";
    private static final String COUNT_RATING_EXPRESSION =
            COUNT_REVIEW_EXPRESSION + "/div[@class='star-rating']/div[@class = 'foreground']";

    private static final Logger logger = Logger.getLogger(FlatParser.class.getName());

    public FlatParser(TagNode htmlPage) {
        super(htmlPage);
    }

    public Flat parse() {
        Flat.FlatBuilder flatBuilder = new Flat.FlatBuilder();

        setAmenities(getAmenities(), flatBuilder);
        flatBuilder.setDescription(getDescription());
        flatBuilder.setLocation(getLocation());
        flatBuilder.setPricePerNight(getPricePerNight());
        flatBuilder.setCurrency(getCurrency());
        flatBuilder.setTitle(getTitle());
        flatBuilder.setBedrooms(getQuantityBedrooms());
        flatBuilder.setBeds(getQuantityBeds());
        flatBuilder.setBathrooms(getQuantityBathrooms());
        flatBuilder.setAccomodates(getQuantityAccommodates());
        flatBuilder.setTypeProperty(getPropertyType());
        flatBuilder.setCountReview(getCountReviews());
        flatBuilder.setRatingValue(getRating());
        flatBuilder.setId(getId());

        return flatBuilder.build();

    }

    private String[] getAmenities() {
        String[] result = getFeatures(AMENITIES_EXPRESSION);

        for (int i = 0; i < result.length; ++i) {
            result[i] = removeSkipSymbols(result[i]);
        }
        return result;
    }

    private String getDescription() {
        String description = "";

        try {
            description = removeSkipSymbols(getFeature(DESCRIPTION_EXPRESSION));
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + " description");
        }

        return description;
    }

    private String getLocation() {
        String location = "";

        try {
            location = getFeature(LOCATION_EXPRESSION);
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + " location");
        }

        return location;
    }

    private int getPricePerNight() {
        String price = "";
        try {
            price = getFeature(PRICE_EXPRESSION);
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + "Price");
        }

        return getNumber(price);
    }

    private Currency getCurrency() {

        String currency = "";
        try {
            currency = getFeature(PRICE_EXPRESSION);
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + "Currency");
        }

        if (currency.contains("$"))
            return Currency.getInstance("USD");

        return null;
    }

    private String getTitle() {
        String title = "";
        try {
            title = getFeature(TITLE_EXPRESSION);
        } catch (TooManyAreasException | NoAreasException e) {
            logger.debug(e.getMessage() + " title");
        }

        return removeSkipSymbols(title);
    }

    private String getListing(String expression) {
        final String[] listing = getFeatures(LISTING_EXPRESSION);

        for (String currentListing : listing) {
            if (removeSkipSymbols(currentListing).contains(expression))
                return currentListing;
        }

        logger.debug("No listings with such expression " + expression);
        return "";
    }

    private int getQuantityBedrooms() {
        return getNumber(getListing("Bedrooms:"));
    }

    private int getQuantityBathrooms() {
        return getNumber(getListing("Bathrooms:"));
    }

    private int getQuantityBeds() {
        return getNumber(getListing("Beds:"));
    }

    private int getQuantityAccommodates() {
        return getNumber(getListing("Accommodates:"));
    }

    private PropertyType getPropertyType() {
        return PropertyType.getPropertyType(getListing("Property type:").replaceAll("Property type: ", ""));
    }

    private int getId() {
        String id = null;
        try {
            id = getFeature(ID_EXPRESSION);
        } catch (TooManyAreasException | NoAreasException e) {
            logger.debug(e.getMessage() + " id");
        }

        return getNumber(id);
    }

    private int getCountReviews() {
        String result = "";

        try {
            result = getFeature(COUNT_REVIEW_EXPRESSION);
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + " count review");
        }

        return getNumber(result);
    }

    private int getRating() {
        int countStars = countFeatures(COUNT_RATING_EXPRESSION + "/i[@class='icon icon-pink icon-beach icon-star']");
        int countHalfStars = countFeatures(COUNT_RATING_EXPRESSION + "/i[@class='icon icon-pink icon-beach icon-star-half']");
        return 2 * countStars + countHalfStars;
    }

    private void setAmenities(String[] amenities, Flat.FlatBuilder builder) {
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

    public ArrayList<Integer> getUserIdsFromComments() {
        return getIds(USER_IDS_EXPRESSION);
    }

}