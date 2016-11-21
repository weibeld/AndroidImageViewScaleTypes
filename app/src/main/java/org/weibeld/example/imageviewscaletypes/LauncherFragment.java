package org.weibeld.example.imageviewscaletypes;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Placeholder fragment in the ViewPager that launches the real PageFragment.
 *
 * The reason that a launcher Fragment is used is the Fragment lifetime when another Activity is
 * started, which is onPause -> onStop -> onStart -> onResume. Thus, when returning from the
 * EditImageViewActivity, there is no call to onCreateView of the active three Fragments. However,
 * a call to onCreateView is need to correctly apply all the new settings to the ImageView.
 *
 * With a launcher Fragment, upon returning from EditImageViewActivity, onStart of the launcher
 * Fragment is called, which triggers the creation from scratch (starting from onAttach) of the
 * real PageFragment. Thus, all the new settings for the ImageView are correctly applied.
 */
// TODO: after the replace in oncCreateView, both Fragments exist simultaneously. Why does the launcher fragment not cease to exist?
// TODO: if a page is to be recreated (by swiping back or returning from EditImageViewActivity), the PageFragment is created simultaneously with the launcher fragment until onStart, then destroyed and detached, and finally a new PageFragment is created starting from onAttach. Why is the old PageFragment started to be created?
public class LauncherFragment extends Fragment {

    private final String LOG_TAG = LauncherFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView of launcher  for " +  Data.SCALE_TYPES[getArguments().getInt(Data.ARG_POSITION)]);
        return inflater.inflate(R.layout.fragment_launcher, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "onStart of launcher for " +  Data.SCALE_TYPES[getArguments().getInt(Data.ARG_POSITION)]);

        Fragment frag = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(Data.ARG_POSITION, getArguments().getInt(Data.ARG_POSITION));
        frag.setArguments(args);

        getChildFragmentManager().beginTransaction().replace(R.id.frag_container, frag).commit();
    }


    /*--------------------------------------------------------------------------------------------*
     * Start debugging area
     *--------------------------------------------------------------------------------------------*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(LOG_TAG, "onAttach of launcher for " +  Data.SCALE_TYPES[getArguments().getInt(Data.ARG_POSITION)]);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate of launcher for " +  Data.SCALE_TYPES[getArguments().getInt(Data.ARG_POSITION)]);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(LOG_TAG, "onActivityCreated of launcher  for " +  Data.SCALE_TYPES[getArguments().getInt(Data.ARG_POSITION)]);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "onResume of launcher for " +  Data.SCALE_TYPES[getArguments().getInt(Data.ARG_POSITION)]);
    }

    @Override
    public void onPause() {
        Log.v(LOG_TAG, "onPause of launcher for " +  Data.SCALE_TYPES[getArguments().getInt(Data.ARG_POSITION)]);
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(LOG_TAG, "onStop of launcher for " +  Data.SCALE_TYPES[getArguments().getInt(Data.ARG_POSITION)]);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(LOG_TAG, "onDestroyView of launcher for " +  Data.SCALE_TYPES[getArguments().getInt(Data.ARG_POSITION)]);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy of launcher for " +  Data.SCALE_TYPES[getArguments().getInt(Data.ARG_POSITION)]);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(LOG_TAG, "onDetach of launcher for " +  Data.SCALE_TYPES[getArguments().getInt(Data.ARG_POSITION)]);
    }
}
