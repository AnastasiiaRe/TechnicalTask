package framework.platform.utilities;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

/**
 * Contains methods for formatting and generation of text, numbers etc.
 */
public class StringUtils {

    /**
     * Return random string of numbers and letters.
     *
     * @param length - length of the string to generate
     */
    public static String generateRandomStrAlphabetic(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    /**
     * Return random string of numbers.
     *
     * @param lenght - length of the string to generate
     */
    public static String generateRandomNumberic(int lenght) {
        return RandomStringUtils.randomNumeric(lenght);
    }

    /**
     * Return a random digit within given range.
     *
     * @param maxNumber - maximum possible digit from the 0
     */
    public static int generateRandomInt(int maxNumber) {
        return new Random().nextInt(maxNumber);
    }

    /** Return a random email address. */
    public static String generateRandomEmail() {
        return (StringUtils.generateRandomStrAlphabetic(10) + "@mailinator.com").toLowerCase();
    }

}
