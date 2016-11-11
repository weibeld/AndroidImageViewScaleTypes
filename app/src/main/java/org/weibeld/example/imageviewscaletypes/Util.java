package org.weibeld.example.imageviewscaletypes;

import android.util.TypedValue;

/**
 * Created by dw on 10/11/16.
 */

public class Util {

    private Util() {}


    // Parse a string of the form <value><unit> and extract its value and unit parts, respectively.
    // Example: "16dp": value=16, unit=dp. The value must be float, and the unit must be one of
    // dp, sp, px, in, mm. Throws a NumberFormatException if the format of the string is not valid.
    public static Dimension parseDimension(String str) {
        if (str.length() < 3) throw new NumberFormatException();
        float value = parseDimensionValue(str.substring(0, str.length()-2));
        int unit = parseDimensionUnit(str.substring(str.length()-2));
        return new Dimension(value, unit);
    }

    // Parse a string for one of the following dimension units: px, dp, sp, in, mm.
    // Returns the TypedValue.COMPLEX_UNIT_XX value corresponding to the parsed unit, or throws a
    // NumberFormatException if the string does not correspond to one of the accepted units.
    private static int parseDimensionUnit(String str) {
        switch (str) {
            case "dp":
                return TypedValue.COMPLEX_UNIT_DIP;
            case "px":
                return TypedValue.COMPLEX_UNIT_PX;
            case "sp":
                return TypedValue.COMPLEX_UNIT_SP;
            case "in":
                return TypedValue.COMPLEX_UNIT_IN;
            case "mm":
                return TypedValue.COMPLEX_UNIT_MM;
            default:
                throw new NumberFormatException();
        }
    }

    // Parse a string for a float representing a valid Android dimension value. Examples of valid
    // dimension values: "1", "1.0", "0.1", "1.", ".1".
    // Returns the parsed float or throws NumberFormatException if the string is not a valid value.
    // TODO: check if we shouldn't use just Float.parse
    public static float parseDimensionValue(String str) {
        String regex = "^(\\d+)|(\\d+\\.)|(\\.\\d+)|(\\d+\\.\\d+)$";
        if (!str.matches(regex)) throw new NumberFormatException();
        return Float.parseFloat(str);
    }

    // Class representing an Android dimension, consisting of a value (float), anda unit (one of
    // the TypedValue.COMPLEX_UNIT_XX ints).
    static class Dimension {
        public float value;
        public int unit;

        public Dimension(float value, int unit) {
            this.value = value;
            this.unit = unit;
        }
    }

    /**
     * Get the binary representation of an integer as a string.
     *
     * @param number Any positive or negative integer.
     * @return Binary representation of the passed integer as a string.
     */
    public static String intToBinaryString(int number) {
        // Idea: iterate through all the 32 positions of an integer (most signif. to least signif.)
        // and build the binary representation bit by bit
        String str = "";
        for (int i = 31; i >= 0; i--) {

            // Generate numbers 1000.., 0100.., 0010.. (note that 1000... is -(2^31) and not 2^31)
            int mask = 1 << i;

            // Check if the number to test has a 1 at the current position. Examples:
            // Mask:    0010            Mask:    0010
            // Number:  0110            Number:  1100
            //        & ----                   & ----
            // Result:  0010 != 0       Result:  0000 == 0
            if ((mask & number) != 0)
                str = str + "1";
            else
                str = str + "0";

            // Add space every 4 positions
            if ((i % 4) == 0 && i != 0) str = str + " ";
        }
        return str;

        // General notes:
        // - An int has 32 bits, and thus 4,294,967,296 (2^32) different values
        // - 2,147,483,648 (2^31) of these values are negative: [-2,147,483,648 .. -1]
        //     - Integer.MIN_VALUE == -2,147,483,648 -(2^31)
        // - 2,147,483,648 (2^31) of these values are positive: [0 ..               2,147,483,647]
        //     - Integer.MAX_VALUE == 2,147,483,647 (2^31)-1
        // The most significant bit (position 31) indicates the sign: 0 -> positive, 1 -> negative
    }

    /**
     * Check if a specific flag is set in a mode value.
     *
     * @param mode The mode consisting of all the set or unset flags.
     * @param flag The flag to be checked.
     * @return {@code true} if {@code flag} is set in {@code mode}, and {@code false} otherwise.
     */
    public static boolean isFlagSet(int mode, int flag) {
        // Examples:
        // Flag is set:             Flag is not set:
        // Mode:   1011             Mode:   1011
        // Flag:   0010             Flag:   0100
        //       & ----                   & ----
        // Result: 0010 != 0        Result: 0000 == 0
        return (mode & flag) != 0;
    }

    public static int setFlag(int mode, int flag) {
        return mode | flag;
    }

    public static int toggleFlag(int mode, int flag) {
        return mode ^ flag;
    }

    public static int unsetFlag(int mode, int flag) {
        if (isFlagSet(mode, flag))
            mode = toggleFlag(mode, flag);
        return mode;
    }
}
