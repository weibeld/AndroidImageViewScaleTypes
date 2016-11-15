package org.weibeld.example.imageviewscaletypes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import org.weibeld.example.imageviewscaletypes.AutoComplFilter.*;

/**
 * Created by dw on 15/11/16.
 */

public abstract class AutoComplAdapter extends ArrayAdapter<String> {

    AutoComplFilter mFilter = null;

    public AutoComplAdapter(Context context, int resource) {
        super(context, resource);
    }




    public static class LayoutDimenAutoComplAdapter extends AutoComplAdapter {

        public LayoutDimenAutoComplAdapter(Context context, int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public Filter getFilter() {
            if (mFilter == null) mFilter = new LayoutDimenAutoComplFilter(this);
            return mFilter;
        }
    }



    public static class DimenAutoComplAdapter extends AutoComplAdapter {
        public DimenAutoComplAdapter(Context context, int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public Filter getFilter() {
            if (mFilter == null) mFilter = new DimenAutoComplFilter(this);
            return mFilter;
        }
    }



    public static class ColorAutoComplAdapter extends AutoComplAdapter {
        public ColorAutoComplAdapter(Context context, int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public Filter getFilter() {
            if (mFilter == null) mFilter = new ColorAutoComplFilter(this);
            return mFilter;
        }
    }

}
