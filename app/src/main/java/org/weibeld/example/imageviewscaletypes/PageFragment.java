package org.weibeld.example.imageviewscaletypes;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

    // TODO: achieve measurement of ImageView and image without a refresh button
    // onCreate --> onCreateView --> onActivityCreated
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView of " + mScaleType.name());
        mRootView = inflater.inflate(R.layout.fragment_page, container, false);
        mBind = FragmentPageBinding.bind(mRootView);
        mBind.imageView.setScaleType(mScaleType);

        ViewTreeObserver o = mBind.imageView.getViewTreeObserver();
        // TODO: remove the listener as soon as all sizes are correctly returned for the first time
        o.addOnGlobalLayoutListener(() -> {
            // It may happen that the Fragment is not attached to its Activity, in which case
            // accessing the resources (getString) would cause an exception.
            if (isAdded()) {
                Size vSize = getImageViewSize(mBind.imageView);
                Size iSizeOrig = getOriginalImageSize(mBind.imageView);
                Size iSizeDrawn = getDrawnImageSize(mBind.imageView);
                String str = getString(R.string.info_image_view) + " " + vSize + "\n" +
                        getString(R.string.info_displayed_image) + " " + iSizeDrawn + "\n" +
                        getString(R.string.info_original_image) + " " + iSizeOrig;
                mBind.imageInfo.setText(str);
                Log.v(LOG_TAG, "onGlobalLayoutListener " + mScaleType.name() + ":" +
                        "\nImageView size: " + getImageViewSize(mBind.imageView) +
                        "\nOriginal image size: " + getOriginalImageSize(mBind.imageView) +
                        "\nDisplayed image size: " + getDrawnImageSize(mBind.imageView));
            }
        });




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
        setupImageView();
        // TODO: improve loading of large images (probably use Glide instead of Picasso)
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
        super.onDetach();
        Log.v(LOG_TAG, "onDetach of " + mScaleType.name());
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
        Uri uri = Uri.parse(Pref.get(getActivity(), R.string.pref_image_key));
        Log.v(LOG_TAG, "Image URI in SharedPreferences: " + uri.toString());

        // If NOT loading the default image, check if we have read URI permission for this image
        if (!uri.toString().equals(getString(R.string.pref_image_default))) {
            int p = getActivity().checkUriPermission(uri, Binder.getCallingPid(), Binder.getCallingUid(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (p != PackageManager.PERMISSION_GRANTED) {
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

    // Reaction on exceptions:
    // PermissionDenialException: app crash
    // OutOfMemoryError: app crash
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
    // Reaction on exceptions:
    // PermissionDenialException: app crash
    // OutOfMemoryError: app crash
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

    // Reaction on exceptions:
    // PermissionDenialException: catch and print stacktrace (no image loaded)
    // OutOfMemoryError: app crash
    private void loadWithSetImageURI(Uri imageUri) {
        Log.v(LOG_TAG, mScaleType.name() + ": loading image with setImageURI " + imageUri.toString());
        mBind.imageView.setImageURI(imageUri);
    }

    // Reaction on exceptions:
    // PermissionDenialException: catch but no log output (no image loaded)
    // OutOfMemoryError: catch and log output (no image loaded)
    private void loadWithPicasso(Uri imageUri) {
        Log.v(LOG_TAG, mScaleType.name() + ": loading image with Picasso " + imageUri.toString());
        Picasso.with(getActivity()).setIndicatorsEnabled(true);
        Picasso
                .with(getActivity())
                .load(imageUri)
                //.resize(1850, 2780)
                .into(mBind.imageView);
    }

    private void loadWithGlide(Uri imageUri) {
        Glide.with(this).load(imageUri).into(mBind.imageView);
    }

    // Width and height (as drawn on screen) of ImageView
    private Size getImageViewSize(ImageView v) {
        return new Size(v.getWidth(), v.getHeight());
    }

    // Original width and height of the image inside the ImageView
    private Size getOriginalImageSize(ImageView v) {
        Drawable d = v.getDrawable();
        // getDrawable returns null if no Drawable has (yet) been assigned to the ImageView
        if (d == null) return new Size(0, 0);
        // getIntrinsicX returns -1 if the Drawable has no width/height (such as a solid color)
        else return new Size(d.getIntrinsicWidth(), d.getIntrinsicHeight());
    }

    // Width and height of the image inside the ImageView as drawn on screen
    private Size getDrawnImageSize(ImageView v) {
        Drawable d = v.getDrawable();
        // getDrawable returns null if no Drawable has (yet) been assigned to the ImageView
        if (d == null)
            return new Size(0, 0);
        // For FIT_XY, the size of the image is directly set to  the size of the ImageView
        if (mScaleType == ImageView.ScaleType.FIT_XY) {
            return getImageViewSize(v);
        }
        // For the other scale types, the scale factors defined in the image matrix are used
        else {
            float[] matrixVals = new float[9];
            // If the ImageView has no matrix set, then getImageMatrix returns the identity matrix
            v.getImageMatrix().getValues(matrixVals);
            float scaleX = matrixVals[Matrix.MSCALE_X];
            float scaleY = matrixVals[Matrix.MSCALE_Y];
            Size o = getOriginalImageSize(v);
            return new Size(Math.round(o.getWidth() * scaleX), Math.round(o.getHeight() * scaleY));
        }
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
