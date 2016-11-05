package org.weibeld.example.imageviewscaletypesexample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

/**
 * Created by dw on 02/11/16.
 */

public class TestDialog extends DialogFragment {

    private final String LOG_TAG = TestDialog.class.getSimpleName();

    // Called always
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.dialog_image_view, container, false);
//    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        return dialog;
//    }

    // Called only when this DialogFragment is displayed as a real dialog (and not fullscreen)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(inflater.inflate(R.layout.dialog_image_view, null)).
                setTitle("Dialog Title").
                setPositiveButton("Do it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v(LOG_TAG, "Positive button");
                    }
                }).
                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v(LOG_TAG, "Negative button");
                    }
                });
        return builder.create();
    }

//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        Log.v(LOG_TAG,"Dialog dismissed");
//    }

//    @Override
//    public void onCancel(DialogInterface dialog) {
//        Log.v(LOG_TAG, "Dialog cancelled");
//    }
}
