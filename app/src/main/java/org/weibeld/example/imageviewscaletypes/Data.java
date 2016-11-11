package org.weibeld.example.imageviewscaletypes;

import android.widget.ImageView;

/**
 * Created by dw on 24/10/16.
 */

public class Data {

    private Data() {}

    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String WRAP_CONTENT = "wrap_content";
    public static final String MATCH_PARENT = "match_parent";

    public static final String[] ARR_BOOL = new String[] {
            TRUE, FALSE
    };

    public static final String[] ARR_DIMEN_KEYWORDS = new String[] {
            WRAP_CONTENT, MATCH_PARENT
    };

    // All the ImageView scale types in the order in which we want to display them
    public static final ImageView.ScaleType[] SCALE_TYPES = new ImageView.ScaleType[] {
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
    public static final String ARG_POSITION = "position";

    // Code for the choose image intent
    public static final int CHOOSE_IMAGE_REQUEST_CODE = 1;
}
