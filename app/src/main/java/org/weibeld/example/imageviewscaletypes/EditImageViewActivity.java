package org.weibeld.example.imageviewscaletypes;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.weibeld.example.imageviewscaletypes.AutoComplAdapter.DimenAutoComplAdapter;
import org.weibeld.example.imageviewscaletypes.AutoComplAdapter.LayoutDimenAutoComplAdapter;
import org.weibeld.example.imageviewscaletypes.databinding.ActivityEditImageViewBinding;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dw on 07/11/16.
 */

public class EditImageViewActivity extends AppCompatActivity {

    private final String LOG_TAG = EditImageViewActivity.class.getSimpleName();

    // Binding to the named XML layout UI elements (Data Binding Library)
    private ActivityEditImageViewBinding mBind;

    private Map<EditText, Validator> mValidators;
    private Map<EditText, Integer> mPrefKeys;
    private final int LAYOUT_DROPDOWN_ITEM = R.layout.item_autocomplete;

    // Colours, defined for enabled and disabled state, for valid and invalid input in text fields
    private ColorStateList mInputTextColorNormal;
    private ColorStateList mInputTextColorInvalid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_edit_image_view);

        // Set up Toolbar as app bar and define what to do when the X icon is clicked
        // Possible Android bug: setNavigationOnClickListener must come AFTER setSupportActionBar
        setSupportActionBar(mBind.toolbar);
        mBind.toolbar.setNavigationOnClickListener(v -> confirmExit());

        // Back up the default ColorStateList of EditText
        mInputTextColorNormal = mBind.layoutWidthEdit.getTextColors();
        mInputTextColorInvalid = getResources().getColorStateList(R.color.invalid_input_color);

        initMappings();
        setupAutoComplete();
        setupValidations();
        setupEmptiableFields();
        setupAdjustViewBoundsField();

        // Load text into fields (call after above setup methods to trigger text change listeners)
        loadValues();
    }

    // Create mapping between text fields and SharedPreferences entries
    private void initMappings() {
        mPrefKeys = new HashMap<>();
        mPrefKeys.put(mBind.layoutWidthEdit, R.string.pref_layout_width_key);
        mPrefKeys.put(mBind.layoutHeightEdit, R.string.pref_layout_height_key);
        mPrefKeys.put(mBind.backgroundEdit, R.string.pref_background_key);
        mPrefKeys.put(mBind.adjustViewBoundsEdit, R.string.pref_adjustViewBounds_key);
        mPrefKeys.put(mBind.maxWidthEdit, R.string.pref_maxWidth_key);
        mPrefKeys.put(mBind.maxHeightEdit, R.string.pref_maxHeight_key);

        mValidators = new HashMap<>();
        mValidators.put(mBind.layoutWidthEdit, new Validator.LayoutDimenValidator(this));
        mValidators.put(mBind.layoutHeightEdit, new Validator.LayoutDimenValidator(this));
        mValidators.put(mBind.backgroundEdit, new Validator.ColorValidator(this));
        mValidators.put(mBind.maxWidthEdit, new Validator.DimenValidator(this));
        mValidators.put(mBind.maxHeightEdit, new Validator.DimenValidator(this));
    }

    private void setupValidations() {
        for (Map.Entry<EditText, Validator> e : mValidators.entrySet()) {
            e.getKey().addTextChangedListener(new MyTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    // Remove error indication if one is still set (may occur if reset is clicked)
                    e.getKey().setError(null);
                    if (e.getValue().test(e.getKey().getText().toString()))
                        e.getKey().setTextColor(mInputTextColorNormal);
                    else
                        e.getKey().setTextColor(mInputTextColorInvalid);
                }
            });
            e.getKey().setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) return;
                if (!e.getValue().test(e.getKey().getText().toString()))
                    e.getKey().setError(e.getValue().getWarnMsg(), e.getValue().getWarnIcon());
            });
        }
    }

    private void setupAutoComplete() {
        // Map between AutoCompleteTextViews and their Adapters
        Map<AutoCompleteTextView, ArrayAdapter<String>> mapAdapt = new HashMap<>();
        mapAdapt.put(mBind.layoutWidthEdit, new LayoutDimenAutoComplAdapter(this, LAYOUT_DROPDOWN_ITEM));
        mapAdapt.put(mBind.layoutHeightEdit, new LayoutDimenAutoComplAdapter(this, LAYOUT_DROPDOWN_ITEM));
        mapAdapt.put(mBind.backgroundEdit, new ArrayAdapter<>(this, LAYOUT_DROPDOWN_ITEM, Data.getArrColors()));
        mapAdapt.put(mBind.maxWidthEdit, new DimenAutoComplAdapter(this, LAYOUT_DROPDOWN_ITEM));
        mapAdapt.put(mBind.maxHeightEdit, new DimenAutoComplAdapter(this, LAYOUT_DROPDOWN_ITEM));

        for (Map.Entry<AutoCompleteTextView, ArrayAdapter<String>> e : mapAdapt.entrySet()) {
            e.getKey().setAdapter(e.getValue());
            if (e.getKey() == mBind.backgroundEdit) continue;
            // Set up custom auto completion for the text fields that use a custom auto completion
            e.getKey().addTextChangedListener(new MyTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    // Trigger filtering process on 's', which updates the data of this adapter
                    e.getValue().getFilter().filter(s);
                }
            });
        }
    }

    // Add "strikethrough" functionality to the text fields that can be left empty
    // Note: leaving background empty disables elevation
    private void setupEmptiableFields() {
        Map<AutoCompleteTextView, TextView> mapLabels = new HashMap<>();
        mapLabels.put(mBind.backgroundEdit, mBind.backgroundLabel);
        mapLabels.put(mBind.maxWidthEdit, mBind.maxWidthLabel);
        mapLabels.put(mBind.maxHeightEdit, mBind.maxHeightLabel);

        for (Map.Entry<AutoCompleteTextView, TextView> e : mapLabels.entrySet()) {
            e.getKey().addTextChangedListener(new MyTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    int mode = e.getValue().getPaintFlags();
                    if (e.getKey().getText().toString().equals(""))
                        mode = Util.setFlag(mode, Paint.STRIKE_THRU_TEXT_FLAG);
                    else
                        mode = Util.unsetFlag(mode, Paint.STRIKE_THRU_TEXT_FLAG);
                    e.getValue().setPaintFlags(mode);
                }
            });
        }
    }

    // TODO: encapsulate adjustViewBounds field in its own subclasss of EditText
    private void setupAdjustViewBoundsField() {
        // The following two settings don't work when defined in XML (possible Android bug?)
        mBind.adjustViewBoundsEdit.setKeyListener(null);        // Make field non-editable
        mBind.adjustViewBoundsEdit.setSelectAllOnFocus(false);  // Disable selectAllOnFocus
        // If "true", enabled maxWidth and maxHeight fields, and if "false" disable them
        mBind.adjustViewBoundsEdit.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (mBind.adjustViewBoundsEdit.getText().toString().equals(Data.TRUE)) {
                    // Enable labels
                    mBind.maxWidthLabel.setEnabled(true);
                    mBind.maxHeightLabel.setEnabled(true);
                    // Enable text fields
                    mBind.maxWidthEdit.setEnabled(true);
                    mBind.maxHeightEdit.setEnabled(true);
                }
                else {
                    // Disable labels
                    mBind.maxWidthLabel.setEnabled(false);
                    mBind.maxHeightLabel.setEnabled(false);
                    // Disable text fields
                    mBind.maxWidthEdit.setEnabled(false);
                    mBind.maxHeightEdit.setEnabled(false);
                }
            }
        });
        // Create the popup window and define what to do when an item is clicked
        ListPopupWindow popup = new ListPopupWindow(this);
        popup.setAnchorView(mBind.adjustViewBoundsEdit);
        popup.setModal(true);
        popup.setVerticalOffset(-25);
        popup.setAdapter(new ArrayAdapter<>(this, LAYOUT_DROPDOWN_ITEM, Data.ARR_BOOL));
        popup.setOnItemClickListener((parent, view, position, id) -> {
            mBind.adjustViewBoundsEdit.setText(Data.ARR_BOOL[position]);
            popup.dismiss();
        });
        // Define what to do when the area of the dropdown icon is clicked
        mBind.adjustViewBoundsEdit.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mBind.adjustViewBoundsEdit.requestFocus();
                int textFieldWidth = mBind.adjustViewBoundsEdit.getWidth();
                int iconWidth = mBind.adjustViewBoundsEdit.getCompoundDrawables()[2].getBounds().width();
                if (event.getX() >= textFieldWidth - iconWidth) popup.show();
                return true;
            }
            return false;
        });
    }

    // Load values from SharedPreferences into the corresponding text fields
    private void loadValues() {
        for (Map.Entry<EditText, Integer> e : mPrefKeys.entrySet()) {
            // Remove the DEACTIVATED marker if it is set in the value of this field
            String s = Pref.get(this, e.getValue()).replace(Data.DEACTIVATED_MARKER, "");
            e.getKey().setText(s);
            // If the field is validatable, check if restored value is valid and restore error icon
            // if it is not (may occur if the field was deactivated when it was saved)
            if (mValidators.containsKey(e.getKey())) {
                Validator v = mValidators.get(e.getKey());
                if (!v.test(s))
                    e.getKey().setError(v.getWarnMsg(), v.getWarnIcon());
            }
        }
    }

    // Load default values of SharedPreferences into the corresponding text fields
    private void loadDefaultValues() {
        for (Map.Entry<EditText, Integer> e : mPrefKeys.entrySet()) {
            e.getKey().setText(Pref.getDefault(this, e.getValue()));
        }
    }

    // Save values from text fields to SharedPreferences (all values MUST be valid)
    private void saveValues() {
        for (Map.Entry<EditText, Integer> e : mPrefKeys.entrySet()) {
            if (e.getKey().isEnabled())
                Pref.put(this, e.getValue(), e.getKey().getText().toString());
            else
                Pref.put(this, e.getValue(), Data.DEACTIVATED_MARKER + e.getKey().getText().toString());
        }

    }

    private boolean isAllInputValid() {
        for (Map.Entry<EditText, Validator> e : mValidators.entrySet()) {
            if (e.getKey().isEnabled()) {
                if (!e.getValue().test(e.getKey().getText().toString()))
                    return false;
            }
        }
        return true;
    }

    // Check if any changes have been made so far in the text fields w.r.t. the SharedPreferences
    private boolean hasChanges() {
        for (Map.Entry<EditText, Integer> e : mPrefKeys.entrySet()) {
            String field = e.getKey().getText().toString();
            String pref = Pref.get(this, e.getValue()).replace(Data.DEACTIVATED_MARKER, "");
            if (!field.equals(pref))
                return true;
        }
        return false;
    }

    // Ask user to confirm exit of current activity (either after clicking the X in the toolbar or
    // pressing the device back button) if any edits have been made so far in the text fields
    private void confirmExit() {
        if (hasChanges()) {
            new AlertDialog.Builder(this).
                    setMessage(R.string.dialog_discard_msg).
                    setNegativeButton(R.string.dialog_discard_neg, (dialog, which) -> {}).
                    setPositiveButton(R.string.dialog_discard_pos, (dialog, which) -> finish()).
                    create().
                    show();
        }
        else {
            showToast(R.string.toast_no_changes);
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
        if (isAllInputValid()) {
            saveValues();
            showToast(R.string.toast_changes_applied);
            finish();
        }
        else {
            new AlertDialog.Builder(this).
                    setTitle(R.string.dialog_error_title).
                    setMessage(R.string.dialog_error_msg).
                    setPositiveButton(R.string.dialog_error_pos, (dialog, which) -> {}).
                    create().show();
        }
    }

    // Called when the "Reset Defaults" button is clicked
    public void onResetClicked(View view) {
        // TODO: scroll to top
        loadDefaultValues();
    }

    private void showToast(int msgResId) {
        Toast.makeText(this, msgResId, Toast.LENGTH_SHORT).show();
    }




    // Functional interface to be used for lambda expressions. This is only necessary, because
    // currently the Java standard functional interfaces in java.util.function cannot be used for
    // devices with API level < 24 (neither with Jack, nor with Retrolambda). If we would target
    // API level >= 24, we could just use the predefined Predicate<String> interface.
    // http://stackoverflow.com/questions/38607149/is-there-a-way-to-use-java-8-functional-interfaces-on-android-api-below-24
//    interface StringPredicate {
//        boolean test(String s);
//    }

    // Provide default empty implementations of beforeTextChanged and onTextChanged of TextWatcher,
    // because we always only use afterTextChanged
    private abstract class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }


    /*--------------------------------------------------------------------------------------------*
     * Start debugging area
     *--------------------------------------------------------------------------------------------*/

    // This method is for debugging only. Find out the default text colours for the "enabled" and
    // "disabled" state of EditText and TextView. Example: http://www.programcreek.com/
    // java-api-examples/index.php?class=android.content.res.ColorStateList&method=getColorForState
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
        // TextView disabled: 0x24000000: black, opacity 36/255 (14.12%) -> 26.1% of secondary text
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
