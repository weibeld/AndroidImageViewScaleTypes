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

import static android.R.id.input;

/**
 * Created by dw on 07/11/16.
 */

public class EditImageViewActivity extends AppCompatActivity {

    private final String LOG_TAG = EditImageViewActivity.class.getSimpleName();

    private final int LAYOUT_DROPDOWN_ITEM = R.layout.item_autocomplete;

    // Binding to the named XML layout UI elements (Data Binding Library)
    ActivityEditImageViewBinding mBind;

    private Map<EditText, Validator> mMapValidators;
    private Map<EditText, Integer> mMapPrefKeys;

    // ColorStateList (used for enabled and disabled states) for valid and invalid text in EditText
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

        // Back up the default colours of EditText
        mInputTextColorNormal = mBind.layoutWidthEdit.getTextColors();
        mInputTextColorInvalid = getResources().getColorStateList(R.color.invalid_text_color);
        //logTextColors();

        initMappings();
        setupAutoComplete();
        setupValidations();
        setupEmptiableFields(new EditText[] {mBind.backgroundEdit, mBind.maxWidthEdit, mBind.maxHeightEdit});
        setupAdjustViewBoundsField();

        // Load text into fields (call after above setup methods to trigger text change listeners)
        loadValues();
    }

    // Create mapping between text fields and SharedPreferences entries
    private void initMappings() {
        mMapPrefKeys = new HashMap<>();
        mMapPrefKeys.put(mBind.layoutWidthEdit, R.string.pref_layout_width_key);
        mMapPrefKeys.put(mBind.layoutHeightEdit, R.string.pref_layout_height_key);
        mMapPrefKeys.put(mBind.backgroundEdit, R.string.pref_background_key);
        mMapPrefKeys.put(mBind.adjustViewBoundsEdit, R.string.pref_adjustViewBounds_key);
        mMapPrefKeys.put(mBind.maxWidthEdit, R.string.pref_maxWidth_key);
        mMapPrefKeys.put(mBind.maxHeightEdit, R.string.pref_maxHeight_key);

        mMapValidators = new HashMap<>();
        mMapValidators.put(mBind.layoutWidthEdit, new Validator.LayoutDimenValidator());
        mMapValidators.put(mBind.layoutHeightEdit, new Validator.LayoutDimenValidator());
        mMapValidators.put(mBind.backgroundEdit, new Validator.ColorValidator());
        mMapValidators.put(mBind.maxWidthEdit, new Validator.DimenValidator());
        mMapValidators.put(mBind.maxHeightEdit, new Validator.DimenValidator());
    }

    private void setupValidations() {
        for (Map.Entry<EditText, Validator> entry : mMapValidators.entrySet()) {
            EditText e = entry.getKey();
            e.addTextChangedListener(new MyTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    // Remove error indication if one is still set (may occur if reset is clicked)
                    e.setError(null);
                    String input = e.getText().toString();
                    if (entry.getValue().test(input))
                        e.setTextColor(mInputTextColorNormal);
                    else
                        e.setTextColor(mInputTextColorInvalid);
                        //textField.setError("Invalid input");
                }
            });
            e.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) return;
                if (!entry.getValue().test(e.getText().toString()))
                    e.setError("Invalid input");
                    //showInvalidInputDialog(e);
            });
        }
    }

    private void setupAutoComplete() {
        // Map between AutoCompleteTextViews and their Adapters
        Map<AutoCompleteTextView, ArrayAdapter<String>> mapAdapt = new HashMap<>();
        mapAdapt.put(mBind.layoutWidthEdit, new LayoutDimenAutoComplAdapter(this, LAYOUT_DROPDOWN_ITEM));
        mapAdapt.put(mBind.layoutHeightEdit, new LayoutDimenAutoComplAdapter(this, LAYOUT_DROPDOWN_ITEM));
        mapAdapt.put(mBind.backgroundEdit, new ArrayAdapter<>(this, LAYOUT_DROPDOWN_ITEM, Data.ARR_COLORS));
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

    // Add "strikethrough" functionality to the text fields that can be left empty
    // Note: leaving background empty disables elevation
    private void setupEmptiableFields(EditText[] editTexts) {
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

    private void setupAdjustViewBoundsField() {
        // The following two settings don't work when defined in XML (possible Android bug?)
        mBind.adjustViewBoundsEdit.setKeyListener(null);        // Make field non-editable
        mBind.adjustViewBoundsEdit.setSelectAllOnFocus(false);  // Disable selectAllOnFocus
        // If "true", enabled maxWidth and maxHeight fields, and if "false" disable them
        mBind.adjustViewBoundsEdit.addTextChangedListener(new MyTextWatcher() {
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
        for (Map.Entry<EditText, Integer> e : mMapPrefKeys.entrySet()) {
            e.getKey().setText(Pref.get(this, e.getValue()));
        }
    }

    // Load default values of SharedPreferences into the corresponding text fields
    private void loadDefaultValues() {
        for (Map.Entry<EditText, Integer> e : mMapPrefKeys.entrySet()) {
            e.getKey().setText(Pref.getDefault(this, e.getValue()));
        }
    }

    // TODO: validate values on save
    // Save values from text fields to the corresponding SharedPreference entries
    private void saveValues() {
        for (Map.Entry<EditText, Integer> entry : mMapPrefKeys.entrySet()) {
            Pref.put(this, entry.getValue(), entry.getKey().getText().toString());
        }
    }

    private void validateValues() {
        for (Map.Entry<EditText, Validator> e : mMapValidators.entrySet()) {
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
        for (Map.Entry<EditText, Integer> e : mMapPrefKeys.entrySet()) {
            if (!e.getKey().getText().toString().equals(Pref.get(this, e.getValue())))
                return true;
        }
        return false;
    }

    // Ask user to confirm exit of current activity (either after clicking the X in the toolbar or
    // pressing the device back button) if any edits have been made so far in the text fields
    private void confirmExit() {
        if (hasChanges()) {
            new AlertDialog.Builder(this).
                    setMessage(R.string.exit_dialog_msg).
                    setNegativeButton(R.string.exit_dialog_neg, (dialog, which) -> {}).
                    setPositiveButton(R.string.exit_dialog_pos, (dialog, which) -> finish()).
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
        //validateValues();
        saveValues();
        showToast(R.string.toast_changes_applied);
        finish();
    }

    // Called when the "Reset Defaults" button is clicked
    public void onResetClicked(View view) {
        // TODO: scroll to top
        loadDefaultValues();
    }

    private void showToast(int msgResId) {
        Toast.makeText(this, msgResId, Toast.LENGTH_SHORT).show();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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

}
