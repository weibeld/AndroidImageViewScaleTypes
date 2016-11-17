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
    static final String COLOR_RED = "red";
    static final String COLOR_BLUE = "blue";
    static final String COLOR_GREEN = "green";
    static final String COLOR_BLACK = "black";
    static final String COLOR_WHITE = "white";
    static final String COLOR_GRAY = "gray";
    static final String COLOR_CYAN = "cyan";
    static final String COLOR_MAGENTA = "magenta";
    static final String COLOR_YELLOW = "yellow";
    static final String COLOR_LIGHTGRAY = "lightgray";
    static final String COLOR_DARKGRAY = "darkgray";
    static final String COLOR_AQUA = "aqua";
    static final String COLOR_FUCHSIA = "fuchsia";
    static final String COLOR_LIME = "lime";
    static final String COLOR_MAROON = "maroon";
    static final String COLOR_NAVY = "navy";
    static final String COLOR_OLIVE = "olive";
    static final String COLOR_PURPLE = "purple";
    static final String COLOR_SILVER = "silver";
    static final String COLOR_TEAL = "teal";
    static final String[] ARR_COLORS = new String[] {
            COLOR_RED, COLOR_BLUE, COLOR_GREEN, COLOR_BLACK, COLOR_WHITE, COLOR_GRAY, COLOR_CYAN,
            COLOR_MAGENTA, COLOR_YELLOW, COLOR_LIGHTGRAY, COLOR_DARKGRAY, COLOR_AQUA, COLOR_FUCHSIA,
            COLOR_LIME, COLOR_MAROON, COLOR_NAVY, COLOR_OLIVE, COLOR_PURPLE, COLOR_SILVER, COLOR_TEAL
    };
    static final String REGEX_NAMED_COLORS = "red|blue|green|black|white|gray|cyan|magenta|yellow|lightgray|darkgray|grey|lightgrey|darkgrey|aqua|fuchsia|lime|maroon|navy|olive|purple|silver|teal";

    static final String REGEX_NUM = "((0\\.\\d+)|([1-9]\\d*\\.\\d+)|(0\\.)|([1-9]\\d*\\.)|(\\.\\d+)|(0)|([1-9]\\d*))";

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
