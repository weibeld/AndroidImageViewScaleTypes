package org.weibeld.example.imageviewscaletypesexample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the Toolbar as the app bar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        // Connect the ViewPager to our custom PagerAdapter
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ScaleTypePagerAdapter(getFragmentManager()));

        // Populate and integrate the TabLayout with the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }


    /* PagerAdapter for supplying the ViewPager with the pages (Fragments) to display. */
    public class ScaleTypePagerAdapter extends FragmentPagerAdapter {

        public ScaleTypePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        /* Return the Fragment at the given position to the ViewPager */
        @Override
        public Fragment getItem(int position) {
            // The Fragment we return is a ScaleTypeFragment that is provided with its position
            // in the collection of pages, so that it knows which scale type it represents.
            Fragment fragment = new ScaleTypeFragment();
            Bundle arg = new Bundle();
            arg.putInt(Data.ARG_POSITION, position);
            fragment.setArguments(arg);
            return fragment;
        }

        /* Return the total number of pages */
        @Override
        public int getCount() {
            return Data.SCALE_TYPES.length;
        }

        /* Return the title of the page at the provided position */
        @Override
        public CharSequence getPageTitle(int position) {
            return Data.SCALE_TYPES[position].name();
        }

    }
}
