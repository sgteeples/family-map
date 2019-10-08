package utils;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/** Responsible for taking in a JSON string and turning it into a generic object */
public class Deserializer {

    /** Makes a generic Object from the data contained in a JSON string
     *
     * @param jsonStr A JSON string to be deserialized
     * @param klass The class whose structure the JSON string will be parsed to match
     * @return A generic Object that has the structure of klass and the contents of jsonStr
     */
    public Object deserialize(String jsonStr, Class klass) {
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, klass);
    }


    /** Makes a generic Object from the data contained in a JSON string in a file
     *
     * @param fname Name of the file containing JSON string
     * @param klass The class whose structure the JSON string will be parsed to match
     * @return A generic Object that has the structure of klass and the contents of fname
     * @throws FileNotFoundException Thrown if the file associated with fname can't be found
     * @throws IOException Thrown if there's a problem reading from the file
     */
    public Object deserializeFromFile(String fname, Class klass) throws FileNotFoundException, IOException {
        Gson gson = new Gson();
        Reader reader = new FileReader(fname);
        Object deserializedObj = gson.fromJson(reader, klass);
        reader.close();
        return deserializedObj;
    }
}