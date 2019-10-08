package utils;

import com.google.gson.Gson;

/** Responsible for taking in an object and turning it into a JSON string */
public class Serializer {

    /** Makes a JSON string from the contents and structure of an object
     *
     * @param obj An Object to be serialized
     * @return A String that contains JSON with the contents of the object being serialized
     */
    public String serialize(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
}
