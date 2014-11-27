package ru.cscenter.practice.recsys;

import ru.cscenter.practice.recsys.enums.PropertyType;

import java.io.Serializable;

/* this class will make you cry */

public class Flat implements Serializable {
    private final int id;

    /* Location */
    private final String city;
    private final String district;
    private final String country;


    /* Pricing */
    private final int pricePerNight;
    private final String currency;

    /* Overview */
    private final String title;
    private final String description;

    /* Amenities */

    /* Most Common */
    private final boolean essentials;
    private final boolean tv;
    private final boolean cableTv;
    private final boolean airConditioning;
    private final boolean heating;
    private final boolean kitchen;
    private final boolean internet;
    private final boolean wirelessInternet;

    /* Extras */
    private final boolean hotTub;
    private final boolean washer;
    private final boolean pool;
    private final boolean dryer;
    private final boolean freeParkingOnPremises;
    private final boolean gym;
    private final boolean elevator;
    private final boolean inDoorFireplace;
    private final boolean buzzerIntercom;
    private final boolean doorman;
    private final boolean shampoo;

    /* Special Features */
    private final boolean familyFriendly;
    private final boolean smokingAllowed;
    private final boolean suitableForEvents;
    private final boolean petsAllowed;
    private final boolean petsLivedOnThisProperty;
    private final boolean wheelchairAccessible;

    /* Home Safety */
    private final boolean smokeDetector;
    private final boolean carbonMonoxideDetector;
    private final boolean firstAidKit;
    private final boolean safetyCard;
    private final boolean fireExtinguiser;

    /* Listing */
    private final int bedrooms;
    private final int beds;
    private final int bathrooms;

    private final PropertyType typeProperty;
    private final int accomodates;

    private final int countReview;
    private final int ratingValue;

    private Flat(FlatBuilder builder) {
        this.id = builder.id;
        this.city = builder.city;
        this.district = builder.district;
        this.country = builder.country;
        this.pricePerNight = builder.pricePerNight;
        this.currency = builder.currency;
        this.title = builder.title;
        this.description = builder.description;
        this.essentials = builder.essentials;
        this.tv = builder.tv;
        this.cableTv = builder.cableTv;
        this.airConditioning = builder.airConditioning;
        this.heating = builder.heating;
        this.kitchen = builder.kitchen;
        this.internet = builder.internet;
        this.wirelessInternet = builder.wirelessInternet;
        this.hotTub = builder.hotTub;
        this.washer = builder.washer;
        this.pool = builder.pool;
        this.dryer = builder.dryer;
        this.freeParkingOnPremises = builder.freeParkingOnPremises;
        this.gym = builder.gym;
        this.elevator = builder.elevator;
        this.inDoorFireplace = builder.inDoorFireplace;
        this.buzzerIntercom = builder.buzzerIntercom;
        this.doorman = builder.doorman;
        this.shampoo = builder.shampoo;
        this.familyFriendly = builder.familyFriendly;
        this.smokingAllowed = builder.smokingAllowed;
        this.suitableForEvents = builder.suitableForEvents;
        this.petsAllowed = builder.petsAllowed;
        this.petsLivedOnThisProperty = builder.petsLivedOnThisProperty;
        this.wheelchairAccessible = builder.wheelchairAccessible;
        this.smokeDetector = builder.smokeDetector;
        this.carbonMonoxideDetector = builder.carbonMonoxideDetector;
        this.firstAidKit = builder.firstAidKit;
        this.safetyCard = builder.safetyCard;
        this.fireExtinguiser = builder.fireExtinguiser;
        this.bedrooms = builder.bedrooms;
        this.beds = builder.beds;
        this.bathrooms = builder.bathrooms;
        this.typeProperty = builder.typeProperty;
        this.accomodates = builder.accomodates;
        this.countReview = builder.countReview;
        this.ratingValue = builder.ratingValue;
    }

    public int getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getCountry() {
        return country;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTitle() {
        return title;
    }

    public boolean isEssentials() {
        return essentials;
    }

    public String getDescription() {
        return description;
    }

    public boolean isTv() {
        return tv;
    }

    public boolean isCableTv() {
        return cableTv;
    }

    public boolean isAirConditioning() {
        return airConditioning;
    }

    public boolean isHeating() {
        return heating;
    }

    public boolean isKitchen() {
        return kitchen;
    }

    public boolean isInternet() {
        return internet;
    }

    public boolean isWirelessInternet() {
        return wirelessInternet;
    }

    public boolean isHotTub() {
        return hotTub;
    }

    public boolean isWasher() {
        return washer;
    }

    public boolean isPool() {
        return pool;
    }

    public boolean isDryer() {
        return dryer;
    }

    public boolean isFreeParkingOnPremises() {
        return freeParkingOnPremises;
    }

    public boolean isGym() {
        return gym;
    }

    public boolean isElevator() {
        return elevator;
    }

    public boolean isInDoorFireplace() {
        return inDoorFireplace;
    }

    public boolean isBuzzerIntercom() {
        return buzzerIntercom;
    }

    public boolean isDoorman() {
        return doorman;
    }

    public boolean isShampoo() {
        return shampoo;
    }

    public boolean isFamilyFriendly() {
        return familyFriendly;
    }

    public boolean isSmokingAllowed() {
        return smokingAllowed;
    }

    public boolean isSuitableForEvents() {
        return suitableForEvents;
    }

    public boolean isPetsAllowed() {
        return petsAllowed;
    }

    public boolean isPetsLivedOnThisProperty() {
        return petsLivedOnThisProperty;
    }

    public boolean isWheelchairAccessible() {
        return wheelchairAccessible;
    }

    public boolean isSmokeDetector() {
        return smokeDetector;
    }

    public boolean isCarbonMonoxideDetector() {
        return carbonMonoxideDetector;
    }

    public boolean isFirstAidKit() {
        return firstAidKit;
    }

    public boolean isSafetyCard() {
        return safetyCard;
    }

    public boolean isFireExtinguiser() {
        return fireExtinguiser;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public int getBeds() {
        return beds;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public PropertyType getTypeProperty() {
        return typeProperty;
    }

    public int getAccomodates() {
        return accomodates;
    }

    public int getCountReview() {
        return countReview;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    @Override
    public String toString() {
        return "Flat{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", country='" + country + '\'' +
                ", pricePerNight=" + pricePerNight +
                ", currency=" + currency +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", essentials=" + essentials +
                ", tv=" + tv +
                ", cableTv=" + cableTv +
                ", airConditioning=" + airConditioning +
                ", heating=" + heating +
                ", kitchen=" + kitchen +
                ", internet=" + internet +
                ", wirelessInternet=" + wirelessInternet +
                ", hotTub=" + hotTub +
                ", washer=" + washer +
                ", pool=" + pool +
                ", dryer=" + dryer +
                ", freeParkingOnPremises=" + freeParkingOnPremises +
                ", gym=" + gym +
                ", elevator=" + elevator +
                ", inDoorFireplace=" + inDoorFireplace +
                ", buzzerIntercom=" + buzzerIntercom +
                ", doorman=" + doorman +
                ", shampoo=" + shampoo +
                ", familyFriendly=" + familyFriendly +
                ", smokingAllowed=" + smokingAllowed +
                ", suitableForEvents=" + suitableForEvents +
                ", petsAllowed=" + petsAllowed +
                ", petsLivedOnThisProperty=" + petsLivedOnThisProperty +
                ", wheelchairAccessible=" + wheelchairAccessible +
                ", smokeDetector=" + smokeDetector +
                ", carbonMonoxideDetector=" + carbonMonoxideDetector +
                ", firstAidKit=" + firstAidKit +
                ", safetyCard=" + safetyCard +
                ", fireExtinguiser=" + fireExtinguiser +
                ", bedrooms=" + bedrooms +
                ", beds=" + beds +
                ", bathrooms=" + bathrooms +
                ", typeProperty=" + typeProperty +
                ", accomodates=" + accomodates +
                ", countReview=" + countReview +
                ", ratingValue=" + ratingValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flat flat = (Flat) o;

        if (accomodates != flat.accomodates) return false;
        if (airConditioning != flat.airConditioning) return false;
        if (bathrooms != flat.bathrooms) return false;
        if (bedrooms != flat.bedrooms) return false;
        if (beds != flat.beds) return false;
        if (buzzerIntercom != flat.buzzerIntercom) return false;
        if (cableTv != flat.cableTv) return false;
        if (carbonMonoxideDetector != flat.carbonMonoxideDetector) return false;
        if (doorman != flat.doorman) return false;
        if (dryer != flat.dryer) return false;
        if (elevator != flat.elevator) return false;
        if (essentials != flat.essentials) return false;
        if (familyFriendly != flat.familyFriendly) return false;
        if (fireExtinguiser != flat.fireExtinguiser) return false;
        if (firstAidKit != flat.firstAidKit) return false;
        if (freeParkingOnPremises != flat.freeParkingOnPremises) return false;
        if (gym != flat.gym) return false;
        if (heating != flat.heating) return false;
        if (hotTub != flat.hotTub) return false;
        if (id != flat.id) return false;
        if (inDoorFireplace != flat.inDoorFireplace) return false;
        if (internet != flat.internet) return false;
        if (kitchen != flat.kitchen) return false;
        if (petsAllowed != flat.petsAllowed) return false;
        if (petsLivedOnThisProperty != flat.petsLivedOnThisProperty) return false;
        if (pool != flat.pool) return false;
        if (pricePerNight != flat.pricePerNight) return false;
        if (safetyCard != flat.safetyCard) return false;
        if (shampoo != flat.shampoo) return false;
        if (smokeDetector != flat.smokeDetector) return false;
        if (smokingAllowed != flat.smokingAllowed) return false;
        if (suitableForEvents != flat.suitableForEvents) return false;
        if (tv != flat.tv) return false;
        if (washer != flat.washer) return false;
        if (wheelchairAccessible != flat.wheelchairAccessible) return false;
        if (wirelessInternet != flat.wirelessInternet) return false;
        if (city != null ? !city.equals(flat.city) : flat.city != null) return false;
        if (country != null ? !country.equals(flat.country) : flat.country != null) return false;
        if (currency != null ? !currency.equals(flat.currency) : flat.currency != null) return false;
        if (description != null ? !description.equals(flat.description) : flat.description != null) return false;
        if (district != null ? !district.equals(flat.district) : flat.district != null) return false;
        if (title != null ? !title.equals(flat.title) : flat.title != null) return false;
        if (typeProperty != flat.typeProperty) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (district != null ? district.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + pricePerNight;
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (essentials ? 1 : 0);
        result = 31 * result + (tv ? 1 : 0);
        result = 31 * result + (cableTv ? 1 : 0);
        result = 31 * result + (airConditioning ? 1 : 0);
        result = 31 * result + (heating ? 1 : 0);
        result = 31 * result + (kitchen ? 1 : 0);
        result = 31 * result + (internet ? 1 : 0);
        result = 31 * result + (wirelessInternet ? 1 : 0);
        result = 31 * result + (hotTub ? 1 : 0);
        result = 31 * result + (washer ? 1 : 0);
        result = 31 * result + (pool ? 1 : 0);
        result = 31 * result + (dryer ? 1 : 0);
        result = 31 * result + (freeParkingOnPremises ? 1 : 0);
        result = 31 * result + (gym ? 1 : 0);
        result = 31 * result + (elevator ? 1 : 0);
        result = 31 * result + (inDoorFireplace ? 1 : 0);
        result = 31 * result + (buzzerIntercom ? 1 : 0);
        result = 31 * result + (doorman ? 1 : 0);
        result = 31 * result + (shampoo ? 1 : 0);
        result = 31 * result + (familyFriendly ? 1 : 0);
        result = 31 * result + (smokingAllowed ? 1 : 0);
        result = 31 * result + (suitableForEvents ? 1 : 0);
        result = 31 * result + (petsAllowed ? 1 : 0);
        result = 31 * result + (petsLivedOnThisProperty ? 1 : 0);
        result = 31 * result + (wheelchairAccessible ? 1 : 0);
        result = 31 * result + (smokeDetector ? 1 : 0);
        result = 31 * result + (carbonMonoxideDetector ? 1 : 0);
        result = 31 * result + (firstAidKit ? 1 : 0);
        result = 31 * result + (safetyCard ? 1 : 0);
        result = 31 * result + (fireExtinguiser ? 1 : 0);
        result = 31 * result + bedrooms;
        result = 31 * result + beds;
        result = 31 * result + bathrooms;
        result = 31 * result + (typeProperty != null ? typeProperty.hashCode() : 0);
        result = 31 * result + accomodates;
        return result;
    }

    public static class FlatBuilder {
        private int id;
        private String city;
        private String district;
        private String country;

        /* Pricing */
        private int pricePerNight;
        private String currency;

        /* Overview */
        private String title;
        private String description;

        /* Amenities */

        /* Most Common */
        private boolean essentials;
        private boolean tv;
        private boolean cableTv;
        private boolean airConditioning;
        private boolean heating;
        private boolean kitchen;
        private boolean internet;
        private boolean wirelessInternet;

        /* Extras */
        private boolean hotTub;
        private boolean washer;
        private boolean pool;
        private boolean dryer;
        private boolean freeParkingOnPremises;
        private boolean gym;
        private boolean elevator;
        private boolean inDoorFireplace;
        private boolean buzzerIntercom;
        private boolean doorman;
        private boolean shampoo;

        /* Special Features */
        private boolean familyFriendly;
        private boolean smokingAllowed;
        private boolean suitableForEvents;
        private boolean petsAllowed;
        private boolean petsLivedOnThisProperty;
        private boolean wheelchairAccessible;

        /* Home Safety */
        private boolean smokeDetector;
        private boolean carbonMonoxideDetector;
        private boolean firstAidKit;
        private boolean safetyCard;
        private boolean fireExtinguiser;

        /* Listing */
        private int bedrooms;
        private int beds;
        private int bathrooms;

        private PropertyType typeProperty;
        private int accomodates;

        private int countReview;
        private int ratingValue;

        public void setId(int id) {
            this.id = id;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setPricePerNight(int pricePerNight) {
            this.pricePerNight = pricePerNight;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setEssentials(boolean essentials) {
            this.essentials = essentials;
        }

        public void setTv(boolean tv) {
            this.tv = tv;
        }

        public void setCableTv(boolean cableTv) {
            this.cableTv = cableTv;
        }

        public void setAirConditioning(boolean airConditioning) {
            this.airConditioning = airConditioning;
        }

        public void setHeating(boolean heating) {
            this.heating = heating;
        }

        public void setKitchen(boolean kitchen) {
            this.kitchen = kitchen;
        }

        public void setInternet(boolean internet) {
            this.internet = internet;
        }

        public void setWirelessInternet(boolean wirelessInternet) {
            this.wirelessInternet = wirelessInternet;
        }

        public void setHotTub(boolean hotTub) {
            this.hotTub = hotTub;
        }

        public void setWasher(boolean washer) {
            this.washer = washer;
        }

        public void setPool(boolean pool) {
            this.pool = pool;
        }

        public void setDryer(boolean dryer) {
            this.dryer = dryer;
        }

        public void setFreeParkingOnPremises(boolean freeParkingOnPremises) {
            this.freeParkingOnPremises = freeParkingOnPremises;
        }

        public void setGym(boolean gym) {
            this.gym = gym;
        }

        public void setElevator(boolean elevator) {
            this.elevator = elevator;
        }

        public void setInDoorFireplace(boolean inDoorFireplace) {
            this.inDoorFireplace = inDoorFireplace;
        }

        public void setBuzzerIntercom(boolean buzzerIntercom) {
            this.buzzerIntercom = buzzerIntercom;
        }

        public void setDoorman(boolean doorman) {
            this.doorman = doorman;
        }

        public void setShampoo(boolean shampoo) {
            this.shampoo = shampoo;
        }

        public void setFamilyFriendly(boolean familyFriendly) {
            this.familyFriendly = familyFriendly;
        }

        public void setSmokingAllowed(boolean smokingAllowed) {
            this.smokingAllowed = smokingAllowed;
        }

        public void setSuitableForEvents(boolean suitableForEvents) {
            this.suitableForEvents = suitableForEvents;
        }

        public void setPetsAllowed(boolean petsAllowed) {
            this.petsAllowed = petsAllowed;
        }

        public void setPetsLivedOnThisProperty(boolean petsLivedOnThisProperty) {
            this.petsLivedOnThisProperty = petsLivedOnThisProperty;
        }

        public void setWheelchairAccessible(boolean wheelchairAccessible) {
            this.wheelchairAccessible = wheelchairAccessible;
        }

        public void setSmokeDetector(boolean smokeDetector) {
            this.smokeDetector = smokeDetector;
        }

        public void setCarbonMonoxideDetector(boolean carbonMonoxideDetector) {
            this.carbonMonoxideDetector = carbonMonoxideDetector;
        }

        public void setFirstAidKit(boolean firstAidKit) {
            this.firstAidKit = firstAidKit;
        }

        public void setSafetyCard(boolean safetyCard) {
            this.safetyCard = safetyCard;
        }

        public void setFireExtinguiser(boolean fireExtinguiser) {
            this.fireExtinguiser = fireExtinguiser;
        }

        public void setBedrooms(int bedrooms) {
            this.bedrooms = bedrooms;
        }

        public void setBeds(int beds) {
            this.beds = beds;
        }

        public void setBathrooms(int bathrooms) {
            this.bathrooms = bathrooms;
        }

        public void setTypeProperty(PropertyType typeProperty) {
            this.typeProperty = typeProperty;
        }

        public void setAccomodates(int accomodates) {
            this.accomodates = accomodates;
        }

        public void setCountReview(int countReview) {
            this.countReview = countReview;
        }

        public void setRatingValue(int ratingValue) {
            this.ratingValue = ratingValue;
        }

        public Flat build() {
            return new Flat(this);
        }
    }


}
