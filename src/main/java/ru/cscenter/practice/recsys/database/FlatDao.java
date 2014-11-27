package ru.cscenter.practice.recsys.database;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.cscenter.practice.recsys.Flat;
import ru.cscenter.practice.recsys.enums.PropertyType;

import java.lang.reflect.Field;
import java.sql.Types;

public class FlatDao implements IAirBnbObjectDao<Flat> {

    private final JdbcTemplate dbAccess;

    public FlatDao(JdbcTemplate template) {
        dbAccess = template;
    }

    public int put(Flat flat) {
        final StringBuilder queryBuilder = new StringBuilder("insert into flats value(");

        for (int i = 0; i < flat.getClass().getDeclaredFields().length; ++i)
            queryBuilder.append("?,");
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.append(")");

        Field[] fieldsOfFlat = flat.getClass().getDeclaredFields();

        Object[] params = new Object[fieldsOfFlat.length];
        int[] types = new int[fieldsOfFlat.length];

        for (int i = 0; i < fieldsOfFlat.length; ++i) {
            try {
                fieldsOfFlat[i].setAccessible(true);

                Object valueOfField = fieldsOfFlat[i].get(flat);
                if (valueOfField == null)
                    valueOfField = "null";

                switch (valueOfField.toString()) {
                    case "true":
                        params[i] = 1;
                        types[i] = Types.BIT;
                        break;
                    case "false":
                        params[i] = 0;
                        types[i] = Types.BIT;
                        break;
                    default:
                        params[i] = valueOfField.toString();
                        types[i] = Types.VARCHAR;
                        break;
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();

            }
        }

        return dbAccess.update(queryBuilder.toString(), params, types);
    }

    public boolean contains(int id) {
        final String query = "select count(*) from flats where id = ?";
        Object[] params = new Object[]{id};
        return  dbAccess.queryForObject(query, params, Integer.class) == 1;
    }

    public Flat get(int id) {
        final String query = "select * from flats where id = ?";
        Object[] params = new Object[]{id};
        int[] types = new int[]{Types.INTEGER};
        SqlRowSet flatSet = dbAccess.queryForRowSet(query, params, types);

        if(!flatSet.next()) return null;

        final Flat.FlatBuilder flatBuilder = new Flat.FlatBuilder();
        flatBuilder.setCountry(flatSet.getString("country"));
        flatBuilder.setDistrict(flatSet.getString("district"));
        flatBuilder.setCity(flatSet.getString("city"));

        flatBuilder.setAccomodates(flatSet.getInt("accomodates"));
        flatBuilder.setBathrooms(flatSet.getInt("bathrooms"));
        flatBuilder.setBedrooms(flatSet.getInt("bedrooms"));
        flatBuilder.setBeds(flatSet.getInt("beds"));
        flatBuilder.setTypeProperty(PropertyType.getPropertyType(flatSet.getString("typeProperty")));

        flatBuilder.setDescription(flatSet.getString("description"));
        flatBuilder.setPricePerNight(flatSet.getInt("pricePerNight"));
        flatBuilder.setCurrency(flatSet.getString("currency"));
        flatBuilder.setTitle(flatSet.getString("title"));
        flatBuilder.setCountReview(flatSet.getInt("countReview"));
        flatBuilder.setRatingValue(flatSet.getInt("ratingValue"));
        flatBuilder.setId(flatSet.getInt("id"));

        flatBuilder.setEssentials(flatSet.getBoolean("essentials"));
        flatBuilder.setTv(flatSet.getBoolean("tv"));
        flatBuilder.setCableTv(flatSet.getBoolean("cableTv"));
        flatBuilder.setAirConditioning(flatSet.getBoolean("airConditioning"));
        flatBuilder.setHeating(flatSet.getBoolean("heating"));
        flatBuilder.setKitchen(flatSet.getBoolean("kitchen"));
        flatBuilder.setInternet(flatSet.getBoolean("internet"));
        flatBuilder.setWirelessInternet(flatSet.getBoolean("wirelessInternet"));
        flatBuilder.setHotTub(flatSet.getBoolean("hotTub"));
        flatBuilder.setWasher(flatSet.getBoolean("washer"));
        flatBuilder.setPool(flatSet.getBoolean("pool"));
        flatBuilder.setDryer(flatSet.getBoolean("dryer"));
        flatBuilder.setFreeParkingOnPremises(flatSet.getBoolean("freeParkingOnPremises"));
        flatBuilder.setGym(flatSet.getBoolean("gym"));
        flatBuilder.setElevator(flatSet.getBoolean("elevator"));
        flatBuilder.setInDoorFireplace(flatSet.getBoolean("inDoorFireplace"));
        flatBuilder.setBuzzerIntercom(flatSet.getBoolean("buzzerIntercom"));
        flatBuilder.setDoorman(flatSet.getBoolean("doorman"));
        flatBuilder.setShampoo(flatSet.getBoolean("shampoo"));
        flatBuilder.setFamilyFriendly(flatSet.getBoolean("familyFriendly"));
        flatBuilder.setSmokingAllowed(flatSet.getBoolean("smokingAllowed"));
        flatBuilder.setSuitableForEvents(flatSet.getBoolean("suitableForEvents"));
        flatBuilder.setPetsAllowed(flatSet.getBoolean("petsAllowed"));
        flatBuilder.setPetsLivedOnThisProperty(flatSet.getBoolean(""));
        flatBuilder.setWheelchairAccessible(flatSet.getBoolean("wheelchairAccessible"));
        flatBuilder.setSmokeDetector(flatSet.getBoolean("smokeDetector"));
        flatBuilder.setCarbonMonoxideDetector(flatSet.getBoolean("carbonMonoxideDetector"));
        flatBuilder.setFirstAidKit(flatSet.getBoolean("firstAidKit"));
        flatBuilder.setSafetyCard(flatSet.getBoolean("safetyCard"));
        flatBuilder.setFireExtinguiser(flatSet.getBoolean("fireExtinguiser"));

        return flatBuilder.build();
    }
}
