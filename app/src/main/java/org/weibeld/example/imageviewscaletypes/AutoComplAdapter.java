package org.weibeld.example.imageviewscaletypes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import org.weibeld.example.imageviewscaletypes.AutoComplFilter.*;

/**
 * <p>Custom adapter for an {@link android.widget.AutoCompleteTextView} that uses a custom auto
 * completion strategy.</p>
 *
 * <p>Subclasses of this class maintain a {@link AutoComplFilter}, which can be obtained with
 * {@link #getFilter()}. This filter implements the custom auto completion strategy.</p>
 *
 * <p>If the filter of this adapter is obtained, then {@link Filter#filter(CharSequence)} can be
 * called on this filter. This triggers the filtering process, which finally dynamically updates the
 * underlying data of this adapter. This procedure should ideally be done on every keystroke that
 * the user makes in the {@link android.widget.AutoCompleteTextView}.</p>
 */
public abstract class AutoComplAdapter extends ArrayAdapter<String> {

    AutoComplFilter mFilter = null;

    public AutoComplAdapter(Context context, int resource) {
        super(context, resource);
    }


    /**
     * <p>Concrete implementation of an {@link AutoComplAdapter} which uses a
     * {@link DimenAutoComplFilter}.</p>
     */
    public static class DimenAutoComplAdapter extends AutoComplAdapter {
        public DimenAutoComplAdapter(Context context, int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public Filter getFilter() {
            if (mFilter == null)
                mFilter = new DimenAutoComplFilter(this);
            return mFilter;
        }
    }


    /**
     * <p>Concrete implementation of an {@link AutoComplAdapter} which uses a
     * {@link LayoutDimenAutoComplFilter}.</p>
     */
    public static class LayoutDimenAutoComplAdapter extends AutoComplAdapter {

        public LayoutDimenAutoComplAdapter(Context context, int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public Filter getFilter() {
            if (mFilter == null)
                mFilter = new LayoutDimenAutoComplFilter(this);
            return mFilter;
        }
    }
}
