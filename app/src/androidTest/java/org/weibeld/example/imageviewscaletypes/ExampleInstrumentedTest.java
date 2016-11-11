package org.weibeld.example.imageviewscaletypes;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.TypedValue;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("org.weibeld.example.imageviewscaletypesexample", appContext.getPackageName());
    }

    @Test
    public void parseDimension() throws Exception {
        String[] validStrings = new String[] {
                "200dp", "20.5px", "20.5sp", "20.5mm", "20.5in"
        };
        float[] values = new float[] {
                200, 20.5f, 20.5f, 20.5f, 20.5f
        };
        int[] units = new int[] {
                TypedValue.COMPLEX_UNIT_DIP, TypedValue.COMPLEX_UNIT_PX, TypedValue.COMPLEX_UNIT_SP,
                TypedValue.COMPLEX_UNIT_MM, TypedValue.COMPLEX_UNIT_IN
        };
        for (int i = 0; i < validStrings.length; i++) {
            Util.Dimension dim = Util.parseDimension(validStrings[i]);
            assertEquals(values[i], dim.value, 0.0001);
            assertEquals(units[i], dim.unit);
        }
    }
}
