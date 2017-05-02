package utils;

import models.Post;

/**
 * Created by yuva on 2/5/17.
 */
public class Location {

    public static Integer GEO_DISTANCE_MILES_CONST = 3959 ;
    public static Integer GEO_DISTANCE_KMS_CONST = 6371 ;

    private Double lat ;
    private Double lng ;

    private Location(Double lat, Double lng) {
        this.lat = lat ;
        this.lng = lng ;
    }

    public static Location get(Double lat, Double lng)  {
        return new Location(lat, lng) ;
    }

    public Double getDistanceTo(Location location) {
        return getDistanceBetweenLocations(location, this) ;
    }

    public boolean isInSameVicinityAs(Location location) {
        return getDistanceTo(location) <= Config.ALBUM_DISTANCE_SPAN_THRESHOLD ;
    }

    /**
     * Gives distance between two geo points in KMs
     * @param loc1
     * @param loc2
     * @return
     */
    public static Double getDistanceBetweenLocations(Location loc2, Location loc1) {
        double latDistance = Math.toRadians(loc2.lat - loc1.lat);
        double lngDistance = Math.toRadians(loc2.lng - loc1.lng);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(loc1.lat)) * Math.cos(Math.toRadians(loc2.lat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = GEO_DISTANCE_KMS_CONST * c ;

//        double height = el1 - el2;

        //      distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return distance ;
    }


}
