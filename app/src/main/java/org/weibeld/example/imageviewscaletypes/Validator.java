package org.weibeld.example.imageviewscaletypes;

/**
 * Created by dw on 13/11/16.
 */

public class Validator {

    private Validator() {}

    public static boolean isValidDimenEntry(String str) {
        switch (str) {
            case "":
                return true;
            default:
                return isValidDimenString(str);
        }
    }

    public static boolean isValidLayoutWidthHeightEntry(String str) {
        switch (str) {
            case Data.WRAP_CONTENT:
                return true;
            case Data.MATCH_PARENT:
                return true;
            default:
                return isValidDimenString(str);
        }
    }

    public static boolean isValidBooleanEntry(String str) {
        return str.equals(Data.TRUE) || str.equals(Data.FALSE);
    }

    public static boolean isValidColorEntry(String str) {
        // We use the colour format defined by Color.parseColor plus the empty string for not
        // specifying a background colour at all.
        String regex = "^$|(^(#([a-fA-F0-9]{6}|[a-fA-F0-9]{8}))|red|blue|green|black|white|gray|cyan|magenta|yellow|lightgray|darkgray|grey|lightgrey|darkgrey|aqua|fuchsia|lime|maroon|navy|olive|purple|silver|teal$)";
        return str.matches(regex);
    }

    // Test if a string has a valid "<value><unit>" format, such as "14.5dp"
    private static boolean isValidDimenString(String str) {
        String regex = "^\\-?((\\d+)|(\\d+\\.)|(\\.\\d+)|(\\d+\\.\\d+))(";
        for (int i = 0; i < Data.ARR_DIMEN_UNITS.length; i++) {
            regex += Data.ARR_DIMEN_UNITS[i];
            if (i < Data.ARR_DIMEN_UNITS.length - 1) regex += "|";
        }
        regex += ")$";
        return str.matches(regex);
    }

}
