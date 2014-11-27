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


    public FlatParser(WebDriver htmlPage) {
        super(htmlPage);
    }

    public Flat parse() {

        Flat.FlatBuilder flatBuilder = new Flat.FlatBuilder();

        setAmenities(getAmenities(), flatBuilder);
        setLocation(flatBuilder);

        flatBuilder.setDescription(getDescription());
        flatBuilder.setPricePerNight(getPricePerNight());
        flatBuilder.setCurrency(getCurrency());
        flatBuilder.setTitle(getFlatTitle());
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

    public List<String> getAmenities() {
        clickAndWait(LOAD_AMENITIES);
        List<String> amenities = getFeatures(AMENITIES_EXPRESSION);
        List<String> result = new ArrayList<>();

        for (String amenity : amenities) {
            result.add(removeSkipSymbols(amenity));
        }

        logger.debug("got amenities: " + result.toString());

        return result;
    }

    public String getDescription() {
        String description = "";

        try {
            description = removeSkipSymbols(getFeature(DESCRIPTION_EXPRESSION));
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + " from description");
        }

        logger.debug("got description: " +  (description.length() > 0 ? description.substring(0, description.length() % 20) : ""));

        return description;
    }

    public String getLocation() {
        String location = "";

        try {
            location = getFeature(LOCATION_EXPRESSION);
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + " from location");
        }

        logger.debug("got location: " + ((location.length() > 0) ?  location.substring(0, location.length() % 20) : ""));

        return location;
    }

    public int getPricePerNight() {
        String price = "";
        try {
            price = getFeature(PRICE_EXPRESSION);
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + "Price");
        }

        return getNumber(price);
    }

    public String getCurrency() {

        String currency = "";
        try {
            currency = getFeature(PRICE_EXPRESSION);
        } catch (NoAreasException | TooManyAreasException e) {
            logger.debug(e.getMessage() + " from Currency");
        }

        String resultCurrency = "";

        if (currency.contains("$")) {
            resultCurrency = "USD";
        }
        else if (currency.contains("p") || currency.contains("р")) //the second p is in Russian
            resultCurrency = "RUB";

        logger.debug("got Currency: " + resultCurrency);

        return resultCurrency;
    }

    public  String getFlatTitle() {
        String title = "";
        try {
            title = getFeature(TITLE_EXPRESSION);
        } catch (TooManyAreasException | NoAreasException e) {
            logger.debug(e.getMessage() + " from title");
        }

        title = removeSkipSymbols(title);

        logger.debug("got title: " + (title.length() > 0 ? title.substring(0, title.length() % 20) : ""));

        return title;
    }

    private  String getListing(final String expression) {
        final List<String> listing = getFeatures(LISTING_EXPRESSION);

        for (String currentListing : listing) {
            if (removeSkipSymbols(currentListing).contains(expression)) {
                logger.debug("got " + currentListing);
                return currentListing;
            }
        }

        logger.debug("No listings with such expression " + expression);
        return "";
    }

    public  int getQuantityBedrooms() {
        return getNumber(getListing("Bedrooms:"));
    }

    public  int getQuantityBathrooms() {
        return getNumber(getListing("Bathrooms:"));
    }

    public  int getQuantityBeds() {
        return getNumber(getListing("Beds:"));
    }

    public  int getQuantityAccommodates() {
        return getNumber(getListing("Accommodates:"));
    }

    public  PropertyType getPropertyType() {
        return PropertyType.getPropertyType(getListing("Property type:").replaceAll("Property type: ", ""));
    }

    public  int getId() {
        String id = null;
        try {
            id = getFeature(ID_EXPRESSION, "value");
        } catch (TooManyAreasException | NoAreasException e) {
            logger.debug(e.getMessage() + " id");
        }

        logger.debug("got id: " + id);

        return getNumber(id);
    }

    public  int getCountReviews() {
        String result = "";

        try {
            result = getFeature(COUNT_REVIEW_EXPRESSION);
        } catch (TooManyAreasException e) {
            logger.debug(e.getMessage() + " count review");
        } catch (NoAreasException e) {
            // ignore because exist flats without reviews
        }

        logger.debug("get countreviews: " + result);

        return getNumber(result);
    }

    public  int getRating() {
        final int countStars = countFeatures(COUNT_RATING_EXPRESSION + "/i[@class='icon icon-pink icon-beach icon-star']");
        final int countHalfStars = countFeatures(COUNT_RATING_EXPRESSION + "/i[@class='icon icon-pink icon-beach icon-star-half']");
        logger.debug("got rating: " + 2 * countStars + countHalfStars );
        return 2 * countStars + countHalfStars;
    }

    private  void setAmenities(final List<String> amenities, final Flat.FlatBuilder builder) {
        final HashSet<String> amenitiesSet = new HashSet<>();
        for (String currentAmenities : amenities)
            amenitiesSet.add(currentAmenities.toLowerCase());

        final boolean active = true;
        if (amenitiesSet.contains("essentials"))
            builder.setTv(active);
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
        if (amenitiesSet.contains("carbon monoxide detector"))
            builder.setCarbonMonoxideDetector(active);
        if (amenitiesSet.contains("first aid kit"))
            builder.setFirstAidKit(active);
        if (amenitiesSet.contains("safety card"))
            builder.setSafetyCard(active);
        if (amenitiesSet.contains("fire extinguisher"))
            builder.setFireExtinguiser(active);

    }

    private  void setLocation(final Flat.FlatBuilder builder) {
        final String location = getLocation();

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

    public List<Integer> getUserIdsFromComments() {

        final List<Integer> result = new ArrayList<>();

        List<String> userIds;
        do {
            userIds = getFeatures(USER_IDS_EXPRESSION, "href");
            for (String currentUserId : userIds)
                result.add(getNumber(currentUserId));
        } while (clickAndWait(LOAD_MORE_COMMENTS));

        logger.debug("got user's ids from comments: " + result.size() + (result.size() > 0 ? " [" + result.get(0) + ", ...]" : ""));

        return result;
    }

}