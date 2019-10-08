package json_loading;

import java.util.Random;

/** Holds an array of locations and allows for random drawing from the array as well */
public class Locations {

    private Location[] data;

    public Location[] getData() {
        return data;
    }

    /** Returns a random location from this object
     *
     * @return A random location from data, the list of locations this object contains
     */
    public Location getRandomLocation() {
        Random generator = new Random();
        int randomIndex = generator.nextInt(data.length);
        return data[randomIndex];
    }
}
