package json_loading;

/** Holds information about a location */
public class Location {

    private String latitude;
    private String longitude;
    private String city;
    private String country;

    public double getLatitude() {
        return Double.parseDouble(this.latitude);
    }

    public double getLongitude() {
        return Double.parseDouble(this.longitude);
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
