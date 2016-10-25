package org.weibeld.example.imageviewscaletypesexample;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by dw on 24/10/16.
 */

public class ScaleTypeFragment extends Fragment {

    private final String LOG_TAG = ScaleTypeFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment, container, false);

        // Get position of this fragment in the ViewPager and derive the corresponding ScaleType
        int position = getArguments().getInt(Data.ARG_POSITION);
        ImageView.ScaleType scaleType = Data.SCALE_TYPES[position];

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageview);
        imageView.setScaleType(scaleType);

        Picasso.with(getActivity()).setIndicatorsEnabled(true);
        Picasso
                .with(getActivity())
                .load(Data.IMAGE_URL)
                //.resize(1850, 2780)
                .into(imageView);

        return rootView;
    }
}
