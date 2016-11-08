package org.weibeld.example.imageviewscaletypes;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by dw on 07/11/16.
 */

public class EditImageViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_image_view, container, false);

//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.edit_image_view_toolbar);
//        toolbar.setTitle(R.string.action_edit_image_view);
//
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//
//        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);  // Enable up (<-) arrow in action bar
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
//        }

        setHasOptionsMenu(true);
        return rootView;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        getActivity().getMenuInflater().inflate(R.menu.edit_image_view, menu);
//    }
}
