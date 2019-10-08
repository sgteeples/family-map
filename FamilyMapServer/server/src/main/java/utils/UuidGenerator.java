package utils;

import java.util.UUID;

/** Responsible for generating random UUID strings */
public class UuidGenerator {

    /** Generates random UUID strings
     *
     * @return A random UUID string
     */
    public String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
