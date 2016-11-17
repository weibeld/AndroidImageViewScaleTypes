package org.weibeld.example.imageviewscaletypes;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Filter for providing customised auto completion suggestions for an input string.</p>
 *
 * <p>The concrete auto completion suggestion strategies are implemented in the subclasses
 * {@link DimenAutoComplFilter} and {@link LayoutDimenAutoComplFilter}.</p>
 *
 * <p>This filter is initialised with an {@link ArrayAdapter<String>} and it dynamically loads the
 * found suggestions into this adapter, replacing the previous data content of the adapter (see
 * {@link #publishResults(CharSequence, FilterResults)}). If no suggestions are found for a given
 * input string, then the ArrayAdapter is left empty.</p>
 *
 * <p>The adapter used by this filter should be the same {@link AutoComplAdapter} that is also used
 * for an {@link android.widget.AutoCompleteTextView}. Then, on each keystroke in the text field,
 * this filter can be obtained via {@link AutoComplAdapter#getFilter()}, and
 * {@link Filter#filter(CharSequence)} can be called. This triggers the filtering process, which
 * finally updates the data of the {@link android.widget.AutoCompleteTextView}'s adapter, as
 * described above.</p>
 */
public abstract class AutoComplFilter extends Filter {
    protected final String LOG_TAG = AutoComplFilter.class.getSimpleName();
    ArrayAdapter<String> mAdapter;

    public AutoComplFilter(ArrayAdapter<String> adapter) {
        mAdapter = adapter;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        // Param "results" are the FilterResults returned by performFiltering (must not be null)
        mAdapter.clear();
        List suggestions = (List) results.values;
        Collections.sort(suggestions);
        mAdapter.addAll(suggestions);
        mAdapter.notifyDataSetChanged();
    }

    protected FilterResults initEmptyFilterResults() {
        FilterResults results = new FilterResults();
        results.values = new ArrayList<String>();
        results.count = 0;
        return results;
    }


    /**
     * Concrete implementation of {@link AutoComplFilter} providing auto completion suggestions
     * of the form <value><unit> (e.g. "14.5dp") given that the input string forms at least a valid
     * number (e.g. "14.5"), and can be expanded to one of the accepted <value><unit> patterns.
     *
     * The values (numbers) must be floating point numbers >= 0. The accepted units are defined in
     * {@link Data#ARR_DIMEN_UNITS}.
     *
     * For example, if the input string is "5", then the returned auto completion suggestion list
     * is { "5dp", "5in", "5mm", "5px", "5sp" }. On the other hand, if the input string is "5d",
     * then the returned suggestion list is only { "5dp" }.
     */
    public static class DimenAutoComplFilter extends AutoComplFilter {
        private Pattern mPatNum;
        private Map<Pattern, String> mMapPat;

        public DimenAutoComplFilter(ArrayAdapter<String> adapter) {
            super(adapter);
            mPatNum = Pattern.compile(Data.getRegexNum());
            // Create <value><unit> pattern for each unit and associate with "replace" unit string
            mMapPat = new HashMap<>();
            for (String s : Data.ARR_DIMEN_UNITS) {
                mMapPat.put(Pattern.compile(Data.getRegexNum() + s), s);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = initEmptyFilterResults();
            if (constraint == null) return results;
            results.values = filterDimen(constraint.toString());
            results.count = ((List) results.values).size();
            return results;
        }

        protected ArrayList<String> filterDimen(String input) {
            ArrayList<String> suggestions = new ArrayList<>();
            Matcher numMatcher = mPatNum.matcher(input);
            // Don't do anything if the input does not start at least with a valid number
            if (numMatcher.find()) {
                for (Map.Entry<Pattern, String> e : mMapPat.entrySet()) {
                    // A matcher is stateful, hitEnd() must come after matches()
                    Matcher matcher = e.getKey().matcher(input);
                    if (matcher.matches())
                        suggestions.add(input);
                    else if (matcher.hitEnd())
                        // If input matches partially (e.g. "14d"), extract number and append unit
                        suggestions.add(numMatcher.group() + e.getValue());
                }
            }
            return suggestions;
        }
    }


    /**
     * Extension of {@link DimenAutoComplFilter}.
     *
     * In addition to auto completion suggestions for input strings that can be expanded to a
     * dimension in <value><unit> form, this filter also provides the suggestions
     * "{@value Data#WRAP_CONTENT}" and "{@value Data#MATCH_PARENT}" for input strings that can
     * be expanded to {@link Data#WRAP_CONTENT} and {@link Data#MATCH_PARENT}.
     */
    public static class LayoutDimenAutoComplFilter extends DimenAutoComplFilter {
        private Map<Pattern, String> mMapPat;

        public LayoutDimenAutoComplFilter(ArrayAdapter<String> adapter) {
            super(adapter);
            mMapPat = new HashMap<>();
            for (String s : Data.ARR_DIMEN_KEYWORDS) {
                mMapPat.put(Pattern.compile(s), s);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.v(LOG_TAG, "performFiltering");
            FilterResults results = initEmptyFilterResults();
            if (constraint == null) return results;
            // Test if input can be expanded to "wrap_content" or "match_parent"
            ArrayList<String> suggestions = new ArrayList<>();
            String input = constraint.toString();
            for (Map.Entry<Pattern, String> e : mMapPat.entrySet()) {
                Matcher matcher = e.getKey().matcher(input);
                // A matcher is stateful, hitEnd() must come after matches()
                if (matcher.matches() || matcher.hitEnd())
                    suggestions.add(e.getValue());
            }
            // Keywords are mutually exclusive with <val><unit> dimensions (i.e. no input can match
            // both), so we need to check for <val><unit> only if there was no match for a keyword
            if (suggestions.size() == 0) suggestions = filterDimen(input);
            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }
    }
}
