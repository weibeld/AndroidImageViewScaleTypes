package org.weibeld.example.imageviewscaletypesexample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the Toolbar as the app bar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        // For the tabs, connect the ViewPager to our custom PagerAdapter
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ScaleTypePagerAdapter(getFragmentManager()));

        // Populate and integrate the TabLayout with the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    /* PagerAdapter for supplying the ViewPager with the tab pages (Fragments) to display. */
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create the option actions defined in menu/main.xml
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_choose_image) {
            // Create and launch an intent for picking an image (includes images saved on device,
            // and images on Google Drive, etc.). The app opened is the built-in file explorer.
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, Data.REQUEST_CHOOSE_IMAGE);
            //startActivityForResult(Intent.createChooser(intent, "Choose image"), Data.REQUEST_CHOOSE_IMAGE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Called by the activity that was started by startActivityForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Data.REQUEST_CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the URI of the image that was selected by the user
            Uri uri = data.getData();
            Toast.makeText(this, uri.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
