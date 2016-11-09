package org.weibeld.example.imageviewscaletypes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    private ListPopupWindow popupWindow;

    private final String[] LAYOUT_WIDTH_HEIGHT = new String[] {
            "match_parent", "wrap_content"
    };

    private final String[] BOOLEAN = new String[] {
            "true", "false"
    };

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


        popupWindow = new ListPopupWindow(this);
        Log.v(LOG_TAG, "vertical offset: " + popupWindow.getVerticalOffset());
        popupWindow.setVerticalOffset(-10);
        popupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, LAYOUT_WIDTH_HEIGHT));
        popupWindow.setAnchorView(mBind.layoutHeightEdit);
        popupWindow.setModal(true);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBind.layoutHeightEdit.setText(LAYOUT_WIDTH_HEIGHT[position]);
                popupWindow.dismiss();
            }
        });


        mBind.layoutHeightEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(LOG_TAG, "onClick");
            }
        });

        mBind.layoutHeightEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    Log.v(LOG_TAG, "got focus");
                else
                    Log.v(LOG_TAG, "lost focus");
            }
        });



        mBind.layoutHeightEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                Log.v(LOG_TAG, "onTouch");

                // Check if touch point is in the area of the right button
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.v(LOG_TAG, "onTouch DOWN");
                    if(event.getX() >= (mBind.layoutHeightEdit.getWidth() - mBind.layoutHeightEdit
                            .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        mBind.layoutHeightEdit.requestFocus();
                        //mBind.layoutHeightEdit.setSelection(mBind.layoutHeightEdit.getText().length()-1);
                        popupWindow.show();
                        return true;
                    }
                }
                return false;
            }
        });




//        mBind.adjustViewBoundsSpinner.setAdapter(new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_dropdown_item, BOOLEAN));

        //mBind.adjustViewBoundsEdit.setInputType(InputType.TYPE_NULL);
        mBind.adjustViewBoundsEdit.setKeyListener(null);
        //mBind.adjustViewBoundsEdit.setEnabled(false);
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
