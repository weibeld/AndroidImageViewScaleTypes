package org.weibeld.example.imageviewscaletypesexample;

import android.R;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dw on 24/10/16.
 */

public class ScaleTypeFragment extends Fragment {

    private final String LOG_TAG = ScaleTypeFragment.class.getSimpleName();

    private View mRootView;
    private ImageView mImageView;
    private TextView mImageInfoTextView;
    private ImageView.ScaleType mScaleType;

    private SharedPreferences mSharedPrefs;

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

    /* Called after onCreate and before onActivityCreated */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView of " + mScaleType.name());

        mSharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);

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

        return mRootView;
    }

    // Read ImageView properties from SharedPreferences and apply them to the ImageView
    private void configureImageView() {

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
        // Read the URI of the image to display from the SharedPreferences of the MainActivity,
        // and load this image into the ImageView. This is done in onResume so that the image is
        // updated automatically when the user returns from the "choose image" activity. Since
        // the choose image activity obscures this fragment, it is guaranteed that onResume is
        // called as soon as this fragment gets focus again (the same applies to the next and the
        // previous fragments of the ViewPager). For all the other fragments, when the user flips
        // to them, the full callback chain will be called (onCreate -> onStart -> onResume), so
        // in these fragments, the new image will be displayed too.
        SharedPreferences sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        Uri uri = Uri.parse(sharedPrefs.getString(getString(R.string.pref_image_key),
                getString(R.string.pref_image_default)));
        Log.v(LOG_TAG, "Image URI in SharedPreferences: " + uri.toString());

        // If NOT loading the default image, check if we have read URI permission for this image
        if (!uri.toString().equals(getString(R.string.pref_image_default))) {
            int readPerm = getActivity().checkUriPermission(uri, Binder.getCallingPid(),
                    Binder.getCallingUid(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (readPerm != PackageManager.PERMISSION_GRANTED) {
                // We get here if we don't have a read URI permission for this image, or if the
                // image has been deleted. In any case, load the default image instead. Attempting
                // to load an image without URI read permission, would result in the following:
                // setImageDrawable, setImageBitmap: raise "SecurityException: Permission Denial"
                // setImageURI, Picasso: no exception raised, but no image loaded into ImageView
                Toast.makeText(getActivity(), "No URI permission for " + uri.toString(), Toast.LENGTH_SHORT).show();
                Log.v(LOG_TAG, mScaleType.name() + ": no URI permission for " + uri.toString());
                uri = Uri.parse(getString(R.string.pref_image_default));
            }
        }
        loadWithPicasso(uri);
    }

    // Throws permission denial exception
    private void loadWithSetImageDrawable(Uri imageUri) {
        Log.v(LOG_TAG, mScaleType.name() + ": loading image with setImageDrawable " + imageUri.toString());
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
            Drawable drawable = Drawable.createFromStream(inputStream, imageUri.toString());
            mImageView.setImageDrawable(drawable);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Throws permission denial exception
    private void loadWithSetImageBitmap(Uri imageUri) {
        Log.v(LOG_TAG, mScaleType.name() + ": loading image with setImageBitmap " + imageUri.toString());
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            mImageView.setImageBitmap(bitmap);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Catches permission denial exception and prints stacktrace, no image loaded into the ImageView
    private void loadWithSetImageURI(Uri imageUri) {
        Log.v(LOG_TAG, mScaleType.name() + ": loading image with setImageURI " + imageUri.toString());
        mImageView.setImageURI(imageUri);
    }

    // Catches permission denial exception but does not print any log output about it, no image
    // loaded into the ImageView
    private void loadWithPicasso(Uri imageUri) {
        Log.v(LOG_TAG, mScaleType.name() + ": loading image with Picasso " + imageUri.toString());
        Picasso.with(getActivity()).setIndicatorsEnabled(true);
        Picasso
                .with(getActivity())
                .load(imageUri)
                //.resize(1850, 2780)
                .into(mImageView);
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

        /* Width and height (as drawn on screen) of the ImageView */
        int widthView = mImageView.getWidth();
        int heightView = mImageView.getHeight();

        /* ORIGINAL width and height of the image (without any scaling) */
        int widthImageOrig = mImageView.getDrawable().getIntrinsicWidth();
        int heightImageOrig = mImageView.getDrawable().getIntrinsicHeight();

        /* Width and height of the image after scaling (done by the ImageView) */
        int widthImageScaled;
        int heightImageScaled;
        Matrix matrix = mImageView.getImageMatrix();
        if (mScaleType != ImageView.ScaleType.FIT_XY) {
            // For all scale types except FIT_XY, the scale that the ImageView applies to width
            // and height of the original image is declared as MSCALE_X and MSCALE_Y in the
            // ImageView's transformation matrix.
            float[] matrixVals = new float[9];
            matrix.getValues(matrixVals);
            float scaleX = matrixVals[Matrix.MSCALE_X];
            float scaleY = matrixVals[Matrix.MSCALE_Y];
            // Multiplying the original size with the scale yields the scaled (displayed) size
            widthImageScaled = Math.round(widthImageOrig * scaleX);
            heightImageScaled = Math.round(heightImageOrig * scaleY);
        }
        else {
            // For the FIT_XY scale type, the transformation matrix is not used, but the bounds
            // of the Drawable (image) are directly set to equal the bounds of the ImageView.
            Drawable drawable = mImageView.getDrawable();
            Rect rect = drawable.getBounds();
            widthImageScaled = rect.width();
            heightImageScaled = rect.height();
            //widthImageScaled = widthView;
            //heightImageScaled = heightView;
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
