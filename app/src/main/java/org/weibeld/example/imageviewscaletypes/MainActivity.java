package org.weibeld.example.imageviewscaletypes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.weibeld.example.imageviewscaletypes.databinding.ActivityMainBinding;
import org.weibeld.util.Util;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    ActivityMainBinding mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBind.toolbar);
        // Set our custom PagerAdapter as the adapter for the ViewPager
        mBind.viewpager.setAdapter(new ScaleTypePagerAdapter(getFragmentManager()));
        // Populate and connect the TabLayout with the ViewPager
        mBind.tablayout.setupWithViewPager(mBind.viewpager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_choose_image:
                startImageChooserActivity();
                return true;

            case R.id.action_edit_image_view:
                startActivity(new Intent(this, EditImageViewActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startImageChooserActivity() {
        // Launch intent to pick an image from a ContentProvider or DocumentsProvider
        Intent intent = new Intent();
        // For ACTION_OPEN_DOCUMENT vs. ACTION_GET_CONTENT see:
        // https://developer.android.com/guide/topics/providers/document-provider.html#client
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // ACTION_OPEN_DOCUMENT was introduced in API level 19 (4.4 KitKat) together with
            // the Storage Access Framework (SAF). It launches a standard activity that shows
            // all the DocumentsProvider on the device and allows to choose a document from any
            // of them. A DocumentsProvider is a special type of ContentProvider.
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            Log.v(LOG_TAG, "Using ACTION_OPEN_DOCUMENT");
        }
        else {
            // ACTION_GET_CONTENT launches the "best" app that provides a certain type of
            // content (e.g. MIME type image/*) and allows to choose an item from it. The URI
            // permissions for the selected file are only temporary.
            intent.setAction(Intent.ACTION_GET_CONTENT);
            Log.v(LOG_TAG, "Using ACTION_GET_CONTENT");
        }
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, Data.CHOOSE_IMAGE_REQUEST_CODE);
    }

    // Called when the activity, which was started by startActivityForResult, returns its result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        // The activity started by the choose image request returns the URI of the selected image.
        // Save this URI in the SharedPreferences.
        if (requestCode == Data.CHOOSE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK && resultData != null) {
                Uri uri = resultData.getData();
                settleUriPermissions(uri, resultData.getFlags());
                Pref.put(this, R.string.pref_image_key, uri.toString());
                Log.v(LOG_TAG, "onActivityResult: saved " + uri.toString() + " in SharedPreferences");
            }
        }
    }

    // Helper function for settling URI permission issues on both pre and post KitKat devices. The
    // goal is to extend the permissions for the returned URI as fas as possible. This is because
    // on every app launch, our app tries to display the image whose URI is currently stored in the
    // SharedPreferences. Attempting to access this file without URI permissions, results in a
    // java.lang.SecurityException: Permission Denial.
    private void settleUriPermissions(Uri uri, int mode) {
        Log.v(LOG_TAG, "Flags " + Util.intToBinaryString(mode) + " for URI " + uri);
        // Extract the READ, WRITE, or both flags, if they are set
        int rwFlags = mode & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        // If we used ACTION_OPEN_DOCUMENT: the READ/WRITE permissions that we got from the
        // DocumentsProvider for the returned URI are valid for the entire app, and across
        // app restarts, however, not across device reboots. With the following, we can make
        // these permissions persistent across device reboots.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Note: this has only an effect if the FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            // is set. Whether this is set, depends on the DocumentsProvider.
            getContentResolver().takePersistableUriPermission(uri, rwFlags);
        }
        // If we used ACTION_GET_CONTENT: the READ/WRITE permissions that we got from the
        // content provider for the returned URI are valid only for the current Activity.
        // That is, when the app is restarted, the new MainActivity has no permissions for
        // the same URI. With the following, we can extend these permissions to the entire
        // package, so that they persist across app restarts. There seems to be no way to
        // extend the permissions across device reboots.
        else {
            grantUriPermission(getPackageName(), uri, rwFlags);
        }
    }

    /* PagerAdapter for supplying the ViewPager with the tab pages (Fragments) to display. */
    public class ScaleTypePagerAdapter extends FragmentPagerAdapter {

        public ScaleTypePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Return the Fragment at the given position to the ViewPager
        @Override
        public Fragment getItem(int position) {
            // The Fragment we return is a PageFragment that is provided with its position
            // in the collection of pages, so that it knows which scale type it represents.
            Fragment fragment = new LauncherFragment();
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
}
