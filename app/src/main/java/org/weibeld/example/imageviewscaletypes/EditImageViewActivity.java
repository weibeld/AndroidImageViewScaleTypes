package org.weibeld.example.imageviewscaletypes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

    // Mapping from text fields to SharedPreferences and  vice versa
    private EditText[] mTextFields;
    private int[] mPrefKeys;

    // Dropdown popup windows that are associated with some of the text fields
    private ListPopupWindow[] mPopupWindows;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_edit_image_view);

        // Set up Toolbar as app bar and define what to do when the X icon is clicked
        // Possible Android bug: setNavigationOnClickListener must come AFTER setSupportActionBar
        setSupportActionBar(mBind.toolbar);
        mBind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmExit();
            }
        });

        initMapping();
        setupPopupFields();
        setupEmptiableFields();
        setupAdjustViewBoundsField();
        // TODO: add colour picker

        // Load text into fields, call after above setup methods so that listeners are triggered
        loadValues();
    }

    // Create mapping between text fields and SharedPreferences entries
    private void initMapping() {
        mTextFields = new EditText[] {
                mBind.layoutWidthEdit,
                mBind.layoutHeightEdit,
                mBind.backgroundEdit,
                mBind.adjustViewBoundsEdit,
                mBind.maxWidthEdit,
                mBind.maxHeightEdit
        };
        mPrefKeys = new int[] {
                R.string.pref_layout_width_key,
                R.string.pref_layout_height_key,
                R.string.pref_background_key,
                R.string.pref_adjustViewBounds_key,
                R.string.pref_maxWidth_key,
                R.string.pref_maxHeight_key
        };
    }

    // Add ListPopupWindows to the text fields with a dropdown icon
    private void setupPopupFields() {
        // Text fields with a dropdown icon
        final EditText[] popupFields = new EditText[] {
                mBind.layoutWidthEdit,
                mBind.layoutHeightEdit,
                mBind.adjustViewBoundsEdit
        };
        // Data for each of the ListPopupWindows
        final String[][] popupData = new String[][] {
                Data.ARR_DIMEN_KEYWORDS,
                Data.ARR_DIMEN_KEYWORDS,
                Data.ARR_BOOL
        };
        // Create and setup ListPopupWindows
        mPopupWindows = new ListPopupWindow[popupFields.length];
        for (int i = 0; i < popupFields.length; i++) {
            mPopupWindows[i] = new ListPopupWindow(this);
            mPopupWindows[i].setAnchorView(popupFields[i]);
            mPopupWindows[i].setModal(true);
            mPopupWindows[i].setVerticalOffset(-10);
            mPopupWindows[i].setAdapter(new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    popupData[i]
            ));
            final int I = i;
            // What to do when an item in the ListPopupWindow is clicked
            mPopupWindows[i].setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    popupFields[I].setText(popupData[I][position]);
                    mPopupWindows[I].dismiss();
                }
            });
            // What to do when the dropdown icon is clicked (--> show ListPopupWindow)
            popupFields[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        int textFieldWidth = popupFields[I].getWidth();
                        int iconWidth = popupFields[I].getCompoundDrawables()[2].getBounds().width();
                        if (event.getX() >= textFieldWidth - iconWidth) {
                            popupFields[I].requestFocus();
                            mPopupWindows[I].show();
                        }
                    }
                    return false;
                }
            });
        }
    }

    // Add "strikethrough" functionality to the text fields that can be left empty
    private void setupEmptiableFields() {
        final EditText[] fields = new EditText[] {
                mBind.backgroundEdit,  // Note: setting no background disables elevation
                mBind.maxWidthEdit,
                mBind.maxHeightEdit
        };
        final TextView[] labels = new TextView[] {
                mBind.backgroundLabel,
                mBind.maxWidthLabel,
                mBind.maxHeightLabel
        };
        // If a field is left empty, strike through the corresponding label
        for (int i = 0; i < fields.length; i++) {
            final int I = i;
            // Called on every text change (i.e. every character typed)
            fields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    int mode = labels[I].getPaintFlags();
                    int flag = Paint.STRIKE_THRU_TEXT_FLAG;
                    // If text field is empty, add strikethrough flag
                    if (fields[I].getText().toString().equals(""))
                        labels[I].setPaintFlags(Util.setFlag(mode, flag));
                    // If text field is non-empty, make sure strikethrough flag is removed
                    else
                        labels[I].setPaintFlags(Util.unsetFlag(mode, flag));
                }
            });
        }
    }

    // If "false" is selected, disable the maxWidth and maxHeight fields
    private void setupAdjustViewBoundsField() {
        // The following two settings don't work when defined in XML (possible Android bug?)
        mBind.adjustViewBoundsEdit.setKeyListener(null);        // Make field non-editable
        mBind.adjustViewBoundsEdit.setSelectAllOnFocus(false);  // Disable selectAllOnFocus
        mBind.adjustViewBoundsEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (mBind.adjustViewBoundsEdit.getText().toString().equals(Data.TRUE)) {
                    mBind.maxWidthLabel.setEnabled(true);
                    mBind.maxWidthEdit.setEnabled(true);
                    mBind.maxHeightEdit.setEnabled(true);
                    mBind.maxHeightLabel.setEnabled(true);
                }
                else {
                    mBind.maxWidthLabel.setEnabled(false);
                    mBind.maxWidthEdit.setEnabled(false);
                    mBind.maxHeightEdit.setEnabled(false);
                    mBind.maxHeightLabel.setEnabled(false);
                }
            }
        });
    }


    // Get the SharedPreference entry at a specific index in the arrays initialised by initMapping
    private String getPref(int index) {
        return Pref.get(this, mPrefKeys[index]);
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
            mTextFields[i].setText(Pref.getDefault(this, mPrefKeys[i]));
        }
    }

    // TODO: validate values on save
    // Save values from text fields to the corresponding SharedPreference entries
    private void saveValues() {
        for (int i = 0; i < mTextFields.length; i++) {
            Pref.put(this, mPrefKeys[i], mTextFields[i].getText().toString());
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
