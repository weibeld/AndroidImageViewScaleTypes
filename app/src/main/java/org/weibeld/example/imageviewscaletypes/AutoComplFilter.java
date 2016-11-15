package org.weibeld.example.imageviewscaletypes;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

/**
 * Created by dw on 15/11/16.
 */

public abstract class AutoComplFilter extends Filter {

    private final String LOG_TAG = AutoComplFilter.class.getSimpleName();

    ArrayAdapter<String> mAdapter;

    public AutoComplFilter(ArrayAdapter<String> adapter) {
        mAdapter = adapter;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if (results.values == null) Log.v(LOG_TAG, "FilterResults.values is null");
        else Log.v(LOG_TAG, "FilterResults.values size: " + ((String[]) (results.values)).length);
        mAdapter.clear();
        if (results.values != null)
            mAdapter.addAll((String[]) results.values);
        mAdapter.notifyDataSetChanged();
    }


    public static class LayoutDimenAutoComplFilter extends AutoComplFilter {

        public LayoutDimenAutoComplFilter(ArrayAdapter<String> adapter) {
            super(adapter);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String[] suggest = new String[] {};
            FilterResults results = new FilterResults();
            results.count = suggest.length;
            results.values = suggest;
            return results;
        }
    }

    public static class DimenAutoComplFilter extends AutoComplFilter {

        private final String LOG_TAG = DimenAutoComplFilter.class.getSimpleName();

        private String mNum, mUnitD, mUnitS, mUnitP, mUnitI, mUnitM, mUnitDP, mUnitSP, mUnitPX, mUnitIN, mUnitMM;

        public DimenAutoComplFilter(ArrayAdapter<String> adapter) {
            super(adapter);
            mNum = "\\-?((\\d+)|(\\d+\\.)|(\\.\\d+)|(\\d+\\.\\d+))";
            mUnitD = mNum + "d";
            mUnitS = mNum + "s";
            mUnitP = mNum + "p";
            mUnitI = mNum + "i";
            mUnitM = mNum + "m";
            mUnitDP = mUnitD + "p";
            mUnitSP = mUnitS + "p";
            mUnitPX = mUnitP + "x";
            mUnitIN = mUnitI + "n";
            mUnitMM = mUnitM + "m";
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String input = constraint.toString();
            String[] suggest;

            if (input.matches(mNum))
                suggest = new String[] { input + "dp", input + "sp", input + "px", input + "in", input + "mm" };
            else if (input.matches(mUnitD))
                suggest = new String[] { input + "p" };
            else if (input.matches(mUnitS))
                suggest = new String[] { input + "p" };
            else if (input.matches(mUnitP))
                suggest = new String[] { input + "x" };
            else if (input.matches(mUnitI))
                suggest = new String[] { input + "n" };
            else if (input.matches(mUnitM))
                suggest = new String[] { input + "m" };
            else if (input.matches(mUnitDP))
                suggest = new String[] { input };
            else if (input.matches(mUnitSP))
                suggest = new String[] { input };
            else if (input.matches(mUnitPX))
                suggest = new String[] { input };
            else if (input.matches(mUnitIN))
                suggest = new String[] { input };
            else if (input.matches(mUnitMM))
                suggest = new String[] { input };
            else
                suggest = new String[] {};

            FilterResults results = new FilterResults();
            results.count = suggest.length;
            results.values = suggest;
            Log.v(LOG_TAG, "performFiltering, results.values: " + results.values);
            return results;
        }
    }

    public static class ColorAutoComplFilter extends AutoComplFilter {

        public ColorAutoComplFilter(ArrayAdapter<String> adapter) {
            super(adapter);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String[] suggest = new String[] {};
            FilterResults results = new FilterResults();
            results.count = suggest.length;
            results.values = suggest;
            return results;
        }
    }
}
