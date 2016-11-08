package org.weibeld.example.imageviewscaletypes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.weibeld.example.imageviewscaletypes.databinding.ActivityEditImageViewBinding;

/**
 * Created by dw on 07/11/16.
 */

public class EditImageViewActivity extends AppCompatActivity {

    private final String LOG_TAG = EditImageViewActivity.class.getSimpleName();

    // Binding to the named XML layout UI elements (Data Binding Library)
    ActivityEditImageViewBinding mBind;

    // Reference to the SharedPreferences of this Activity
    SharedPreferences mPrefs;

    // Arrays for looping through text fields and SharedPreferences
    private String[] mPrefKeys;
    private String[] mPrefDefaults;
    private TextView[] mTextFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_edit_image_view);

        mPrefs = getPreferences(Context.MODE_PRIVATE);

        // Set up Toolbar as app bar and define what to do when the X icon is clicked
        // Possible Android bug: setNavigationOnClickListener must come AFTER setSupportActionBar
        setSupportActionBar(mBind.toolbar);
        mBind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmExit();
            }
        });

        initArrays();
        loadValues();
    }

    // Arrays serving as mapping between SharedPreference keys, default values, and UI text fields
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

    // Get the SharedPreference entry at a specific index in the arrays initialised by initArrays
    private String getPref(int index) {
        return mPrefs.getString(mPrefKeys[index], mPrefDefaults[index]);
    }

    // Load values from SharedPreferences into the corresponding text fields
    private void loadValues() {
        for (int i = 0; i < mTextFields.length; i++) {
            mTextFields[i].setText(getPref(i));
        }
    }

    // Load default values of SharedPreferences into the corresponding text fields
    private void loadDefaultValues() {
        for (int i = 0; i < mTextFields.length; i++) {
            mTextFields[i].setText(mPrefDefaults[i]);
        }
    }

    // Save values from text fields to the corresponding SharedPreference entries
    private void saveValues() {
        for (int i = 0; i < mTextFields.length; i++) {
            mPrefs.edit().putString(mPrefKeys[i], mTextFields[i].getText().toString()).apply();
        }
    }

    // Check if any changes have been made so far in the text fields w.r.t. the SharedPreferences
    private boolean hasChanges() {
        for (int i = 0; i < mTextFields.length; i++) {
            if (!mTextFields[i].getText().toString().equals(getPref(i))) return true;
        }
        return false;
    }

    // Ask user to confirm exit of current activity (either after clicking the X in the toolbar or
    // pressing the device back button) if any edits have been made so far in the text fields
    private void confirmExit() {
        if (hasChanges()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.exit_dialog_message).
                    setNegativeButton(R.string.exit_dialog_neg_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    }).
                    setPositiveButton(R.string.exit_dialog_pos_button, new DialogInterface.OnClickListener() {
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

    // Called when the device back button is pressed
    @Override
    public void onBackPressed() {
        confirmExit();
    }

    // Called when the save button in the toolbar is clicked
    public void onSaveClicked(View view) {
        saveValues();
        Toast.makeText(this, R.string.toast_changes_applied, Toast.LENGTH_SHORT).show();
        finish();
    }

    // Called when the "Reset Defaults" button is clicked
    public void onResetClicked(View view) {
        loadDefaultValues();
    }

}
