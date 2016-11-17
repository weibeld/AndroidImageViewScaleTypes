package org.weibeld.example.imageviewscaletypes;

import java.util.regex.Pattern;

/**
 * Created by dw on 13/11/16.
 */

public abstract class Validator {

    protected Pattern mPat;

    private Validator() {}

    public boolean test(String s) {
        return mPat.matcher(s).matches();
    }

    public static class DimenValidator extends Validator {
        public DimenValidator() {
            mPat = Pattern.compile(Data.getRegexEmpty() + "|" + Data.getRegexDimen());
        }
    }

    public static class LayoutDimenValidator extends Validator {
        public LayoutDimenValidator() {
            mPat = Pattern.compile(Data.getRegexDimen() + "|" + Data.getRegexDimenKeywords());
        }
    }

    public static class ColorValidator extends Validator {
        public ColorValidator() {
            mPat = Pattern.compile(Data.getRegexEmpty() + "|" + Data.getRegexRgb() + "|" + Data.getRegexColors());
        }
    }
}
