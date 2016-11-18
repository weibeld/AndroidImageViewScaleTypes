package org.weibeld.example.imageviewscaletypes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;

import java.util.regex.Pattern;

/**
 * Created by dw on 13/11/16.
 */

public abstract class Validator {

    protected Pattern mPat;
    protected String mWarnMsg;
    protected Drawable mWarnIcon;

    private Validator(Context c) {
        // Initialise warning message (must be completed by the concrete subclasses)
        mWarnMsg = c.getString(R.string.warn_invalid_input);
        // Set up warning icon (is identical for all subclasses)
        mWarnIcon = c.getResources().getDrawable(R.drawable.ic_warning_white_24dp);
        mWarnIcon.setBounds(0, 0, mWarnIcon.getIntrinsicWidth(), mWarnIcon.getIntrinsicHeight());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mWarnIcon.setTintList(c.getResources().getColorStateList(R.color.invalid_input_color));
        else
            DrawableCompat.setTintList(mWarnIcon, c.getResources().getColorStateList(R.color.invalid_input_color));
    }

    public boolean test(String s) {
        return mPat.matcher(s).matches();
    }

    public String getWarnMsg() {
        return mWarnMsg;
    }

    public Drawable getmWarnIcon() {
        return mWarnIcon;
    }


    public static class DimenValidator extends Validator {
        public DimenValidator(Context c) {
            super(c);
            mPat = Pattern.compile(Data.getRegexEmpty() + "|" + Data.getRegexDimen());
            mWarnMsg += Data.getWarnMsgLineEmpty() + Data.getWarnMsgLineDimen();
        }
    }

    public static class LayoutDimenValidator extends Validator {
        public LayoutDimenValidator(Context c) {
            super(c);
            mPat = Pattern.compile(Data.getRegexDimen() + "|" + Data.getRegexDimenKeywords());
            mWarnMsg += Data.getWarnMsgLineDimen() + Data.getWarnMsgLineDimenKeywords();
        }
    }

    public static class ColorValidator extends Validator {
        public ColorValidator(Context c) {
            super(c);
            mPat = Pattern.compile(Data.getRegexEmpty() + "|" + Data.getRegexRgb() + "|" + Data.getRegexColors());
            mWarnMsg += Data.getWarnMsgLineEmpty() + Data.getWarnMsgLineRgb() + Data.getWarnMsgLineColors();
        }
    }
}