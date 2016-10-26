package org.weibeld.example.imageviewscaletypesexample;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by dw on 24/10/16.
 */

public class ScaleTypeFragment extends Fragment {

    private final String LOG_TAG = ScaleTypeFragment.class.getSimpleName();

    private View mRootView;
    private ImageView mImageView;
    private TextView mImageInfoTextView;
    private ImageView.ScaleType mScaleType;

    // Temporary button for querying sizes of ImageView and image
    private Button mTmpButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mScaleType = Data.SCALE_TYPES[getArguments().getInt(Data.ARG_POSITION)];
        Log.v(LOG_TAG, "onAttach of " + mScaleType.name());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate of " + mScaleType.name());
    }

    /* Called between onCreate and onActivityCreated */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment, container, false);
        mImageView = (ImageView) mRootView.findViewById(R.id.imageview);
        mImageInfoTextView = (TextView) mRootView.findViewById(R.id.image_info);

        // Temporary button for querying sizes of ImageView and image
        mTmpButton = (Button) mRootView.findViewById(R.id.button);
        mTmpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setImageInfoText();
            }
        });

        mImageView.setScaleType(mScaleType);

        Picasso.with(getActivity()).setIndicatorsEnabled(true);
        Picasso
                .with(getActivity())
                .load(Data.IMAGE_URL)
                //.resize(1850, 2780)
                .into(mImageView);

        Log.v(LOG_TAG, "onCreateView of " + mScaleType.name());

        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(LOG_TAG, "onActivityCreated of " + mScaleType.name());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "onStart of " + mScaleType.name());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "onResume of " + mScaleType.name());
        //Log.v(LOG_TAG, "mImageView.getWidth(): " + mImageView.getWidth() + " of " + mScaleType.name());
        //Log.v(LOG_TAG, "mImageView.getDrawable().getIntrinsicWidth(): " + mImageView.getDrawable().getIntrinsicWidth() + " of " + mScaleType.name());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(LOG_TAG, "onPause of " + mScaleType.name());

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(LOG_TAG, "onStop of " + mScaleType.name());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy of " + mScaleType.name());
    }





    private void setImageInfoText() {

        /* Width and height of the ImageView */
        int widthView = mImageView.getWidth();
        int heightView = mImageView.getHeight();

        /* Width and height of the ORIGINAL image */
        int widthImageOrig = mImageView.getDrawable().getIntrinsicWidth();
        int heightImageOrig = mImageView.getDrawable().getIntrinsicHeight();


        /* Width and height of the image after all scaling done by the ImageView */
        int widthImageScaled;
        int heightImageScaled;
        Matrix matrix = mImageView.getImageMatrix();
        if (mScaleType != ImageView.ScaleType.FIT_XY) {
            // The scaling the ImageView performs to the width and height of the image is defined
            // in the ImageView's transformation matrix as entries MSCALE_X and MSCALE_Y.
            float[] matrixVals = new float[9];
            matrix.getValues(matrixVals);
            float scaleX = matrixVals[Matrix.MSCALE_X];
            float scaleY = matrixVals[Matrix.MSCALE_Y];
            // Multiplying the original size with the scale yields the scaled (displayed) size
            widthImageScaled = Math.round(widthImageOrig * scaleX);
            heightImageScaled = Math.round(heightImageOrig * scaleY);
        }
        else {
            // Second approach to get width and height of image (drawable) after scaling
            Drawable drawable = mImageView.getDrawable();
            Rect rect = drawable.getBounds();
            widthImageScaled = rect.width();
            heightImageScaled = rect.height();
        }

        Log.v(LOG_TAG, "Matrix: " + matrix + " of " + mScaleType.name());

        String str =
                getString(R.string.imageview_size) + " " + formatSize(widthView, heightView) + "\n"
                + getString(R.string.image_size_original) + " " + formatSize(widthImageOrig, heightImageOrig) + "\n"
                + getString(R.string.image_size_displayed) + " " + formatSize(widthImageScaled, heightImageScaled) + "\n"
                + "Matrix:" + "\n"
                + formatMatrix(matrix);

        mImageInfoTextView.setText(str);
    }

    private String formatSize(int width, int height) {
        return width + "x" + height + " px";
    }

    // Format the 3x3 transformation matrix of the ImageView as 3 lines of text
    private String formatMatrix(Matrix matrix) {
        float[] vals = new float[9];
        matrix.getValues(vals);
        String str = "";
        for (int i = 0; i <= 8; i++) {
            float v = vals[i];
            // Add padding before number to vertically align decimal points
            if (v >= 0) {
                if (v < 10) str = str + "    ";
                else if (v < 100) str = str + "   ";
                else if (v < 1000) str = str + "  ";
            }
            else {
                if (v > -10) str = str + "   ";
                else if (v > -100) str = str + "  ";
                else if (v > -1000) str = str + " ";
            }
            str = str + String.format("%9.3f ", v);
            if (i == 2 || i == 5) str = str + "\n";
        }
        return str;
    }
}
