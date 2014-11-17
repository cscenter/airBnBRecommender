package ru.cscenter.practice.recsys.enums;
//TODO: rename package Enums to enums

public enum PropertyType {
    APARTMENT("Apartment"),
    HOUSE("House"),
    BEDNBREAKFAST("Bed & Breakfast"),
    LOFT("Loft"),
    CABIN("Cabin"),
    VILLA("Villa"),
    CASTLE("Castle"),
    DORM("Dorm"),
    TREEHOUSE("Treehouse"),
    BOAT("Boat"),
    PLANE("Plane"),
    CAMPER("Camper"),
    IGLOO("Igloo"),
    LIGHTHOUSE("Lighthouse"),
    YURT("Yurt"),
    TIPI("Tipi"),
    CAVE("Cave"),
    ISLAND("Island"),
    CHALET("Chalet"),
    EARTHHOUSE("Earthhouse"),
    HUT("Hut"),
    TRAIN("Train"),
    TENT("Tent"),
    OTHER("Other");

    private final String type;

    PropertyType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static PropertyType getPropertyType(String desiredType) {
        for (PropertyType type : PropertyType.values())
            if (type.getType().equals(desiredType))
                return type;
        return null;
    }

    @Override
    public String toString() {
        return getType();
    }
}
