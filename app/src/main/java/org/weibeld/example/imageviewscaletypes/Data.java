package org.weibeld.example.imageviewscaletypes;

import android.widget.ImageView;

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
    static final String[] ARR_DIMEN_KEYWORDS = new String[] { WRAP_CONTENT, MATCH_PARENT };

    static final String UNIT_DP = "dp";
    static final String UNIT_SP = "sp";
    static final String UNIT_PX = "px";
    static final String UNIT_IN = "in";
    static final String UNIT_MM = "mm";
    public static final String[] ARR_DIMEN_UNITS = new String[] {
            UNIT_DP, UNIT_SP, UNIT_PX, UNIT_IN, UNIT_MM
    };

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
    static final String[] ARR_COLORS = new String[] {
            COLOR_RED, COLOR_BLUE, COLOR_GREEN, COLOR_BLACK, COLOR_WHITE, COLOR_GRAY, COLOR_CYAN,
            COLOR_MAGENTA, COLOR_YELLOW, COLOR_LIGHTGRAY, COLOR_DARKGRAY, COLOR_AQUA, COLOR_FUCHSIA,
            COLOR_LIME, COLOR_MAROON, COLOR_NAVY, COLOR_OLIVE, COLOR_PURPLE, COLOR_SILVER, COLOR_TEAL
    };

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
        String regex = "";
        for (int i = 0; i < ARR_DIMEN_KEYWORDS.length; i++) {
            regex += ARR_DIMEN_KEYWORDS[i];
            if (i < ARR_DIMEN_KEYWORDS.length - 1) regex += "|";
        }
        return regex;
    }
    static String getRegexDimen() {
        String regex = REGEX_NUM + "(";
        for (int i = 0; i < ARR_DIMEN_UNITS.length; i++) {
            regex += ARR_DIMEN_UNITS[i];
            if (i < ARR_DIMEN_UNITS.length - 1) regex += "|";
        }
        regex += ")";
        return regex;
    }
    static String getRegexColors() {
        String regex = "(?i)";  // Match colour names case-insensitively
        for (int i = 0; i < ARR_COLORS.length; i++) {
            regex += ARR_COLORS[i];
            if (i < ARR_COLORS.length - 1) regex += "|";
        }
        return regex;
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
