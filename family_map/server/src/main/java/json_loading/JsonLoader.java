package json_loading;

import utils.Deserializer;

/** Responsible for reading JSON files into objects and making those objects available */
public class JsonLoader {

    private Locations locations;
    private Names femaleNames;
    private Names maleNames;
    private Names surnames;

    /** Constructs a JsonLoader object, which includes reading JSON files into member variables */
    public JsonLoader() {
        Deserializer d = new Deserializer();
        String jsonPath = System.getProperty("user.home") +
                "/AndroidStudioProjects/family_map/server/json/";

        try {
            locations = (Locations)d.deserializeFromFile(jsonPath + "locations.json", Locations.class);
            femaleNames = (Names)d.deserializeFromFile(jsonPath + "fnames.json", Names.class);
            maleNames = (Names)d.deserializeFromFile(jsonPath + "mnames.json", Names.class);
            surnames = (Names)d.deserializeFromFile(jsonPath + "snames.json", Names.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Locations getLocations() {
        return locations;
    }

    public Names getFemaleNames() {
        return femaleNames;
    }

    public Names getMaleNames() {
        return maleNames;
    }

    public Names getSurnames() {
        return surnames;
    }
}
