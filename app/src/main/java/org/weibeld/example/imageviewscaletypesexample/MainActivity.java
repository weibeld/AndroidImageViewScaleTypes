package org.weibeld.example.imageviewscaletypesexample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

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

        // Return the Fragment at the given position to the ViewPager
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

        // Return the total number of pages
        @Override
        public int getCount() {
            return Data.SCALE_TYPES.length;
        }

        // Return the title of the page at the provided position
        @Override
        public CharSequence getPageTitle(int position) {
            return Data.SCALE_TYPES[position].name();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create the options (action) in the app bar as defined in menu/main.xml
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Create and launch an intent for picking an image (includes images on device and on
        // network locatios, e.g. Google Drive). The app opened is the built-in file explorer.
        if (id == R.id.action_choose_image) {
            Intent intent = new Intent();
            // For ACTION_OPEN_DOCUMENT vs. ACTION_GET_CONTENT see:
            // https://developer.android.com/guide/topics/providers/document-provider.html#client
            if (Build.VERSION.SDK_INT >= 19)
                // ACTION_OPEN_DOCUMENT introduced in API level 19 (Android 4.4 KitKat) together
                // with the Storage Access Framework (SAF). Allows to get a URI to a file owned
                // by a Document Provider with a LONG-TERM and PERSISTENT URI permission grant for
                // this file for the ENTIRE APP. The permission grant lasts until the device is
                // turned off. That is, unlike ACTION_GET_CONTENT, all components of our app have
                // access to this file, also across app restarts (until device is restarted).
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            else
                // ACTION_GET_CONTENT allows to get an URI to a file owned by another app with a
                // TEMPORARY permission grant for this file for the current activity only (and its
                // fragments). That means, if the app is restarted, then a fresh activity has no
                // permission to access this file (java.lang.SecurityException: Permission Denial).
                intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, Data.REQUEST_CHOOSE_IMAGE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Called when the activity, which was started by startActivityForResult, returns its result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // The started activity returns the URI of the selected image, along with a URI permission.
        // Save this URI in the SharedPreferences of this activity.
        if (requestCode == Data.REQUEST_CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            // If we used ACTION_OPEN_DOCUMENT: the URI permission lasts until the device restarts.
            // The following persists this permission across device restarts. See:
            // https://developer.android.com/guide/topics/providers/document-provider.html#client
            if (Build.VERSION.SDK_INT >= 19)
                getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // If we used ACTION_GET_CONTENT: the URI permission is valid only for the current
            // activity. If the app is restarted, the new activity has no permission for this URI.
            // The following extends the URI permission to the entire app, and as long as the
            // device is running (not across device restarts). Might not work on all devices.
            else
                grantUriPermission(getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(getString(R.string.pref_image_key), uri.toString());
            editor.commit();
            Log.v(LOG_TAG, "onActivityResult: saved " + uri.toString() + " in SharedPreferences");
        }
    }
}
