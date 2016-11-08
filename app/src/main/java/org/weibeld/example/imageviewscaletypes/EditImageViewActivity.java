package org.weibeld.example.imageviewscaletypes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.weibeld.example.imageviewscaletypes.databinding.ActivityEditImageViewBinding;

/**
 * Created by dw on 07/11/16.
 */

public class EditImageViewActivity extends AppCompatActivity {

    private final String LOG_TAG = EditImageViewActivity.class.getSimpleName();

    private String[] mPrefKeys;
    private String[] mPrefDefaults;
    private TextView[] mTextFields;

    ActivityEditImageViewBinding mBind;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_edit_image_view);

        setSupportActionBar(mBind.toolbar);
        mBind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmExit();
            }
        });

        mPrefs = getPreferences(Context.MODE_PRIVATE);
        initArrays();
        loadValues();
    }

    private void initArrays() {
        mPrefKeys = new String[] {
                getString(R.string.pref_layout_width_key),
                getString(R.string.pref_layout_height_key),
                getString(R.string.pref_background_key),
                getString(R.string.pref_adjustViewBounds_key),
                getString(R.string.pref_maxWidth_key),
                getString(R.string.pref_maxHeight_key)
        };
        mPrefDefaults = new String[] {
                getString(R.string.pref_layout_width_default),
                getString(R.string.pref_layout_height_default),
                getString(R.color.colorAccent).toUpperCase(),
                getString(R.string.pref_adjustViewBounds_default),
                getString(R.string.pref_maxWidth_default),
                getString(R.string.pref_maxHeight_default)
        };
        mTextFields = new TextView[] {
                mBind.layoutWidthEdit,
                mBind.layoutHeightEdit,
                mBind.backgroundEdit,
                mBind.adjustViewBoundsEdit,
                mBind.maxWidthEdit,
                mBind.maxHeightEdit
        };
    }

    @Override
    public void onBackPressed() {
        confirmExit();
    }

    // Check if any changes have been made already, and if yes, show a dialog to confirm the
    // the discarding of these changes. If no changes have been made, finish the activity.
    private void confirmExit() {
        if (hasChanges()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.back_dialog_message).
                    setNegativeButton(R.string.back_dialog_neg_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    }).
                    setPositiveButton(R.string.back_dialog_pos_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
        }
        else {
            Toast.makeText(this, R.string.toast_no_changes, Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    // Returns true if any text field value is different from its corresponding SharedPreferences
    // value, and returns false otherwise.
    private boolean hasChanges() {
        return false;
    }

    // SharedPreferences --> Text Fields
    private void loadValues() {
        for (int i = 0; i < mTextFields.length; i++) {
            mTextFields[i].setText(mPrefs.getString(mPrefKeys[i], mPrefDefaults[i]));
        }
    }

    // Text Fields --> SharedPreferences
    private void saveValues() {
        for (int i = 0; i < mTextFields.length; i++) {
            mPrefs.edit().putString(mPrefKeys[i], mTextFields[i].getText().toString()).apply();
        }
    }


    public void onClickSave(View view) {
        Log.v(LOG_TAG, "Save button clicked");
        saveValues();
        Toast.makeText(this, R.string.toast_changes_applied, Toast.LENGTH_SHORT).show();
        finish();
    }

    // SharedPreferences (defaults) --> Text Fields
    public void onClickReset(View view) {
        Log.v(LOG_TAG, "Reset button clicked");
        // Delete all entries in the SharedPreferences so that the default values will be used
        mPrefs.edit().clear().apply();
        loadValues();
    }

}
