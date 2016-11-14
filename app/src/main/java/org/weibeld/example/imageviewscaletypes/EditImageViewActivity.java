package org.weibeld.example.imageviewscaletypes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.weibeld.example.imageviewscaletypes.databinding.ActivityEditImageViewBinding;

import java.util.HashMap;
import java.util.Map;

import static android.R.id.input;

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

    private Map<EditText, StringPredicate> mMapValidators;
    private Map<EditText, Integer> mMapPrefKeys;

    // The default text colours of an EditText in different states (e.g. enabled, disabled)
    private ColorStateList mDefaultColorsEditText;

    // Functional interface to be used for lambda expressions. This is only necessary, because
    // currently the Java standard functional interfaces in java.util.function cannot be used for
    // devices with API level < 24 (neither with Jack, nor with Retrolambda). If we would target
    // API level >= 24, we could just use the predefined Predicate<String> interface.
    // http://stackoverflow.com/questions/38607149/is-there-a-way-to-use-java-8-functional-interfaces-on-android-api-below-24
    interface StringPredicate {
        boolean test(String s);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_edit_image_view);

        // Set up Toolbar as app bar and define what to do when the X icon is clicked
        // Possible Android bug: setNavigationOnClickListener must come AFTER setSupportActionBar
        setSupportActionBar(mBind.toolbar);
        mBind.toolbar.setNavigationOnClickListener(v -> confirmExit());

        // Back up the default colours of EditText
        mDefaultColorsEditText = mBind.layoutWidthEdit.getTextColors();
        logTextColors();

        initMapping();
        setupValidations();
        setupPopupFields();
        setupEmptiableFields();
        setupAdjustViewBoundsField();
        // TODO: add colour picker

        // Load text into fields (call after above setup methods to trigger text change listeners)
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

        mMapPrefKeys = new HashMap<>();
        mMapPrefKeys.put(mBind.layoutWidthEdit, R.string.pref_layout_width_key);
        mMapPrefKeys.put(mBind.layoutHeightEdit, R.string.pref_layout_height_key);
        mMapPrefKeys.put(mBind.backgroundEdit, R.string.pref_background_key);
        mMapPrefKeys.put(mBind.adjustViewBoundsEdit, R.string.pref_adjustViewBounds_key);
        mMapPrefKeys.put(mBind.maxWidthEdit, R.string.pref_maxWidth_key);
        mMapPrefKeys.put(mBind.maxHeightEdit, R.string.pref_maxHeight_key);

        mMapValidators = new HashMap<>();
        mMapValidators.put(mBind.layoutWidthEdit, Validator::isValidLayoutWidthHeightEntry);
        mMapValidators.put(mBind.layoutHeightEdit, Validator::isValidLayoutWidthHeightEntry);
        mMapValidators.put(mBind.backgroundEdit, Validator::isValidColorEntry);
        mMapValidators.put(mBind.adjustViewBoundsEdit, Validator::isValidBooleanEntry);
        mMapValidators.put(mBind.maxWidthEdit, Validator::isValidDimenEntry);
        mMapValidators.put(mBind.maxHeightEdit, Validator::isValidDimenEntry);
    }

    // Add OnFocusChangeListeners to text fields so that whenever a field loses focus, the content
    // of the field is validated, and the focus remains on this field.
    private void setupValidations() {
        for (Map.Entry<EditText, StringPredicate> e : mMapValidators.entrySet()) {
            EditText textField = e.getKey();
            textField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    String input = textField.getText().toString();
                    if (mMapValidators.get(textField).test(input))
                        textField.setTextColor(mDefaultColorsEditText);
                    else
                        textField.setTextColor(getColor(R.color.colorInvalidInput));
                        //textField.setError("Invalid input");
                }
            });
            textField.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) return;
                if (!mMapValidators.get(textField).test(textField.getText().toString()))
                    showInvalidInputDialog(textField);
            });
        }
    }

    private void showInvalidInputDialog(EditText textField) {
        new AlertDialog.Builder(this).
                setTitle("Invalid Input").
                setMessage("\"" + input + "\" is not a valid value for ...\nValid inputs are: (<num>dp|sp|px|in|mm)|match_parent|wrap_content").
                setPositiveButton("OK", (dialog, which) -> {
                    textField.requestFocus();
                    final Rect scrollBounds = new Rect();
                    mBind.scrollView.getHitRect(scrollBounds);
                    if (!textField.getLocalVisibleRect(scrollBounds)) {
                        mBind.scrollView.post(() -> mBind.scrollView.smoothScrollTo(0, textField.getTop()));
                    }
                }).
                create().show();
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
                    mBind.maxHeightLabel.setEnabled(true);
                    mBind.maxHeightEdit.setEnabled(true);
                }
                else {
                    mBind.maxWidthLabel.setEnabled(false);
                    mBind.maxWidthEdit.setEnabled(false);
                    mBind.maxHeightLabel.setEnabled(false);
                    mBind.maxHeightEdit.setEnabled(false);
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

    private void validateValues() {
        for (Map.Entry<EditText, StringPredicate> e : mMapValidators.entrySet()) {
            String text = e.getKey().getText().toString();
            if (!e.getValue().test(text)) {
                Log.v(LOG_TAG, text + " is invalid");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Invalid entry: " + text).
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        }).create().show();
                break;
            }
            else
                Log.v(LOG_TAG, text + " is valid");
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
        //validateValues();
        saveValues();
        Toast.makeText(this, R.string.toast_changes_applied, Toast.LENGTH_SHORT).show();
        finish();
    }

    // Called when the "Reset Defaults" button is clicked
    public void onResetClicked(View view) {
        loadDefaultValues();
    }

    // This method is for debugging only. Find out the default text colours for the "enabled" and
    // "disabled" state of EditText and TextView.
    private void logTextColors() {
        // Get ColorStateLists (defines text colors for all possible states that the View can be in)
        ColorStateList cslEditText = mBind.maxWidthEdit.getTextColors();
        ColorStateList cslTextView = mBind.maxWidthLabel.getTextColors();

        // The standard Android enabled and disabled states (used in the ColorStateLists)
        int enabledState = android.R.attr.state_enabled;
        int disabledState = -android.R.attr.state_enabled;

        // Get the text colours for the enabled and disabled states
        int defaultColor = 0xFFFFFFFF;
        int editTextEnabled = cslEditText.getColorForState(new int[] {enabledState}, defaultColor);
        int editTextDisabled = cslEditText.getColorForState(new int[] {disabledState}, defaultColor);
        int textViewEnabled = cslTextView.getColorForState(new int[] {enabledState}, defaultColor);
        int textViewDisabled = cslTextView.getColorForState(new int[] {disabledState}, defaultColor);

        // Output
        Log.v(LOG_TAG, "Text colour EditText enabled: " + Integer.toHexString(editTextEnabled));
        Log.v(LOG_TAG, "Text colour EditText disabled: " + Integer.toHexString(editTextDisabled));
        Log.v(LOG_TAG, "Text colour TextView enabled: " + Integer.toHexString(textViewEnabled));
        Log.v(LOG_TAG, "Text colour TextView disabled: " + Integer.toHexString(textViewDisabled));

        // Example results on API level 23 with dark text on light background theme
        // (Compare with https://material.google.com/style/color.html#color-color-schemes):
        // EditText enabled: 0xDE000000: black, opacity 222/255 (87.06%) -> primary text
        // EditText disabled: 0x3A000000: black, opacity 58/255 (22.75%) -> 26.1% of primary text
        // TextView enabled: 0x8A000000: black, opacity 138/255 (54.12%) -> secondary text
        // TextView disabled: 0x24000000: black, opacity 36/255 (14.12%) -> 26.1% of primary text
    }

}
