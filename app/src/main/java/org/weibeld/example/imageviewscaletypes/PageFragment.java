package org.weibeld.example.imageviewscaletypes;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.weibeld.example.imageviewscaletypes.databinding.FragmentPageBinding;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.weibeld.example.imageviewscaletypes.Data.DEACTIVATED_MARKER;

/**
 * Created by dw on 24/10/16.
 */

// Lifetime of Fragment when swiped in ViewPager:
// onCreateView -> onActivity Created -> onStart -> onResume -> onPause -> onStop -> onDestroyView
// Lifetime of Fragment when starting another Activity and returning:
// onStart -> onResume -> onPause -> onStop
public class PageFragment extends Fragment {

    private final String LOG_TAG = PageFragment.class.getSimpleName();

    // Binding to layout elements through Data Binding Library
    private FragmentPageBinding mBind;

    private View mRootView;
    private ImageView.ScaleType mScaleType;

    // on Attach --> onCreate
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mScaleType = Data.SCALE_TYPES[getArguments().getInt(Data.ARG_POSITION)];
        Log.v(LOG_TAG, "onAttach of " + mScaleType.name());
    }

    // onAttach --> onCreate --> onCreateView
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate of " + mScaleType.name());
    }

    // onCreate --> onCreateView --> onActivityCreated
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView of " + mScaleType.name());
        mRootView = inflater.inflate(R.layout.fragment_page, container, false);
        mBind = FragmentPageBinding.bind(mRootView);
        mBind.imageView.setScaleType(mScaleType);
        // Temporary button for querying sizes of ImageView and image
//        mBind.button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                setImageInfoText();
//            }
//        });
        return mRootView;
    }

    // onCreateView --> onActivityCreated --> onViewStateRestored
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(LOG_TAG, "onActivityCreated of " + mScaleType.name());
    }

    // onViewStateRestored --> onStart --> onResume
    @Override
    public void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "onStart of " + mScaleType.name());
    }

    // onStart --> onResume --> onPause
    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "onResume of " + mScaleType.name());

        // TODO: recreate the two or three stopped Fragments after return from EditImageViewActivity
        setupImageView();
        // TODO: improve loading of large images
        loadImage();
    }

    // onResume --> onPause --> onStop
    @Override
    public void onPause() {
        super.onPause();
        Log.v(LOG_TAG, "onPause of " + mScaleType.name());

    }

    // onPause --> onStop --> onDestroyView
    @Override
    public void onStop() {
        super.onStop();
        Log.v(LOG_TAG, "onStop of " + mScaleType.name());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(LOG_TAG, "onDestroyView of " + mScaleType.name());
    }

    // onDestroyView --> onDestroy --> onDetach
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy of " + mScaleType.name());
    }

    @Override
    public void onDetach() {
        Log.v(LOG_TAG, "onDetach of " + mScaleType.name());
        super.onDetach();
    }

    // Set ImageView properties according to values in SharedPreferences. Note: when returning from
    // EditImageViewProperties, the callbacks called of this Fragment are onStart and onResume.
    private void setupImageView() {
        // Set adjustViewBounds (required boolean field)
        String s = Pref.get(getActivity(), R.string.pref_adjustViewBounds_key);
        mBind.imageView.setAdjustViewBounds(s.equals(Data.TRUE));

        // Set layout_width and layout_height (required fields)
        String w = Pref.get(getActivity(), R.string.pref_layout_width_key);
        mBind.imageView.getLayoutParams().width = dimenStrToInt(w);
        String h = Pref.get(getActivity(), R.string.pref_layout_height_key);
        mBind.imageView.getLayoutParams().height = dimenStrToInt(h);

        // Set background colour (optional field)
        String color = Pref.get(getActivity(), R.string.pref_background_key);
        if (!color.equals(""))
            mBind.imageView.setBackgroundColor(Color.parseColor(color));



        // Set maxWidth and maxHeight (optional fields that may be deactivated)
        String maxW = Pref.get(getActivity(), R.string.pref_maxWidth_key);
        if (!maxW.equals("") && !maxW.contains(DEACTIVATED_MARKER))
            mBind.imageView.setMaxWidth(dimenStrToInt(maxW));
        String maxH = Pref.get(getActivity(), R.string.pref_maxHeight_key);
        if (!maxH.equals("") && !maxH.contains(Data.DEACTIVATED_MARKER))
            mBind.imageView.setMaxHeight(dimenStrToInt(maxH));
    }

    private int dimenStrToInt(String s) {
        switch (s) {
            case Data.WRAP_CONTENT:
                return ViewGroup.LayoutParams.WRAP_CONTENT;
            case Data.MATCH_PARENT:
                return ViewGroup.LayoutParams.MATCH_PARENT;
            default:
                Util.Dimension dim = Util.parseDimension(s);
                return (int) TypedValue.applyDimension(dim.unit, dim.value, getResources().getDisplayMetrics());
        }
    }

    private void loadImage() {
        // Read the URI of the image to display from the SharedPreferences of the MainActivity,
        // and load this image into the ImageView. This is done in onResume so that the image is
        // updated automatically when the user returns from the "choose image" activity. Since
        // the choose image activity obscures this fragment_page, it is guaranteed that onResume is
        // called as soon as this fragment_page gets focus again (the same applies to the next and the
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
            mBind.imageView.setImageDrawable(drawable);
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
            mBind.imageView.setImageBitmap(bitmap);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Catches permission denial exception and prints stacktrace, no image loaded into the ImageView
    private void loadWithSetImageURI(Uri imageUri) {
        Log.v(LOG_TAG, mScaleType.name() + ": loading image with setImageURI " + imageUri.toString());
        mBind.imageView.setImageURI(imageUri);
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
                .into(mBind.imageView);
    }

    private void setImageInfoText() {

        /* Width and height (as drawn on screen) of the ImageView */
        int widthView = mBind.imageView.getWidth();
        int heightView = mBind.imageView.getHeight();

        /* ORIGINAL width and height of the image (without any scaling) */
        int widthImageOrig = mBind.imageView.getDrawable().getIntrinsicWidth();
        int heightImageOrig = mBind.imageView.getDrawable().getIntrinsicHeight();

        /* Width and height of the image after scaling (done by the ImageView) */
        int widthImageScaled;
        int heightImageScaled;
        Matrix matrix = mBind.imageView.getImageMatrix();
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
            Drawable drawable = mBind.imageView.getDrawable();
            Rect rect = drawable.getBounds();
            widthImageScaled = rect.width();
            heightImageScaled = rect.height();
            //widthImageScaled = widthView;
            //heightImageScaled = heightView;
        }

        Log.v(LOG_TAG, "Matrix: " + matrix + " of " + mScaleType.name());

        String str =
                getString(R.string.info_image_view) + " " + formatSize(widthView, heightView) + "\n"
                + getString(R.string.info_original_image) + " " + formatSize(widthImageOrig, heightImageOrig) + "\n"
                + getString(R.string.info_displayed_image) + " " + formatSize(widthImageScaled, heightImageScaled) + "\n"
                + "Matrix:" + "\n"
                + formatMatrix(matrix);

        mBind.imageInfo.setText(str);
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
