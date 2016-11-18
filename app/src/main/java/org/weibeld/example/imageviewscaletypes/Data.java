package org.weibeld.example.imageviewscaletypes;

import android.widget.ImageView;

import java.util.Arrays;

/**
 * Created by dw on 24/10/16.
 */

class Data {

    private Data() {}

    static final String TRUE = "true";
    static final String FALSE = "false";
    static final String[] ARR_BOOL = new String[] { TRUE, FALSE };

    static final String WRAP_CONTENT = "wrap_content";
    static final String MATCH_PARENT = "match_parent";
    static final String[] ARR_DIMEN_KEYWORDS = new String[] { MATCH_PARENT, WRAP_CONTENT };

    static final String UNIT_DP = "dp";
    static final String UNIT_SP = "sp";
    static final String UNIT_PX = "px";
    static final String UNIT_IN = "in";
    static final String UNIT_MM = "mm";
    private static final String[] ARR_DIMEN_UNITS = new String[] {
            UNIT_DP, UNIT_SP, UNIT_PX, UNIT_IN, UNIT_MM
    };
    static String[] getArrDimenUnits() {
        Arrays.sort(ARR_DIMEN_UNITS);
        return ARR_DIMEN_UNITS;
    }

    // Color strings accepted by Color.parseColor (except "grey" variants)
    static final String COLOR_RED = "Red";
    static final String COLOR_BLUE = "Blue";
    static final String COLOR_GREEN = "Green";
    static final String COLOR_BLACK = "Black";
    static final String COLOR_WHITE = "White";
    static final String COLOR_GRAY = "Gray";
    static final String COLOR_CYAN = "Cyan";
    static final String COLOR_MAGENTA = "Magenta";
    static final String COLOR_YELLOW = "Yellow";
    static final String COLOR_LIGHTGRAY = "Lightgray";
    static final String COLOR_DARKGRAY = "Darkgray";
    static final String COLOR_AQUA = "Aqua";
    static final String COLOR_FUCHSIA = "Fuchsia";
    static final String COLOR_LIME = "Lime";
    static final String COLOR_MAROON = "Maroon";
    static final String COLOR_NAVY = "Navy";
    static final String COLOR_OLIVE = "Olive";
    static final String COLOR_PURPLE = "Purple";
    static final String COLOR_SILVER = "Silver";
    static final String COLOR_TEAL = "Teal";
    private static final String[] ARR_COLORS = new String[] {
            COLOR_RED, COLOR_BLUE, COLOR_GREEN, COLOR_BLACK, COLOR_WHITE, COLOR_GRAY, COLOR_CYAN,
            COLOR_MAGENTA, COLOR_YELLOW, COLOR_LIGHTGRAY, COLOR_DARKGRAY, COLOR_AQUA, COLOR_FUCHSIA,
            COLOR_LIME, COLOR_MAROON, COLOR_NAVY, COLOR_OLIVE, COLOR_PURPLE, COLOR_SILVER, COLOR_TEAL
    };
    static String[] getArrColors() {
        Arrays.sort(ARR_COLORS);
        return ARR_COLORS;
    }

    private static final String REGEX_NUM = "^((0\\.\\d+)|([1-9]\\d*\\.\\d+)|(0\\.)|([1-9]\\d*\\.)|(\\.\\d+)|(0)|([1-9]\\d*))";
    private static final String REGEX_RGB = "(#[a-fA-F0-9]{8})|(#[a-fA-F0-9]{6})";
    private static final String REGEX_EMPTY = "^$";
    static String getRegexNum() {
        return REGEX_NUM;
    }
    static String getRegexRgb() {
        return REGEX_RGB;
    }
    static String getRegexEmpty() {
        return REGEX_EMPTY;
    }
    static String getRegexDimenKeywords() {
        return getArrEltChain(ARR_DIMEN_KEYWORDS, "|");
    }
    static String getRegexDimen() {
        return REGEX_NUM + "(" + getArrEltChain(getArrDimenUnits(), "|") + ")";
    }
    static String getRegexColors() {
        return "(?i)" + getArrEltChain(getArrColors(), "|");  // (?i) = case-insensitive matching
    }


    static String getWarnMsgLineDimen() {
        return "\n" + bullet + "<N> " + getArrEltChain(getArrDimenUnits(), " | ");
    }
    static String getWarnMsgLineDimenKeywords() {
        return "\n" + bullet + getArrEltChain(ARR_DIMEN_KEYWORDS, " | ");
    }
    static String getWarnMsgLineEmpty() {
        return "\n" + bullet + "<empty>";
    }
    static String getWarnMsgLineColors() {
        return "\n" + bullet + getArrEltChain(getArrColors(), " | ");
    }
    static String getWarnMsgLineRgb() {
        return "\n" + bullet + "#rrggbb | #aarrggbb";
    }
    private static String bullet = "   \u2022 ";


    private static String getArrEltChain(String[] a, String sep) {
        String s = "";
        for (int i = 0; i < a.length; i++) {
            s += a[i];
            if (i < a.length - 1) s += sep;
        }
        return s;
    }

    // All the ImageView scale types in the order in which we want to display them
    static final ImageView.ScaleType[] SCALE_TYPES = new ImageView.ScaleType[] {
            ImageView.ScaleType.CENTER,
            ImageView.ScaleType.CENTER_CROP,
            ImageView.ScaleType.CENTER_INSIDE,
            ImageView.ScaleType.FIT_CENTER,
            ImageView.ScaleType.FIT_START,
            ImageView.ScaleType.FIT_END,
            ImageView.ScaleType.FIT_XY,
            ImageView.ScaleType.MATRIX,
    };

    // Page position argument passed to the PageFragment
    static final String ARG_POSITION = "position";

    // Code for the choose image intent
    static final int CHOOSE_IMAGE_REQUEST_CODE = 1;
}
