package json_loading;

import java.util.Random;

/** Holds an array of names and allows for random drawing from the array as well */
public class Names {

    private String[] data;

    public String[] getData() {
        return data;
    }

    /** Returns a random name from this object
     *
     * @return A random name from data, the list of names this object contains
     */
    public String getRandomName() {
        Random generator = new Random();
        int randomIndex = generator.nextInt(data.length);
        return data[randomIndex];
    }
}
