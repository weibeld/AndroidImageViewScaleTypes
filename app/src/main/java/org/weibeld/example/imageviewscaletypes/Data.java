package org.weibeld.example.imageviewscaletypes;

import android.widget.ImageView;

/**
 * Created by dw on 24/10/16.
 */

class Data {

    private Data() {}

    static final String TRUE = "true";
    static final String FALSE = "false";
    static final String WRAP_CONTENT = "wrap_content";
    static final String MATCH_PARENT = "match_parent";
    static final String UNIT_DP = "dp";
    static final String UNIT_SP = "sp";
    static final String UNIT_PX = "px";
    static final String UNIT_IN = "in";
    static final String UNIT_MM = "mm";

    static final String[] ARR_BOOL = new String[] {
            TRUE, FALSE
    };

    static final String[] ARR_DIMEN_KEYWORDS = new String[] {
            WRAP_CONTENT, MATCH_PARENT
    };

    static final String[] ARR_DIMEN_UNITS = new String[] {
            UNIT_DP, UNIT_SP, UNIT_PX, UNIT_IN, UNIT_MM
    };

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
