package org.weibeld.example.imageviewscaletypes;

import android.app.DialogFragment;

//import org.weibeld.example.imageviewscaletypes.databinding.DialogImageViewBinding;

/**
 * Created by dw on 03/11/16.
 */

public class EditImageViewDialog extends DialogFragment {

//    DialogImageViewBinding mBind;
//
//    SharedPreferences mPrefs;
//
//    View mRootView;
//
//    // Called after on Create and before onCreateView
////    @SuppressWarnings("ResourceType")
////    @Override
////    public Dialog onCreateDialog(Bundle savedInstanceState) {
////        LayoutInflater inflater = getActivity().getLayoutInflater();
////        mRootView = inflater.inflate(R.layout.dialog_image_view, null);
////
////        mBind = DialogImageViewBinding.bind(mRootView);
////
    ////        mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
////
////        setInitialEditText();
////
////        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////        builder.setView(mRootView).
////                setTitle(R.string.dialog_title_image_view).
////                setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        // Read values from EditTexts and save in Shared Preferences
////                    }
////                }).
////                setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        // Ask user to confirm cancellation
////                    }
////                }).
////                setNeutralButton("Reset Defaults", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////
////                    }
////                });
////        return builder.create();
////    }
//
//    @SuppressWarnings("ResourceType")
//    private void setInitialEditText() {
//        mBind.layoutWidthEdit.setText(mPrefs.getString(getString(R.string.pref_layout_width_key),
//                getString(R.string.pref_layout_width_default)));
//        mBind.layoutHeightEdit.setText(mPrefs.getString(getString(R.string.pref_layout_height_key),
//                getString(R.string.pref_layout_height_default)));
//        mBind.backgroundEdit.setText(mPrefs.getString(getString(R.string.pref_background_key),
//                getString(R.color.colorAccent).toUpperCase()));
//        mBind.adjustViewBoundsEdit.setText(mPrefs.getString(getString(R.string.pref_adjustViewBounds_key),
//                getString(R.string.pref_adjustViewBounds_default)));
//        mBind.maxWidthEdit.setText(mPrefs.getString(getString(R.string.pref_maxWidth_key),
//                getString(R.string.pref_maxWidth_default)));
//        mBind.maxHeightEdit.setText(mPrefs.getString(getString(R.string.pref_maxHeight_key),
//                getString(R.string.pref_maxHeight_default)));
//    }
//
//    // Called after onCreateDialog
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mRootView = inflater.inflate(R.layout.dialog_image_view, container, false);
//        mBind = DialogImageViewBinding.bind(mRootView);
//        mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
//        setInitialEditText();
//        return mRootView;
//    }


}
