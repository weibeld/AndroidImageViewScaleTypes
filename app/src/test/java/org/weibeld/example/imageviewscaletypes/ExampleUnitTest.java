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

    @Test
    public void isValidLayoutWidthHeightEntry_test() throws Exception {
        String msg;

        // First, test if valid layout_width/layout_height entries are positively validated
        // Note: the entry for layout_width/layout_height must NOT be empty
        String[] validEntries = new String[] {
                "1dp", "5274sp", "1.0px", "0.000in", "342.34532mm", ".5dp", ".2442sp", "2.px",
                "452.in", "-10mm", "-.5dp", "-2.sp", "-0.px", "-.0in", "wrap_content", "match_parent"
        };
        for (int i = 0; i < validEntries.length; i++) {
            msg = "\"" + validEntries[i] + "\"" + " should be valid.";
            assertTrue(msg, Validator.isValidLayoutWidthHeightEntry(validEntries[i]));
        }

        // Next, test if invalid layout_width/layout_height entries are negatively validated
        String[] invalidEntries = new String[] {
                "", "1", "200.5", "10 dp", "10d", "10fd", ".dp", "-.px", "1,0dp", "1cm",
                "WRAP_CONTENT", "MATCH_PARENT"
        };
        for (int i = 0; i < invalidEntries.length; i++) {
            msg = "\"" + invalidEntries[i] + "\"" + " should be invalid.";
            assertFalse(msg, Validator.isValidLayoutWidthHeightEntry(invalidEntries[i]));
        }
    }


    @Test
    public void isValidDimenEntry_test() throws Exception {
        String msg;

        // First, test if valid dimension entries are positively validated
        // Note: the entry for a dimension field may be empty
        String[] validEntries = new String[] {
                "", "1dp", "5274sp", "1.0px", "0.000in", "342.34532mm", ".5dp", ".2442sp", "2.px",
                "452.in", "-10mm", "-.5dp", "-2.sp", "-0.px", "-.0in"
        };
        for (int i = 0; i < validEntries.length; i++) {
            msg = "\"" + validEntries[i] + "\"" + " should be valid.";
            assertTrue(msg, Validator.isValidDimenEntry(validEntries[i]));
        }

        // Next, test if invalid dimension entries are negatively validated
        String[] invalidEntries = new String[] {
                "1", "200.5", "10 dp", "10d", "10fd", ".dp", "-.px", "1,0dp", "1cm", "wrap_content",
                "match_parent"
        };
        for (int i = 0; i < invalidEntries.length; i++) {
            msg = "\"" + invalidEntries[i] + "\"" + " should be invalid.";
            assertFalse(msg, Validator.isValidDimenEntry(invalidEntries[i]));
        }
    }

    @Test
    public void isValidBooleanEntry_test() throws Exception {
        String msg;

        // First, test if valid boolean entries are positively validated
        String[] validEntries = new String[] { "true", "false" };
        for (int i = 0; i < validEntries.length; i++) {
            msg = "\"" + validEntries[i] + "\"" + " should be valid.";
            assertTrue(msg, Validator.isValidBooleanEntry(validEntries[i]));
        }

        // Next, test if invalid boolean entries are negatively validated
        String[] invalidEntries = new String[] {
                "", " ", "True", "False", "TRUE", "FALSE", " true", "false "
        };
        for (int i = 0; i < invalidEntries.length; i++) {
            msg = "\"" + invalidEntries[i] + "\"" + " should be invalid.";
            assertFalse(msg, Validator.isValidBooleanEntry(invalidEntries[i]));
        }
    }

    // Make sure that the "value" strings that we want to accept in our dimension entries are also
    // accepted by Float.parseFloat. This is because we use Float.parseFloat to convert these
    // "value" strings to floats when the ImageView is drawn in PageFragment.
    @Test
    public void parseFloat_test() throws Exception {
        String[] validStrings = new String[] {
                "1", "1.0", "1.", ".1", "-1", "-1.0", "-1.", "-.1", "0.0", "0.", ".0",
                "-0.0", "-0.", "-.0"
        };
        for (int i = 0; i < validStrings.length; i++) {
            // Throws a NumberFormatException if it does not recognise the string
            Float.parseFloat(validStrings[i]);
        }
    }
}