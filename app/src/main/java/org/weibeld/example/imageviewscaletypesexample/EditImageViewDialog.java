package org.weibeld.example.imageviewscaletypesexample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import org.weibeld.example.imageviewscaletypesexample.databinding.DialogImageViewBinding;

/**
 * Created by dw on 03/11/16.
 */

public class EditImageViewDialog extends DialogFragment {

    DialogImageViewBinding mBinding;

    SharedPreferences mSharedPrefs;

    View mRootView;

    // Called after on Create and before onCreateView
    @SuppressWarnings("ResourceType")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mRootView = inflater.inflate(R.layout.dialog_image_view, null);

        mBinding = DialogImageViewBinding.bind(mRootView);

        mSharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);

        mBinding.layoutWidthEdit.setText(mSharedPrefs.getString(getString(R.string.pref_layout_width_key),
                getString(R.string.pref_layout_width_default)));
        mBinding.layoutHeightEdit.setText(mSharedPrefs.getString(getString(R.string.pref_layout_height_key),
                getString(R.string.pref_layout_height_default)));
        mBinding.backgroundEdit.setText(mSharedPrefs.getString(getString(R.string.pref_background_key),
                getString(R.color.colorAccent).toUpperCase()));
        mBinding.adjustViewBoundsEdit.setText(mSharedPrefs.getString(getString(R.string.pref_adjustViewBounds_key),
                getString(R.string.pref_adjustViewBounds_default)));
        mBinding.maxWidthEdit.setText(mSharedPrefs.getString(getString(R.string.pref_maxWidth_key),
                getString(R.string.pref_maxWidth_default)));
        mBinding.maxHeightEdit.setText(mSharedPrefs.getString(getString(R.string.pref_maxHeight_key),
                getString(R.string.pref_maxHeight_default)));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mRootView).
                setTitle(R.string.dialog_title_image_view).
                setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Read values from EditTexts and save in Shared Preferences
                    }
                }).
                setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ask user to confirm cancellation
                    }
                }).
                setNeutralButton("Reset Defaults", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    // Called after onCreateDialog
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mRootView = inflater.inflate(R.layout.dialog_image_view, container, false);
//        mLayoutWidth = (EditText) mRootView.findViewById(R.id.layout_width_edittext);
//        mLayoutHeight = (EditText) mRootView.findViewById(R.id.layout_height_edittext);
//        mBackground = (EditText) mRootView.findViewById(R.id.background_edittext);
//        mAdjustViewBounds = (EditText) mRootView.findViewById(R.id.adjustViewBounds_edittext);
//        mMaxWidth = (EditText) mRootView.findViewById(R.id.maxWidth_edittext);
//        mMaxHeight = (EditText) mRootView.findViewById(R.id.maxHeight_edittext);
//
//        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
//        mLayoutWidth.setText(prefs.getString(getString(R.string.pref_ImageView_layout_width_key),
//                getString(R.string.pref_ImageView_layout_width_default)));
//        mLayoutHeight.setText(prefs.getString(getString(R.string.pref_ImageView_layout_height_key),
//                getString(R.string.pref_ImageView_layout_height_default)));
//        mBackground.setText(prefs.getString(getString(R.string.pref_ImageView_background_key),
//                getString(R.color.colorAccent)));
//        mAdjustViewBounds.setText(prefs.getString(getString(R.string.pref_ImageView_adjustViewBounds_key),
//                getString(R.string.pref_ImageView_adjustViewBounds_default)));
//        mMaxWidth.setText(prefs.getString(getString(R.string.pref_ImageView_maxWidth_key),
//                getString(R.string.pref_ImageView_maxWidth_default)));
//        mMaxHeight.setText(prefs.getString(getString(R.string.pref_ImageView_maxHeight_key),
//                getString(R.string.pref_ImageView_maxHeight_default)));
//
//        return mRootView;
//    }


}
