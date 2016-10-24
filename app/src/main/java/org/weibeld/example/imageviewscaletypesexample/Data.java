package org.weibeld.example.imageviewscaletypesexample;

import android.widget.ImageView;

/**
 * Created by dw on 24/10/16.
 */

public class Data {

    private Data() {}

    // All the ImageView scale types in the order that we want to display them
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

    // Page position argument passed to the ScaleTypeFragment
    public static final String ARG_POSITION = "position";
}
