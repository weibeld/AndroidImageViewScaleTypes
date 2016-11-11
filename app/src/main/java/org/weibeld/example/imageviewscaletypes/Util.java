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
}
