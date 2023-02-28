package net.jadedmc.jadedchat.utils;

import java.util.Arrays;
import java.util.List;

/**
 * A collection of String-related utilities.
 */
public class StringUtils {

    /**
     * Join multiple strings together into one string.
     * @param args List of strings to join together.
     * @param separator What should be between each string.
     * @return Combined string.
     */
    public static String join(List<String> args, String separator) {
        StringBuilder temp = new StringBuilder();

        for(String str : args) {
            if(!temp.toString().equals("")) {
                temp.append(separator);
            }

            temp.append(str);
        }

        return temp.toString();
    }

    /**
     * Join multiple strings together into one string.
     * @param args Array of strings to join together.
     * @param separator What should be between each string.
     * @return Combined string.
     */
    public static String join(String[] args, String separator) {
        return join(Arrays.asList(args), separator);
    }
}