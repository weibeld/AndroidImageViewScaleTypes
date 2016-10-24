package org.weibeld.example.imageviewscaletypesexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the Toolbar as the app bar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }
}
