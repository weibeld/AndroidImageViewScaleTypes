package org.weibeld.example.imageviewscaletypes;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.weibeld.example.imageviewscaletypes.Util.parseDimensionValue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    // Test Util.parseDimensionValue
    @Test
    public void parseDimensionValue_test() throws Exception {

        // First, test valid dimension value strings
        String[] validStrings = new String[] {
                "1", "5274", "1.0", "0.000", "342.34532", ".5", ".2442", "2.", "452.", "200"
        };
        float[] validFloats = new float[] {
                1, 5274, 1, 0, 342.34532f, 0.5f, 0.2442f, 2, 452, 200
        };
        for (int i = 0; i < validStrings.length; i++) {
            assertEquals(validFloats[i], parseDimensionValue(validStrings[i]), 0.0001f);
        }

        // Then, test invalid dimension value strings (parseDimensionValue should throw exception)
        String[] invalidStrings = new String[] {
                "", " ", ".", "1.1.", "1.2.2", "2,300", "14dp", "1 ", "1.0 ", " 1 ", "14d", "-1", "-0", "-1.1"
        };
        for (String str : invalidStrings) {
            try {
                // Fail the test if parseDimensionValue does not throw a NumberFormatException
                parseDimensionValue(str);
                fail(str + " should throw NumberFormatException.");
            } catch (NumberFormatException e) {}
        }
    }
}