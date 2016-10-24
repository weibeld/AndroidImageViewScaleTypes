package org.weibeld.example.imageviewscaletypesexample;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by dw on 24/10/16.
 */

public class ScaleTypeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment, container, false);

        // Get the position of this fragment in the ViewPager
        int position = getArguments().getInt(Data.ARG_POSITION);

        TextView textView = (TextView) rootView.findViewById(R.id.textview);
        textView.setText("I am " + Data.SCALE_TYPES[position].name());

        return rootView;
    }
}
